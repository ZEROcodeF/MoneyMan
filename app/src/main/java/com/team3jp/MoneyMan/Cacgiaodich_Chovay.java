package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.ObjectThuchi;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;

import java.util.ArrayList;
import java.util.List;

public class Cacgiaodich_Chovay extends Fragment {

    ListView mylist;
    ArrayList<ObjectThuchi> arrayList = new ArrayList<ObjectThuchi>();

    private static final String TAG = "Cacgiaodich_Chovay";
    String name = "", sdate = "", edate = "";
    Spinner spinner;
    CurrencyUltils currencyUltils = new CurrencyUltils();
    String[] data;
    int pos;
    public static Cacgiaodich_Chovay newInstance() {
        Cacgiaodich_Chovay fragment = new Cacgiaodich_Chovay();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.cacgiaodich_chovay, null);

        //
        spinner = (Spinner) linearLayout.findViewById(R.id.spn_cackhoanchovay);
        data = getActivity().getResources().getStringArray(R.array.list_giaodich_cacluachon);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spin, R.id.tvspin, data);


        spinner.setAdapter(adapter);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) loadFragment(new Cacgiaodich_Thuvao());
                else if (position == 1) loadFragment(new Cacgiaodich_Chira());
                else if (position == 3) loadFragment(new Cacgiaodich_Vay());
                else if (position == 4) loadFragment(new Cacgiaodich_Cacgiaodichganday());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //

        Bundle bundle = getArguments();
        if (bundle != null) {
            //name = bundle.getString("tenvi", "");
            pos = bundle.getInt("pos");
            sdate = bundle.getString("ngaybatdau", "");
            edate = bundle.getString("ngayketthuc", "");
        }


        mylist = (ListView) linearLayout.findViewById(R.id.list_cacgiaodich_chovay);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();

        int ID_Wallet, size;
        size = databaseHandler.getAllWallet().size();
        if(pos>size){
            pos = size;
            ID_Wallet = 0;
        }
        else ID_Wallet = databaseHandler.getAllWallet().get(pos).getID();
        //if(name.equals("Tatca")==false &&name!="") ID_Wallet = databaseHandler.getID_Wallet(name);

        List<Logs> logsList = databaseHandler.getAllLogs();
        for (int i = 0; i < logsList.size(); i++) {
            String date = logsList.get(i).getLog_datetime();
            date = date.substring(0, 10);
            if (logsList.get(i).getLog_action() == 4 && name.equals("") == true && sdate.equals("") == true
                    && edate.equals("") == true) {        // 4: cho vay

                double money = logsList.get(i).getLog_money();

                String unit = logsList.get(i).getLog_unit();
                if (unit == null) unit = "";

                Wallet wallet = databaseHandler.getWallet(logsList.get(i).getLog_id());
                String name_wallet = wallet.getName();

                String datetime = logsList.get(i).getLog_datetime();

                ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_call_made_black_24dp,
                        name_wallet, "", currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);

                arrayList.add(objectThuchi);

            } else if (logsList.get(i).getLog_action() == 4 && pos == size) {
                if ((date.equals(sdate) == true || date.compareTo(sdate) > 0) &&
                        (date.equals(edate) == true || date.compareTo(edate) < 0)) {
                    double money = logsList.get(i).getLog_money();

                    String unit = logsList.get(i).getLog_unit();
                    if (unit == null) unit = "";

                    Wallet wallet = databaseHandler.getWallet(logsList.get(i).getLog_id());
                    String name_wallet = wallet.getName();

                    String datetime = logsList.get(i).getLog_datetime();

                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_call_made_black_24dp,
                            name_wallet, "", currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);

                    arrayList.add(objectThuchi);
                }
            } else if (logsList.get(i).getLog_action() == 4 && pos != size) {
                if (ID_Wallet == logsList.get(i).getLog_id() && (date.equals(sdate) == true || date.compareTo(sdate) > 0) &&
                        (date.equals(edate) == true || date.compareTo(edate) < 0)) {
                    double money = logsList.get(i).getLog_money();

                    String unit = logsList.get(i).getLog_unit();
                    if (unit == null) unit = "";

                    Wallet wallet = databaseHandler.getWallet(logsList.get(i).getLog_id());
                    String name_wallet = wallet.getName();

                    String datetime = logsList.get(i).getLog_datetime();

                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_call_made_black_24dp,
                            name_wallet, "", currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);

                    arrayList.add(objectThuchi);
                }
            }
        }

        final AdapterObjectThuchi adapterObjectThuchi = new AdapterObjectThuchi(getActivity(),
                R.layout.custom_row_cacgiaodich, arrayList);
        mylist.setAdapter(adapterObjectThuchi);

        FloatingActionButton fab = (FloatingActionButton) linearLayout.findViewById(R.id.fab_Loc_Chovay);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                Cacgiaodich_Loc cacgiaodich_loc = new Cacgiaodich_Loc();
                bundle1.putString("ten_fragment", "chovay");
                cacgiaodich_loc.setArguments(bundle1);
                cacgiaodich_loc.show(getFragmentManager(), "");

            }
        });

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
