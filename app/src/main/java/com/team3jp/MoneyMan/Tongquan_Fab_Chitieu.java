package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.Wallet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.team3jp.MoneyMan.Taikhoan_Taotaikhoanmoi.checkNumber;

public class Tongquan_Fab_Chitieu extends DialogFragment {

    EditText tienthuvao, ghichu;
    Button chonvi, boqua, themvao;

    TextView ngaychi, hangmuchi;
    RadioGroup loaichi, laplai;
    Spinner spinner_chonhangmuc, spinner_chonvichitieu;


    ArrayList<String> list_vi = new ArrayList<String>();


    String[] list_hangmuc;

    RadioButton chithuong, chovay, lap, khonglap;

    String name_wallet, name_description;
    int pos;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tongquan_fab_chitieu, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tienthuvao = (EditText) view.findViewById(R.id.edt_fab_Tienchitieu);
        ghichu = (EditText) view.findViewById(R.id.edt_fab_Ghichuchi);

//        chonvi = (Button)view.findViewById(R.id.btnChonvichitieu);
        boqua = (Button) view.findViewById(R.id.btnBoquachi);
        themvao = (Button) view.findViewById(R.id.btnThemvaochi);

        loaichi = (RadioGroup) view.findViewById(R.id.Loaichi);
        laplai = (RadioGroup) view.findViewById(R.id.rg_fab_chi_laplai);

        chithuong = (RadioButton) view.findViewById(R.id.Chithuong);
        chovay = (RadioButton) view.findViewById(R.id.Chovay);
        lap = (RadioButton) view.findViewById(R.id.rb_fab_chi_Colap);
        khonglap = (RadioButton) view.findViewById(R.id.rb_fab_chi_Khonglap);


        ngaychi = (TextView) view.findViewById(R.id.tvNgaychi);
        Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        ngaychi.setText(sdf.format(date));


        // chọn ví
        spinner_chonvichitieu = (Spinner) view.findViewById(R.id.spnChonvichitieu);
        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Wallet> walletList = databaseHandler.getAllWallet();
        for (int i = 0; i < walletList.size(); i++) {
            String name = walletList.get(i).getName();
            list_vi.add(name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                list_vi);

        spinner_chonvichitieu.setAdapter(adapter);

        spinner_chonvichitieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //name_wallet = list_vi.get(position);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // chọn hạng mục chi
        hangmuchi = (TextView) view.findViewById(R.id.tvHangmucchi);

        spinner_chonhangmuc = (Spinner) view.findViewById(R.id.spnHangmucchi);
        list_hangmuc = getActivity().getResources().getStringArray(R.array.list_Hangmucchi);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                list_hangmuc);

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

        themvao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chithuong.isChecked() == false && chovay.isChecked() == false) {
                    //Toast.makeText(getActivity(),"Bạn phải chọn Chi thường hoặc Cho vay",Toast.LENGTH_SHORT).show();
                } else {
                    String moneys;
                    moneys = tienthuvao.getText().toString();
                    Boolean check = checkNumber(moneys);
                    if (!check) {
                        Toast.makeText(getActivity(), "Tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                    } else {
                        Logs logs = createLogs();
                        if (logs != null) {
                            databaseHandler.addLogs(logs);
                            int ID_wallet = logs.getLog_id();
                            double money = (-1) * logs.getLog_money();
                            databaseHandler.update_CurrentMoneyWallet_byID(ID_wallet, money);
                        }
                        dismiss();
                        loadFragment(new Tongquan());
                        Toast.makeText(getActivity(), "Xem kết quả ở Các giao dịch", Toast.LENGTH_SHORT).show();
                    }
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
        int action;
        String des;
        if (chithuong.isChecked() == true) {
            action = 2;     // chi thường
            des = name_description;
        } else {
            action = 4;     // cho vay
            des = "Cho vay";
        }
        String money = "0";
        if (!tienthuvao.getText().toString().equals("")) money = tienthuvao.getText().toString();
        double cmoney = Double.parseDouble(money);

        String unit = null;
        unit = databaseHandler.getWallet(id).getUnit();
        if (unit == null) unit = "";

        long timestamp = 0;
        String note = ghichu.getText().toString();


        Logs logs = new Logs(datetime, id, action, des, cmoney, unit, timestamp, note);
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

