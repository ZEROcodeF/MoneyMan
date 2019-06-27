package com.team3jp.MoneyMan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.team3jp.MoneyMan.Items.Wallet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Bieudo_Ngaychinhxac extends Fragment {


    Spinner spinner;


    ArrayList<String> list_vi = new ArrayList<String>();

    EditText edtNgaybatdau, edtNgayketthuc;

    Button btnNgaybatdau, btnNgayketthuc, btnHienbieudo;

    MainActivity main;
    Context context = null;

    //String name_wallet;
    int pos;

    public static Bieudo_Ngaychinhxac newInstance() {
        Bieudo_Ngaychinhxac fragment = new Bieudo_Ngaychinhxac();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            context = getActivity(); // use this reference to invoke main callbacks
            main = (MainActivity) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.bieudo_ngaychinhxac, null);

        edtNgaybatdau = (EditText) linearLayout.findViewById(R.id.edtNgaybatdau);
        edtNgayketthuc = (EditText) linearLayout.findViewById(R.id.edtNgayketthuc);
        btnNgaybatdau = (Button) linearLayout.findViewById(R.id.btnNgaybatdau);
        btnNgayketthuc = (Button) linearLayout.findViewById(R.id.btnNgayketthuc);
        btnHienbieudo = (Button) linearLayout.findViewById(R.id.btnHienbando);


        spinner = (Spinner) linearLayout.findViewById(R.id.spnTaikhoan);
        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        List<Wallet> walletList = databaseHandler.getAllWallet();

        for (int i = 0; i < walletList.size(); i++) {
            String name = walletList.get(i).getName();
            list_vi.add(name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                list_vi);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                name_wallet = list_vi.get(position);
                    pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnNgaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_ngaybatdau();
            }
        });
        btnNgayketthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_ngayketthuc();
            }
        });

        btnHienbieudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtNgaybatdau.getText().toString().equals("") &&
                        !edtNgayketthuc.getText().toString().equals("")) {
                    String startdate = convertDate(edtNgaybatdau.getText().toString());
                    String enddate = convertDate(edtNgayketthuc.getText().toString());
                    if (startdate.compareTo(enddate) > 0) {
                        String tmp = startdate;
                        startdate = enddate;
                        enddate = tmp;
                    }
                    if (startdate.compareTo(enddate) <= 0) {

                        FragmentTransaction ft;
                        ft = getFragmentManager().beginTransaction();

                        //   Bieudo_Ngaychinhxac_Piechart bieudo_ngaychinhxac_piechart =Bieudo_Ngaychinhxac_Piechart.newInstance();
                        Bieudo_Ngaychinhxac_Piechart bieudo_ngaychinhxac_piechart = new Bieudo_Ngaychinhxac_Piechart();

                        Bundle bundle = new Bundle();

                        //bundle.putString("tenvi",name_wallet);
                        bundle.putInt("pos",pos);
                        bundle.putString("ngaybatdau", startdate);
                        bundle.putString("ngayketthuc", enddate);
                        bieudo_ngaychinhxac_piechart.setArguments(bundle);

                        ft.add(R.id.frame_bieudo_ngaychinhxac, bieudo_ngaychinhxac_piechart);
                        ft.commit();
                    }
                } else
                    Toast.makeText(getActivity(), R.string.no_empty_start_end_date, Toast.LENGTH_SHORT).show();

                //   loadFragment(new Bieudo_Ngaychinhxac_Piechart());
            }
        });

        return linearLayout;

    }

    private String convertDate(String date) {     // chuyển từ dd/MM/yyyy -> sang yyyy/MM/dd

        String day = date.substring(0, 2);
        String month = date.substring(3, 5);
        String year = date.substring(6, 10);

        return year + "/" + month + "/" + day;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnNgaybatdau:
//                select_ngaybatdau();
//                break;
//            case R.id.btnNgayketthuc:
//                select_ngayketthuc();
//                break;
//            case R.id.btnHienbando:
//                loadFragment(new Bieudo_Ngaychinhxac_Piechart());
//
//                break;
//        }
//    }


    private void select_ngaybatdau() {
        final Calendar calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtNgaybatdau.setText((dayOfMonth < 10 ? "0" : "") + dayOfMonth + "/" + (month < 10 ? "0" : "") + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }


    private void select_ngayketthuc() {
        final Calendar calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtNgayketthuc.setText((dayOfMonth < 10 ? "0" : "") + dayOfMonth + "/" + (month < 10 ? "0" : "") + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_bieudo, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
