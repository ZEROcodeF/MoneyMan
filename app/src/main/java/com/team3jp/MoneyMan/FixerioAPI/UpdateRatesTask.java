package com.team3jp.MoneyMan.FixerioAPI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team3jp.MoneyMan.CurrencyRates;
import com.team3jp.MoneyMan.DatabaseHandler;
import com.team3jp.MoneyMan.MoneyManApplication;
import com.team3jp.MoneyMan.R;
import com.team3jp.MoneyMan.Utils.TimestampUltils;


public class UpdateRatesTask extends DialogFragment {

    TextView msgPleaseWait, msgResult, msgInfo, msgLatestUse;
    ProgressBar progressCircularLoading;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.fragment_dialog_update_rates, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msgPleaseWait = (TextView) view.findViewById(R.id.messagePleaseWait);
        msgResult = (TextView) view.findViewById(R.id.messageUpdateTaskResult);
        msgInfo = (TextView) view.findViewById(R.id.messageUpdateTaskInfo);
        msgLatestUse = (TextView) view.findViewById(R.id.messageUpdateTaskLatestUse);

        progressCircularLoading = (ProgressBar) view.findViewById(R.id.progressBarUpdateTask);

        btn = (Button) view.findViewById(R.id.btnUpdatesTaskAgree);

        msgPleaseWait.setVisibility(View.GONE);
        progressCircularLoading.setVisibility(View.GONE);

        if (isConnected(getActivity())) {
            msgResult.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            msgLatestUse.setVisibility(View.GONE);
            msgInfo.setVisibility(View.GONE);
            msgPleaseWait.setVisibility(View.VISIBLE);
            progressCircularLoading.setVisibility(View.VISIBLE);
            new UpdateRates().execute();
        } else {
            msgResult.setText(R.string.no_internet_connection);
            DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
            TimestampUltils timestampUltils = new TimestampUltils();
            String str = getString(R.string.rates_changes_at) + timestampUltils.getDateTime(databaseHandler.getLastestTimestamp()*1000);
            msgInfo.setText(str);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public class UpdateRates extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... args) {
            FixerioAPIService delta = new FixerioAPIService();
            CurrencyRates fixerio = delta.getFixerioRespone();
            if (fixerio != null) {
                DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
                if (fixerio.getTimestamp() != databaseHandler.getLastestTimestamp()) {
                    databaseHandler.addCurrencyRates(fixerio);
                }
                return true;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            msgResult.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
            msgInfo.setVisibility(View.VISIBLE);
            msgPleaseWait.setVisibility(View.GONE);
            progressCircularLoading.setVisibility(View.GONE);

            //
            DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
            TimestampUltils timestampUltils = new TimestampUltils();
            String str = getString(R.string.rates_changes_at) + timestampUltils.getDateTime(databaseHandler.getLastestTimestamp()*1000);
            msgInfo.setText(str);

            if (result) {
                msgResult.setText(R.string.update_successful);
            } else {
                msgResult.setText(R.string.update_failed);
                msgLatestUse.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

}



