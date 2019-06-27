package com.team3jp.MoneyMan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.DatabaseHandler;
import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.MoneyManApplication;
import com.team3jp.MoneyMan.R;
import com.team3jp.MoneyMan.Taikhoan;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.TimestampUltils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.team3jp.MoneyMan.Taikhoan_Taotaikhoanmoi.checkNumber;

public class FragmentDialogPaidFromWallet extends DialogFragment {

    EditText tienchira, ghichu;
    Button btnChira;
    TextView ngaychi, chonhangmuc, boqua, txtWalletName;//, viduocchon;
    Spinner spinner_chonhangmuc;
    CheckBox checkFrequently;

    ArrayList<String> list_vi = new ArrayList<String>();
    String[] list_hangmuc;

    String name_description;
    int pos;
    String mUnit;
    double mMoney = 0;

    DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
    TimestampUltils timestampUltils = new TimestampUltils();
    CurrencyUltils currencyUltils = new CurrencyUltils();

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static FragmentDialogPaidFromWallet newInstance(int position) {
        FragmentDialogPaidFromWallet f = new FragmentDialogPaidFromWallet();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("position");
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_paid_from_wallet, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtWalletName = (TextView) view.findViewById(R.id.txtNameWallet);

        tienchira = (EditText) view.findViewById(R.id.edt_fab_Tienthunhap);
        ghichu = (EditText) view.findViewById(R.id.edt_fab_Ghichuthu);

        boqua = (TextView) view.findViewById(R.id.txtBoquathu);
        btnChira = (Button) view.findViewById(R.id.btnThemvaothu);

        checkFrequently = (CheckBox) view.findViewById(R.id.checkboxFrequently);

        ngaychi = (TextView) view.findViewById(R.id.tvNgaythu);

        Date date = new Date();
        ngaychi.setText(timestampUltils.getDate(date.getTime()));

        chonhangmuc = (TextView) view.findViewById(R.id.tvHangmucthu);


        txtWalletName.setText(databaseHandler.getAllWallet().get(pos).getName());


        // Hạng mục của ví
        spinner_chonhangmuc = (Spinner) view.findViewById(R.id.spnHangmucthu);
        list_hangmuc = getActivity().getResources().getStringArray(R.array.list_Hangmucchi);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                list_hangmuc) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };

        spinner_chonhangmuc.setAdapter(adapter1);

        spinner_chonhangmuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name_description = list_hangmuc[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        boqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mUnit = databaseHandler.getAllWallet().get(pos).getUnit();
        Log.e("FrmntDlgPaidFromWallet", "Unit of pos=" + databaseHandler.getAllWallet().get(pos).getUnit());

        tienchira.setText("0");
        tienchira.setSelection(1);
        tienchira.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               mMoney = currencyUltils.MoneyInputWithUnit(tienchira, mUnit, this, s);
               Log.d("FrgmntDlgPaidFromWallet","Return from "+tienchira.getText().toString()+" -> "+mMoney);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnChira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs logs = createLogs();
                if (logs != null) {
                    DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
                    databaseHandler.addLogs(logs);
                    int ID_wallet = logs.getLog_id();
                    double money = (-1) * logs.getLog_money();
                    databaseHandler.update_CurrentMoneyWallet_byID(ID_wallet, money);
                    dismiss();
                    loadFragment(new Taikhoan());
                    Toast.makeText(getActivity(), "Xem kết quả ở Các giao dịch", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Logs createLogs() {

        Date date = new Date();
        String strDateFormat = "yyyy/MM/dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();

        String datetime = sdf.format(date);
        int id = databaseHandler.getAllWallet().get(pos).getID();//databaseHandler.getID_Wallet(name_wallet);
        int action = 2;
        String des = name_description;

        long timestamp = 0;
        String note = ghichu.getText().toString();


        Logs logs = new Logs(datetime, id, action, des, mMoney, mUnit, timestamp, note);
        return logs;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
