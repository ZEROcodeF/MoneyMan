package com.team3jp.MoneyMan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.ObjectPieChart;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.MoneyIndexer;

import java.util.ArrayList;
import java.util.List;

public class Bieudo_Ngaychinhxac_Piechart extends Fragment implements OnChartValueSelectedListener {


    PieChart mChart;

    ObjectPieChart objectPieChart = new ObjectPieChart(0, 0);

    double thu = 0;
    double chi = 0;
    CurrencyUltils currencyUltils = new CurrencyUltils();

    MoneyIndexer mThu,mChi;

    String unit= "₫", ngaybatdau = "", ngayketthuc = "";

    public static Bieudo_Ngaychinhxac_Piechart newInstance() {
        Bieudo_Ngaychinhxac_Piechart fragment = new Bieudo_Ngaychinhxac_Piechart();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.bieudo_ngaychinhxac_piechart, null);

        Bundle bundle = getArguments();
        //String name_wallet = bundle.getString("tenvi","");
        int pos = bundle.getInt("pos");
        String startdate = bundle.getString("ngaybatdau", "");
        String enddate = bundle.getString("ngayketthuc", "");

        ngaybatdau = startdate;
        ngayketthuc = enddate;

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();

        int ID_wallet = databaseHandler.getAllWallet().get(pos).getID();//databaseHandler.getID_Wallet(name_wallet);

        List<Logs> logsList = databaseHandler.getAllLogs();

//        if (logsList.size() > 0) {
//            unit = logsList.get(0).getLog_unit();
//            if (unit == null) unit = "";
//        }
        mThu = new MoneyIndexer(getActivity());
        mChi = new MoneyIndexer(getActivity());

        for (int i = 0; i < logsList.size(); i++) {
            String date = logsList.get(i).getLog_datetime();
            date = date.substring(0, 10);

            if ((date.equals(startdate) == true || date.compareTo(startdate) > 0) &&
                    (date.equals(enddate) == true || date.compareTo(enddate) < 0) &&
                    logsList.get(i).getLog_id() == ID_wallet) {
                if (logsList.get(i).getLog_action() == 1 || logsList.get(i).getLog_action() == 3)
                    //thu += logsList.get(i).getLog_money();
                    mThu.addMoneyWithTimestamp(logsList.get(i).getLog_money(),logsList.get(i).getLog_unit(),logsList.get(i).getLog_timestamp());

                else if (logsList.get(i).getLog_action() == 2 || logsList.get(i).getLog_action() == 4)
                    //chi += logsList.get(i).getLog_money();
                    mChi.addMoneyWithTimestamp(logsList.get(i).getLog_money(),logsList.get(i).getLog_unit(),logsList.get(i).getLog_timestamp());
            }
        }
        thu = mThu.getTotal();
        chi = mChi.getTotal();

        objectPieChart.setChi(chi);
        objectPieChart.setThu(thu);


        mChart = (PieChart) relativeLayout.findViewById(R.id.piechart);
        mChart.setRotationEnabled(true);
        mChart.setDescription(new Description());
        mChart.setHoleRadius(30f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("3JP");
        mChart.setCenterTextSize(15);
        mChart.setDrawEntryLabels(true);


        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        double[] yData = {objectPieChart.getThu(), objectPieChart.getChi()};        // Thu trước chi sau
        String[] xData = {"Thu", "Chi"};

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry((long) yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Thu Chi");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(10);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#00695C"));
        colors.add(Color.parseColor("#FF5722"));

        pieDataSet.setColors(colors);

        Legend legend = mChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        mChart.setData(pieData);
        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(this);
        //    addDataSet(mChart);
        return relativeLayout;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        String tmp = null;
        if (h.getX() == 0)
            tmp = getString(R.string.total_income_between) + ngaybatdau + "-" + ngayketthuc + ": ";
        else tmp = getString(R.string.total_paid_between) + ngaybatdau + "-" + ngayketthuc + ": ";
        Toast.makeText(getActivity(), tmp +currencyUltils.formatCurrencyWithSymbol(e.getY(),"VND"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }


}
