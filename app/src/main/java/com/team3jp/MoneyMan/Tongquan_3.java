package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.ObjectThuchi;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tongquan_3 extends Fragment {

    ListView mylist;
    ArrayList<ObjectThuchi> arrayList = new ArrayList<ObjectThuchi>();

    private static final String TAG = "Tongquan_3";

    CurrencyUltils currencyUltils = new CurrencyUltils();

    public static Tongquan_3 newInstance() {
        Tongquan_3 fragment = new Tongquan_3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CardView cardView = (CardView) inflater.inflate(R.layout.tongquan_3, null);

        mylist = (ListView) cardView.findViewById(R.id.list_tongquan_3);

        final Date date = new Date();
        String strDateFormat = "yyyy/MM/dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        // tvngaytao.setText( sdf.format(date));

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Logs> logsList = databaseHandler.getAllLogs();

        int size = logsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {            // lấy tất cả hd trong ngày
                String d = logsList.get(i).getLog_datetime();
                d = d.substring(0, 10);
                if (sdf.format(date).equals(d) == true) {

                    String des = logsList.get(size - i - 1).getLog_description();

                    Wallet wallet = databaseHandler.getWallet(logsList.get(size - i - 1).getLog_id());
                    String name_wallet = wallet.getName();


                    String unit = wallet.getUnit();
                    if (unit == null) unit = "";

                    double money = logsList.get(size - i - 1).getLog_money();
                    String datetime = logsList.get(size - i - 1).getLog_datetime();

                    if (logsList.get(size - i - 1).getLog_action() == 2) {
                        ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                                des, "Từ: " + name_wallet, "-" + currencyUltils.formatCurrencyWithSymbol(money, unit),
                                datetime);
                        arrayList.add(objectThuchi);
                    } else {
                        ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                                des, "Từ: " + name_wallet, currencyUltils.formatCurrencyWithSymbol(money, unit),
                                datetime);
                        arrayList.add(objectThuchi);
                    }
                }

            }

        }

//        if (size > 0) {
//            if (size == 1) {
//                String des = logsList.get(0).getLog_description();
//
//                Wallet wallet = databaseHandler.getWallet(logsList.get(0).getLog_id());
//                String name_wallet = wallet.getName();
//
//                String unit = wallet.getUnit();
//                if(unit==null)unit="";
//
//                double money = logsList.get(0).getLog_money();
//
//                String datetime = logsList.get(0).getLog_datetime();
//
//                if(logsList.get(0).getLog_action()==2) {
//                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
//                            des, "Từ: " + name_wallet, "-"+money + " " + unit, datetime);
//                    arrayList.add(objectThuchi);
//                }
//                else {
//                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
//                            des, "Từ: " + name_wallet, money + " " + unit, datetime);
//                    arrayList.add(objectThuchi);
//                }
//
//            } else if (size >= 2) {
//
//                for (int i = 0; i < 2; i++) {       // chỉ lấy 2 hoạt động mới nhất trong Logs
//
//                    String des = logsList.get(size - i - 1).getLog_description();
//
//                    Wallet wallet = databaseHandler.getWallet(logsList.get(size - i - 1).getLog_id());
//                    String name_wallet = wallet.getName();
//
//
//                    String unit = wallet.getUnit();
//                    if(unit==null)unit="";
//
//                    double money = logsList.get(size - i - 1).getLog_money();
//                    String datetime = logsList.get(size - i - 1).getLog_datetime();
//
//                    if(logsList.get(size-i-1).getLog_action()==2) {
//                        ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
//                                des, "Từ: " + name_wallet, "-"+money + " " + unit, datetime);
//                        arrayList.add(objectThuchi);
//                    }
//                    else {
//                        ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
//                                des, "Từ: " + name_wallet, money + " " + unit, datetime);
//                        arrayList.add(objectThuchi);
//                    }
//                }
//
//            }
//        }

        final AdapterObjectThuchi adapterObjectThuchi = new AdapterObjectThuchi(getActivity(),
                R.layout.custom_row_cacgiaodich, arrayList);
        mylist.setAdapter(adapterObjectThuchi);
        setListViewHeightBasedOnChildren(mylist);

        return cardView;
    }


    //Hàm này để xử lý việc ScrollView không hoạt động trên listview con
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
