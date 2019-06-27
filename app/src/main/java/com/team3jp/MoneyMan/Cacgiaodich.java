package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Cacgiaodich extends Fragment {

    public static Cacgiaodich newInstance() {
        Cacgiaodich fragment = new Cacgiaodich();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        showDialog_Cacgiaodich();
        return inflater.inflate(R.layout.cacgiaodich_activity, container, false);
    }

    private void showDialog_Cacgiaodich() {
        FragmentManager fm = getFragmentManager();
        Cacgiaodich_HienthiDialogcacgiaodich cacgiaodich_hienthiDialogcacgiaodich = new Cacgiaodich_HienthiDialogcacgiaodich();
        cacgiaodich_hienthiDialogcacgiaodich.show(fm, "");
    }


}