package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.ObjectThuchi;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;

import java.util.ArrayList;
import java.util.List;

public class Taikhoan_Xemchitiet extends Fragment {

    ListView mylist;
    ArrayList<ObjectThuchi> arrayList = new ArrayList<ObjectThuchi>();

    Button btnOK;
    TextView tenvi;
    int ID_wallet, pos;
    String name_wallet;

    CurrencyUltils currencyUltils = new CurrencyUltils();

    public static Taikhoan_Xemchitiet newInstance() {
        Taikhoan_Xemchitiet fragment = new Taikhoan_Xemchitiet();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.taikhoan_xemchitiet, null);

        tenvi = (TextView) relativeLayout.findViewById(R.id.tvTenvi);

        mylist = (ListView) relativeLayout.findViewById(R.id.list_cachoatdong);

        btnOK = (Button) relativeLayout.findViewById(R.id.btn_xemchitiet_OK);


        Bundle bundle = getArguments();
        //name_wallet = bundle.getString("tenvi");
        pos = bundle.getInt("pos");

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        //ID_wallet = databaseHandler.getID_Wallet(name_wallet);

        final List<Logs> logsList = databaseHandler.getAllLogs();

        Wallet wallet = databaseHandler.getAllWallet().get(pos);
        ID_wallet = wallet.getID();
        name_wallet=wallet.getName();
        tenvi.setText(name_wallet);

        for (int i = 0; i < logsList.size(); i++) {

            if (ID_wallet == logsList.get(i).getLog_id()) {
                String des = logsList.get(i).getLog_description();
                double money = logsList.get(i).getLog_money();

                //if(logsList.get(i).getLog_action()==2)money = "-"+money;

                String unit = logsList.get(i).getLog_unit();
                if (unit == null) unit = "";
                String datetime = logsList.get(i).getLog_datetime();


                if (logsList.get(i).getLog_action() != 2) {
                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                            des, "", currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);
                    arrayList.add(objectThuchi);
                } else {
                    ObjectThuchi objectThuchi = new ObjectThuchi(R.drawable.ic_import_export_black_24dp,
                            des, "", "-" + currencyUltils.formatCurrencyWithSymbol(money,unit), datetime);
                    arrayList.add(objectThuchi);
                }
            }
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Taikhoan());
            }
        });


        final AdapterObjectThuchi adapterObjectThuchi = new AdapterObjectThuchi(getActivity(),
                R.layout.custom_row_cacgiaodich, arrayList);
        mylist.setAdapter(adapterObjectThuchi);


        return relativeLayout;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}