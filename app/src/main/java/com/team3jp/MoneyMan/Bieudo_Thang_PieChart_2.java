package com.team3jp.MoneyMan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bieudo_Thang_PieChart_2 extends Fragment implements OnChartValueSelectedListener {


    PieChart mChart;

    TextView tvThangtruoc;

    String month;

    ObjectPieChart objectPieChart = new ObjectPieChart(0.d, 0.d);
    CurrencyUltils currencyUltils = new CurrencyUltils();
    double thu = 0;
    double chi = 0;
    String unit= "₫";
    MoneyIndexer mThu,mChi;

    public static Bieudo_Thang_PieChart_2 newInstance() {
        Bieudo_Thang_PieChart_2 fragment = new Bieudo_Thang_PieChart_2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.bieudo_thang_piechart_2, null);

        tvThangtruoc = (TextView) relativeLayout.findViewById(R.id.tvthang_2);

        final Date date = new Date();
        String strDateFormat = "yyyy/MM";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String currentDate = sdf.format(date);

        String nam = currentDate.substring(0, 4);
        String thang = currentDate.substring(5);

        int tmp = Integer.parseInt(thang);

        if (tmp >= 2) {
            tmp--;
            currentDate = nam + "/" + String.valueOf(tmp);
        } else {
            tmp = 12;
            int tmp1 = Integer.parseInt(nam);
            tmp1--;
            currentDate = String.valueOf(tmp1) + "/" + String.valueOf(tmp);
        }

        tvThangtruoc.setText("Tháng " + String.valueOf(tmp));
        month = String.valueOf(tmp);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Logs> logsList = databaseHandler.getAllLogs();

//        if (logsList.size() > 0) {
//            unit = logsList.get(0).getLog_unit();
//            if (unit == null) unit = "";
//        }
        mThu = new MoneyIndexer(getActivity());
        mChi = new MoneyIndexer(getActivity());
        for (int i = 0; i < logsList.size(); i++) {

            if (logsList.get(i).getLog_datetime().startsWith(currentDate) == true) {
                if (logsList.get(i).getLog_action() == 1 || logsList.get(i).getLog_action() == 3) {
                    // 1: thu vào           3: đi vay
                    //thu += logsList.get(i).getLog_money();
                    mThu.addMoneyWithTimestamp(logsList.get(i).getLog_money(),logsList.get(i).getLog_unit(),logsList.get(i).getLog_timestamp());

                } else if (logsList.get(i).getLog_action() == 2 || logsList.get(i).getLog_action() == 4) {
                    // 2: chi ra        4: cho vay
                    //chi += logsList.get(i).getLog_money();
                    mChi.addMoneyWithTimestamp(logsList.get(i).getLog_money(),logsList.get(i).getLog_unit(),logsList.get(i).getLog_timestamp());

                }
            }
        }
        thu = mThu.getTotal();
        chi = mChi.getTotal();

        objectPieChart.setChi(chi);
        objectPieChart.setThu(thu);


        mChart = (PieChart) relativeLayout.findViewById(R.id.piechart_thang_2);
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
        //      addDataSet(mChart);
        return relativeLayout;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        String tmp = null;
        if (h.getX() == 0) tmp = "Tổng thu nhập tháng " + month + ": ";
        else tmp = "Tổng chi phí tháng " + month + ": ";
        Toast.makeText(getActivity(), tmp +currencyUltils.formatCurrencyWithSymbol(e.getY(),"VND"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

}
