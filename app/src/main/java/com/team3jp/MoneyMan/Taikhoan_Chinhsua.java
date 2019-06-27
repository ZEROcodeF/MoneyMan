package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.Items.Wallet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Taikhoan_Chinhsua extends DialogFragment {

    EditText edtTenvi, edtTienchinhsua, edtGhichu;
    Button btnHuy, btnLuu;
    TextView tvNgay, tvDonvi;

    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taikhoan_chinhsua, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTenvi = (EditText) view.findViewById(R.id.edtChinhsuaTenvi);
        edtTienchinhsua = (EditText) view.findViewById(R.id.edtTienhientai);
        edtGhichu = (EditText) view.findViewById(R.id.edtGhichusua);

        tvNgay = (TextView) view.findViewById(R.id.tvNgaychinhsua);
        tvDonvi = (TextView) view.findViewById(R.id.tvDonvitiente);

        btnHuy = (Button) view.findViewById(R.id.btnHuysua);
        btnLuu = (Button) view.findViewById(R.id.btnLuusua);

        final Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        tvNgay.setText(sdf.format(date));

        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        Bundle bundle = getArguments();
        pos = bundle.getInt("pos");
        //String name_wallet = bundle.getString("tenvi","");

        //final int ID_wallet = databaseHandler.getID_Wallet(name_wallet);

        final Wallet wallet = databaseHandler.getAllWallet().get(pos);
        final int ID_wallet = wallet.getID();
        String name_wallet = wallet.getName();

        edtTenvi.setText(name_wallet);
        tvDonvi.setText(wallet.getUnit());
        edtTienchinhsua.setText("" + wallet.getCurrentmoney());


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                loadFragment(new Taikhoan());
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Wallet wallet1 = new Wallet(wallet.getID(), edtTenvi.getText().toString(), wallet.getType(),
                        wallet.getStarmoney(), Double.parseDouble(edtTienchinhsua.getText().toString()),
                        wallet.getUnit(), wallet.getStardate(), wallet.getEnddate());
                databaseHandler.updateWallet(wallet1);

                Toast.makeText(getActivity(), "Đã chỉnh sửa!", Toast.LENGTH_SHORT).show();
                loadFragment(new Taikhoan());
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
