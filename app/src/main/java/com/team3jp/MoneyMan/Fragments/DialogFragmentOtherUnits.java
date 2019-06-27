package com.team3jp.MoneyMan.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team3jp.MoneyMan.DatabaseHandler;
import com.team3jp.MoneyMan.MoneyManApplication;
import com.team3jp.MoneyMan.R;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.MoneyIndexer;
import com.team3jp.MoneyMan.Utils.TimestampUltils;

public class DialogFragmentOtherUnits extends DialogFragment {

    static final String TAG = "DlgFragmentOtherUnits";

    TextView tvOtherUnitsTotalTitle, tvOtherUnitsAUDAmount, tvOtherUnitsEURAmount, tvOtherUnitsGBPAmount, tvOtherUnitsJPYAmount, tvOtherUnitsUSDAmount, tvOtherUnitsVNDAmount, tvOtherUnitsInfo;
    LinearLayout tvOtherUnitsAUD, tvOtherUnitsEUR, tvOtherUnitsGBP, tvOtherUnitsJPY, tvOtherUnitsUSD, tvOtherUnitsVND;
    Button btnDismiss;
    MoneyIndexer moneyIndexer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_other_units, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOtherUnitsTotalTitle = (TextView) view.findViewById(R.id.tvOtherUnitsTotalTitle);
        tvOtherUnitsInfo = (TextView) view.findViewById(R.id.tvOtherUnitsInfo);

        tvOtherUnitsAUD = (LinearLayout) view.findViewById(R.id.tvOtherUnitsAUD);
        tvOtherUnitsEUR = (LinearLayout) view.findViewById(R.id.tvOtherUnitsEUR);
        tvOtherUnitsGBP = (LinearLayout) view.findViewById(R.id.tvOtherUnitsGBP);
        tvOtherUnitsJPY = (LinearLayout) view.findViewById(R.id.tvOtherUnitsJPY);
        tvOtherUnitsUSD = (LinearLayout) view.findViewById(R.id.tvOtherUnitsUSD);
        tvOtherUnitsVND = (LinearLayout) view.findViewById(R.id.tvOtherUnitsVND);

        btnDismiss = (Button) view.findViewById(R.id.btnOtherUnitsDismiss);

        moneyIndexer = new MoneyIndexer(getActivity());
        Log.d(TAG, "Data of Money indexer: MoneyManApp: " + MoneyManApplication.getMoneyIndexer().getTotal());
        moneyIndexer.copy(MoneyManApplication.getMoneyIndexer());
        Log.d(TAG, "Data of Money indexer: this moneyindexer: " + moneyIndexer.getTotal());

        CurrencyUltils currencyUltils = new CurrencyUltils();
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        TimestampUltils timestampUltils = new TimestampUltils();

        String str = currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getTotal(), moneyIndexer.getDefaultUnit()) + " " + getResources().getString(R.string.msg_dialog_other_units_included);
        tvOtherUnitsTotalTitle.setText(str);

        if (moneyIndexer.getAUD() == 0) {
            tvOtherUnitsAUD.setVisibility(View.GONE);
        } else {
            tvOtherUnitsAUDAmount = (TextView) view.findViewById(R.id.tvOtherUnitsAUDAmount);
            tvOtherUnitsAUDAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getAUD(), "AUD"));
            Log.d(TAG, "AUD view opened!");
        }
        if (moneyIndexer.getEUR() == 0) {
            tvOtherUnitsEUR.setVisibility(View.GONE);
        } else {
            tvOtherUnitsEURAmount = (TextView) view.findViewById(R.id.tvOtherUnitsEURAmount);
            tvOtherUnitsEURAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getEUR(), "EUR"));
            Log.d(TAG, "EUR view opened!");
        }
        if (moneyIndexer.getGBP() == 0) {
            tvOtherUnitsGBP.setVisibility(View.GONE);
        } else {
            tvOtherUnitsGBPAmount = (TextView) view.findViewById(R.id.tvOtherUnitsGBPAmount);
            tvOtherUnitsGBPAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getGBP(), "GBP"));
            Log.d(TAG, "GBP view opened!");
        }
        if (moneyIndexer.getJPY() == 0) {
            tvOtherUnitsJPY.setVisibility(View.GONE);
        } else {
            tvOtherUnitsJPYAmount = (TextView) view.findViewById(R.id.tvOtherUnitsJPYAmount);
            tvOtherUnitsJPYAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getJPY(), "JPY"));
            Log.d(TAG, "JPY view opened!");
        }
        if (moneyIndexer.getUSD() == 0) {
            tvOtherUnitsUSD.setVisibility(View.GONE);
        } else {
            tvOtherUnitsUSDAmount = (TextView) view.findViewById(R.id.tvOtherUnitsUSDAmount);
            tvOtherUnitsUSDAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getUSD(), "USD"));
            Log.d(TAG, "USD view opened!");
        }
        if (moneyIndexer.getVND() == 0) {
            tvOtherUnitsVND.setVisibility(View.GONE);
        } else {
            tvOtherUnitsVNDAmount = (TextView) view.findViewById(R.id.tvOtherUnitsVNDAmount);
            tvOtherUnitsVNDAmount.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getVND(), "VND"));
            Log.d(TAG, "VND view opened!");
        }


        str = getString(R.string.dialog_fragment_other_units_msg_info) + timestampUltils.getDateTime(databaseHandler.getLastestTimestamp()*1000);
        tvOtherUnitsInfo.setText(str);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
