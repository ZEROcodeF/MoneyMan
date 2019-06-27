package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.team3jp.MoneyMan.Fragments.DialogFragmentOtherUnits;
import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.MoneyIndexer;

import java.util.List;

public class Tongquan_1 extends Fragment implements View.OnClickListener {

    MoneyIndexer mTongthu, mTongchi, mSodu, mTongchovay, mTongno;
    CardView cvTongthu, cvTongchi, cvSodu, cvTongchovay, cvTongno;
    TextView tvtongthu, tvtongchi, tvsodu, tvtongchovay, tvtongno;
    String unit = "";

    public static Tongquan_1 newInstance() {
        Tongquan_1 fragment = new Tongquan_1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) inflater.inflate(R.layout.tongquan_1, container, false);

        tvtongchi = (TextView) horizontalScrollView.findViewById(R.id.tvTongchi);
        tvtongthu = (TextView) horizontalScrollView.findViewById(R.id.tvTongthu);
        tvsodu = (TextView) horizontalScrollView.findViewById(R.id.tvSodu);
        tvtongchovay = (TextView) horizontalScrollView.findViewById(R.id.tvTongchovay);
        tvtongno = (TextView) horizontalScrollView.findViewById(R.id.tvTongno);

        cvTongthu = (CardView) horizontalScrollView.findViewById(R.id.cvTongthu);
        cvTongchi = (CardView) horizontalScrollView.findViewById(R.id.cvTongchi);
        cvSodu = (CardView) horizontalScrollView.findViewById(R.id.cvSodu);
        cvTongchovay = (CardView) horizontalScrollView.findViewById(R.id.cvTongchovay);
        cvTongno = (CardView) horizontalScrollView.findViewById(R.id.cvTongno);

        mTongthu = new MoneyIndexer(getActivity());
        mTongchi = new MoneyIndexer(getActivity());
        mSodu = new MoneyIndexer(getActivity());
        mTongchovay = new MoneyIndexer(getActivity());
        mTongno = new MoneyIndexer(getActivity());

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        final MoneyIndexer moneyIndexer = MoneyManApplication.getMoneyIndexer();
        CurrencyUltils currencyUltils = new CurrencyUltils();

        List<Logs> logsList = databaseHandler.getAllLogs();

        if (logsList.size() > 0) {
            unit = logsList.get(0).getLog_unit();
            if (unit == null) unit = "";
        }

        for (int i = 0; i < logsList.size(); i++) {
            if (logsList.get(i).getLog_action() == 1) {        // 1: Thu vào
                mTongthu.addMoneyWithTimestamp(logsList.get(i).getLog_money(), logsList.get(i).getLog_unit(), logsList.get(i).getLog_timestamp());
            } else if (logsList.get(i).getLog_action() == 2) {        // 2: chi ra
                mTongchi.addMoneyWithTimestamp(logsList.get(i).getLog_money(), logsList.get(i).getLog_unit(), logsList.get(i).getLog_timestamp());
            } else if (logsList.get(i).getLog_action() == 3) {        // 3: đi vay
                mTongno.addMoneyWithTimestamp(logsList.get(i).getLog_money(), logsList.get(i).getLog_unit(), logsList.get(i).getLog_timestamp());
            } else if (logsList.get(i).getLog_action() == 4) {        // 3: cho vay
                mTongchovay.addMoneyWithTimestamp(logsList.get(i).getLog_money(), logsList.get(i).getLog_unit(), logsList.get(i).getLog_timestamp());
            }
        }

        mSodu.addFromMoneyIndexer(mTongthu);
        mSodu.addFromMoneyIndexer(mTongno);
        mSodu.subtractFromMoneyIndexer(mTongchovay);
        mSodu.subtractFromMoneyIndexer(mTongchi);

        tvtongchi.setText(currencyUltils.formatCurrencyWithSymbol(mTongchi.getTotal(), mTongchi.getDefaultUnit()));
        tvtongthu.setText(currencyUltils.formatCurrencyWithSymbol(mTongthu.getTotal(), mTongthu.getDefaultUnit()));
        tvtongchovay.setText(currencyUltils.formatCurrencyWithSymbol(mTongchovay.getTotal(), mTongchovay.getDefaultUnit()));
        tvtongno.setText(currencyUltils.formatCurrencyWithSymbol(mTongno.getTotal(), mTongno.getDefaultUnit()));
        tvsodu.setText(currencyUltils.formatCurrencyWithSymbol(mSodu.getTotal(), mSodu.getDefaultUnit()));

        cvTongthu.setOnClickListener(this);
        cvTongchi.setOnClickListener(this);
        cvSodu.setOnClickListener(this);
        cvTongchovay.setOnClickListener(this);
        cvTongno.setOnClickListener(this);

        return horizontalScrollView;
    }

    public void showDialogOtherUnits() {
        FragmentManager fm = getFragmentManager();
        DialogFragmentOtherUnits dialogFragmentOtherUnits = new DialogFragmentOtherUnits();
        dialogFragmentOtherUnits.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvTongthu:
                if (mTongthu.numberOthersUnit() > 0) {
                    MoneyManApplication.setMoneyIndexer(mTongthu);
                    showDialogOtherUnits();
                }
                break;
            case R.id.cvTongchi:
                if (mTongchi.numberOthersUnit() > 0) {
                    MoneyManApplication.setMoneyIndexer(mTongchi);
                    showDialogOtherUnits();
                }
                break;
            case R.id.cvSodu:
                if (mSodu.numberOthersUnit() > 0) {
                    MoneyManApplication.setMoneyIndexer(mSodu);
                    showDialogOtherUnits();
                }
                break;
            case R.id.cvTongchovay:
                if (mTongchovay.numberOthersUnit() > 0) {
                    MoneyManApplication.setMoneyIndexer(mTongchovay);
                    showDialogOtherUnits();
                }
                break;
            case R.id.cvTongno:
                if (mTongno.numberOthersUnit() > 0) {
                    MoneyManApplication.setMoneyIndexer(mTongno);
                    showDialogOtherUnits();
                    break;
                }
        }
    }

}

