package com.team3jp.MoneyMan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.team3jp.MoneyMan.Items.Wallet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cacgiaodich_Loc extends DialogFragment {

    Spinner spinner;
    EditText edtNgaybatdau, edtNgayketthuc;
    Button btnHuy, btnLoc, btnNgaybatdau, btnNgayketthuc;

    ArrayList<String> list_vi = new ArrayList<String>();

    String name, sdate, edate, fragmentname = "";
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cacgiaodich_loc, container);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        fragmentname = bundle.getString("ten_fragment");


        edtNgaybatdau = (EditText) view.findViewById(R.id.edtLoc_Ngaybatdau);
        edtNgayketthuc = (EditText) view.findViewById(R.id.edtLoc_Ngayketthuc);

        btnHuy = (Button) view.findViewById(R.id.btnLoc_Huy);
        btnLoc = (Button) view.findViewById(R.id.btnLoc_Loc);

        btnNgaybatdau = (Button) view.findViewById(R.id.btnLoc_Ngaybatdau);
        btnNgayketthuc = (Button) view.findViewById(R.id.btnLoc_Ngayketthuc);

        spinner = (Spinner) view.findViewById(R.id.spnLoc_Chonvi);

        list_vi.add("Tất cả");

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

//                name = list_vi.get(position);
//                if (position == 0) name = "Tatca";
                if(position==0)pos = list_vi.size();
                else pos = position - 1;
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

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNgaybatdau.getText().toString().equals("") == false &&
                        edtNgayketthuc.getText().toString().equals("") == false) {
                    sdate = convertDate(edtNgaybatdau.getText().toString());
                    edate = convertDate(edtNgayketthuc.getText().toString());
                    if (sdate.compareTo(edate) > 0) {
                        String tmp = sdate;
                        sdate = edate;
                        edate = tmp;
                    }
                    if (sdate.compareTo(edate) <= 0) {
                        if (fragmentname.equals("thuvao") == true) {
                            Cacgiaodich_Thuvao cacgiaodich_thuvao = new Cacgiaodich_Thuvao();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            Bundle bundle = new Bundle();
                            //bundle.putString("tenvi", name);
                            bundle.putInt("pos",pos);
                            bundle.putString("ngaybatdau", sdate);
                            bundle.putString("ngayketthuc", edate);
                            cacgiaodich_thuvao.setArguments(bundle);

                            ft.replace(R.id.frame_container, cacgiaodich_thuvao);
                            ft.commit();
                            dismiss();
                        } else if (fragmentname.equals("chira") == true) {
                            Cacgiaodich_Chira cacgiaodich_chira = new Cacgiaodich_Chira();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            Bundle bundle = new Bundle();
                            //bundle.putString("tenvi", name);
                            bundle.putInt("pos",pos);
                            bundle.putString("ngaybatdau", sdate);
                            bundle.putString("ngayketthuc", edate);
                            cacgiaodich_chira.setArguments(bundle);

                            ft.replace(R.id.frame_container, cacgiaodich_chira);
                            ft.commit();
                            dismiss();
                        } else if (fragmentname.equals("vay") == true) {
                            Cacgiaodich_Vay cacgiaodich_vay = new Cacgiaodich_Vay();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            Bundle bundle = new Bundle();
                            //bundle.putString("tenvi", name);
                            bundle.putInt("pos",pos);
                            bundle.putString("ngaybatdau", sdate);
                            bundle.putString("ngayketthuc", edate);
                            cacgiaodich_vay.setArguments(bundle);

                            ft.replace(R.id.frame_container, cacgiaodich_vay);
                            ft.commit();
                            dismiss();
                        } else if (fragmentname.equals("chovay") == true) {
                            Cacgiaodich_Chovay cacgiaodich_chovay = new Cacgiaodich_Chovay();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            Bundle bundle = new Bundle();
                            //bundle.putString("tenvi", name);
                            bundle.putInt("pos",pos);
                            bundle.putString("ngaybatdau", sdate);
                            bundle.putString("ngayketthuc", edate);
                            cacgiaodich_chovay.setArguments(bundle);

                            ft.replace(R.id.frame_container, cacgiaodich_chovay);
                            ft.commit();
                            dismiss();
                        }
                    }
                } else
                    Toast.makeText(getActivity(), "Ngày bắt đầu và ngày kết thúc không được trống!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String convertDate(String date) {     // chuyển từ dd/MM/yyyy -> sang yyyy/MM/dd

        String day = date.substring(0, 2);
        String month = date.substring(3, 5);
        String year = date.substring(6, 10);

        return year + "/" + month + "/" + day;
    }

    private void select_ngaybatdau() {
        final Calendar calendar = Calendar.getInstance();
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mMonth = calendar.get(Calendar.MONTH);
        int mYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edtNgayketthuc.setText((dayOfMonth < 10 ? "0" : "") + dayOfMonth + "/" + (month < 10 ? "0" : "") + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
