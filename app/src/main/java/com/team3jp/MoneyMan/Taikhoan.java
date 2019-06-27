package com.team3jp.MoneyMan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.Fragments.DialogFragmentOtherUnits;
import com.team3jp.MoneyMan.Items.ObjectTaikhoan;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.MoneyIndexer;

import java.util.ArrayList;
import java.util.List;


public class Taikhoan extends Fragment {

    ListView mylist;
    ArrayList<ObjectTaikhoan> arrayList = new ArrayList<ObjectTaikhoan>();

    private static final String TAG = "TaikhoanFragment";

    CurrencyUltils currencyUltils = new CurrencyUltils();

    CardView cardViewTotal;
    TextView tvTong, tvAttention;

    public static Taikhoan newInstance() {
        Taikhoan fragment = new Taikhoan();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.taikhoan_activity, null);


        cardViewTotal = (CardView) relativeLayout.findViewById(R.id.taikhoan_total_view);
        tvTong = (TextView) relativeLayout.findViewById(R.id.tvTong);
        tvAttention = (TextView) relativeLayout.findViewById(R.id.taikhoan_include_many_units);
        tvAttention.setVisibility(View.GONE);

        mylist = (ListView) relativeLayout.findViewById(R.id.list_Taikhoan);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();

        List<Wallet> walletList = databaseHandler.getAllWallet();

        final MoneyIndexer moneyIndexer = new MoneyIndexer(getActivity());

        for (int i = 0; i < walletList.size(); i++) {
            String name = walletList.get(i).getName();
            double currentmoney = walletList.get(i).getCurrentmoney();
            String date = walletList.get(i).getStardate();
            String unit = walletList.get(i).getUnit();

            if (!unit.equals("")) {
                moneyIndexer.addMoneyWithLatestRates(currentmoney, unit);
            }

            ObjectTaikhoan objectTaikhoan = new ObjectTaikhoan(R.drawable.ic_local_grocery_store_black_24dp,
                    name, currencyUltils.formatCurrencyWithSymbol(currentmoney,unit), date);

            arrayList.add(objectTaikhoan);
        }

        //Xử lý View tổng
        CurrencyUltils currencyUltils = new CurrencyUltils();
        MoneyManApplication.setMoneyIndexer(moneyIndexer);
        tvTong.setText(currencyUltils.formatCurrencyWithSymbol(moneyIndexer.getTotal(), moneyIndexer.getDefaultUnit()));
        Log.d(TAG, "Default unit is: " + moneyIndexer.getDefaultUnit());
        if (moneyIndexer.numberOthersUnit() > 0) {
            tvAttention.setVisibility(View.VISIBLE);
            cardViewTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:Là nơi chứa trung gian với DialogFrament MoneyOtherUnits
                    showDialogOthersUnit();
                }
            });
        }

        final AdapterObjectTaikhoan adapterObjectTaikhoan = new AdapterObjectTaikhoan(getActivity(),
                R.layout.custom_row_taikhoan, arrayList, getFragmentManager());

        mylist.setAdapter(adapterObjectTaikhoan);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = arrayList.get(position).getTen();        // Lấy tên ví
                    Log.d(TAG, "Item:" + position + " is selected");
                    showAlertDialog(name, position);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_taikhoan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_Taotaikhoanmoi();

            }
        });

        return relativeLayout;
    }


    private void showDialog_Taotaikhoanmoi() {
        FragmentManager fm = getFragmentManager();
        Taikhoan_Taotaikhoanmoi taikhoan_taotaikhoanmoi = new Taikhoan_Taotaikhoanmoi();
        taikhoan_taotaikhoanmoi.show(fm, "");
    }

    private void showDialogOthersUnit() {
        FragmentManager fm = getFragmentManager();
        DialogFragmentOtherUnits dialogFragmentOtherUnits = new DialogFragmentOtherUnits();
        dialogFragmentOtherUnits.show(fm, "");
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showAlertDialog(final String title, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_local_grocery_store_black_24dp);
        builder.setMessage("Lựa chọn của bạn đối với " + title + "?");
        builder.setCancelable(true);        // dialog sẽ mất khi kích ra ngoài dialog, flase: sẽ không mất

        builder.setNeutralButton("Xem chi tiết", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Taikhoan_Xemchitiet taikhoan_xemchitiet = new Taikhoan_Xemchitiet();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                // bundle.putString("tenvi",title);
                bundle.putInt("pos",pos);
                taikhoan_xemchitiet.setArguments(bundle);

                ft.replace(R.id.frame_container, taikhoan_xemchitiet);
                ft.commit();

            }
        });


        builder.setPositiveButton("Xóa ví", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (deleteWallet(title, pos)) {
                    dialogInterface.dismiss();
                    loadFragment(new Taikhoan());
                    Toast.makeText(getActivity(), "Đã xóa ví " + title + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Sửa ví", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //          Toast.makeText(getActivity(),"Sửa ví",Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                Taikhoan_Chinhsua taikhoan_chinhsua = new Taikhoan_Chinhsua();
                //bundle.putString("tenvi",title);
                bundle.putInt("pos",pos);
                taikhoan_chinhsua.setArguments(bundle);
                taikhoan_chinhsua.show(getFragmentManager(), "");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    private boolean deleteWallet(String name_wallet, int pos) {
        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Wallet> walletList = databaseHandler.getAllWallet();

        if(walletList.size()>1) {

//            int ID_wallet = databaseHandler.getID_Wallet(name_wallet);
//            Wallet wallet = databaseHandler.getWallet(ID_wallet);
//            databaseHandler.deleteWallet(wallet);
//            return true;

            for(int i = 0; i<walletList.size();i++){
                if(i==pos) {
                    Wallet wallet = walletList.get(i);
                    databaseHandler.deleteWallet(wallet);
                    return true;
                }
            }
            return false;
        }
        else {
            Toast.makeText(getActivity(), "Không xóa được ví cuối cùng!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}