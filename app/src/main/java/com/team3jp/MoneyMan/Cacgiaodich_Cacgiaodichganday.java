package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.ObjectThuchi;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;

import java.util.ArrayList;
import java.util.List;

public class Cacgiaodich_Cacgiaodichganday extends Fragment {

    ListView mylist;
    ArrayList<ObjectThuchi> arrayList = new ArrayList<ObjectThuchi>();
    Spinner spinner;

    CurrencyUltils currencyUltils = new CurrencyUltils();

    String[] data;
    private static final String TAG = "Cacgiaodich_Cacgiaodichganday";

    public static Cacgiaodich_Cacgiaodichganday newInstance() {
        Cacgiaodich_Cacgiaodichganday fragment = new Cacgiaodich_Cacgiaodichganday();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.cacgiaodich_cacgiaodichganday, null);

        //
        spinner = (Spinner) linearLayout.findViewById(R.id.spn_tatcagiaodich);
        data = getActivity().getResources().getStringArray(R.array.list_giaodich_cacluachon);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spin, R.id.tvspin, data);


        spinner.setAdapter(adapter);
        spinner.setSelection(4);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) loadFragment(new Cacgiaodich_Thuvao());
                else if (position == 1) loadFragment(new Cacgiaodich_Chira());
                else if (position == 2) loadFragment(new Cacgiaodich_Chovay());
                else if (position == 3) loadFragment(new Cacgiaodich_Vay());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //

        mylist = (ListView) linearLayout.findViewById(R.id.list_cacgiaodichganday);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Logs> logsList = databaseHandler.getAllLogs();
        for (int i = 0; i < logsList.size(); i++) {

            if (logsList.get(i).getLog_action() == 1 || logsList.get(i).getLog_action() == 3 || logsList.get(i).getLog_action() == 4) {        // 1: thu vào
                String des = logsList.get(i).getLog_description();
                double money = logsList.get(i).getLog_money();

                Wallet wallet = databaseHandler.getWallet(logsList.get(i).getLog_id());
                String name_wallet = wallet.getName();

                String unit = logsList.get(i).getLog_unit();
                if (unit == null) unit = "";

                String datetime = logsList.get(i).getLog_datetime();

                ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                        des, "Từ: " + name_wallet, currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);

                arrayList.add(objectThuchi);

            } else if (logsList.get(i).getLog_action() == 2) {    // 2: chi ra
                String des = logsList.get(i).getLog_description();
                double money = logsList.get(i).getLog_money();

                Wallet wallet = databaseHandler.getWallet(logsList.get(i).getLog_id());
                String name_wallet = wallet.getName();

                String unit = logsList.get(i).getLog_unit();
                if (unit == null) unit = "";

                String datetime = logsList.get(i).getLog_datetime();

                ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                        des, "Từ: " + name_wallet, "-" + currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);

                arrayList.add(objectThuchi);

            }
        }

        final AdapterObjectThuchi adapterObjectThuchi = new AdapterObjectThuchi(getActivity(),
                R.layout.custom_row_cacgiaodich, arrayList);
        mylist.setAdapter(adapterObjectThuchi);

        return linearLayout;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

