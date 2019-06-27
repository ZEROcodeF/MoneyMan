package com.team3jp.MoneyMan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.TimestampUltils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Taikhoan_Taotaikhoanmoi extends DialogFragment {
    EditText edtTenvi, edtTienbandau, edtGhichu;
    Button btnLuu;
    TextView txtHuy, txtSymbolofUnit;

    private static final String TAG = "Taikhoanmoi";
    String[] units;
    Spinner spinner;
    TextView tvngaytao;

    String mUnit;
    double mMoney = 0;

    DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
    TimestampUltils timestampUltils = new TimestampUltils();
    CurrencyUltils currencyUltils = new CurrencyUltils();


    //    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taikhoan_taotaikhoanmoi, container);


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTenvi = (EditText) view.findViewById(R.id.edtTenvi);
        edtTienbandau = (EditText) view.findViewById(R.id.edtTienbandau);
        edtGhichu = (EditText) view.findViewById(R.id.edtGhichu);

        txtHuy = (TextView) view.findViewById(R.id.txtHuy);
        btnLuu = (Button) view.findViewById(R.id.btnLuu);
        txtSymbolofUnit = (TextView) view.findViewById(R.id.txtSymbolofUnit);

        tvngaytao = (TextView) view.findViewById(R.id.tvNgaytao);
        final Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        tvngaytao.setText(sdf.format(date));

        spinner = (Spinner) view.findViewById(R.id.spndonvi);
        units = getActivity().getResources().getStringArray(R.array.list_Currency);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                units);

        spinner.setAdapter(adapter1);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUnit = sharedPreferences.getString(SettingsActivity.KEY_PREF_UNIT, "VND");

        int pos = Arrays.asList(units).indexOf(mUnit);
        txtSymbolofUnit.setText(currencyUltils.symbolOf(mUnit));

        spinner.setSelection(pos);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnit = units[position];
                txtSymbolofUnit.setText(currencyUltils.symbolOf(mUnit));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtTienbandau.setText("0");
        edtTienbandau.setSelection(1);
        edtTienbandau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMoney = currencyUltils.MoneyInputWithUnit(edtTienbandau, mUnit, this, s);
                Log.d("FrgmntDlgPaidFromWallet", "Return from " + edtTienbandau.getText().toString() + " -> " + mMoney);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTenvi.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Tên ví không được rỗng", Toast.LENGTH_SHORT).show();
                } else {
                    Wallet wallet = createWallet();
                    if (wallet != null) {
                        databaseHandler.addWallet(wallet);
                        dismiss();
                        loadFragment(new Taikhoan());
                    }
                }
            }
        });

        txtHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private Wallet createWallet() {

        final Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Wallet> walletList = databaseHandler.getAllWallet();

        int id = 1;

        int size = walletList.size();
        if (size > 0) id = walletList.get(size - 1).getID() + 1;

        String name = edtTenvi.getText().toString();
        int type = 0;

//        if (size > 0 && walletList.get(0).getUnit() != null) unit = walletList.get(0).getUnit();
        String startdate = sdf.format(date);
        String enddate = null;

        Wallet wallet = new Wallet(id, name, type, mMoney, mMoney, mUnit, startdate, enddate);

        return wallet;

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static boolean checkNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {

            } else return false;
        }
        return true;
    }

}
