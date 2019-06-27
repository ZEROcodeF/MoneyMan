package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Bieudo_Thang extends Fragment {

    Bieudo_Thang_PieChart_1 bd_1;
    Bieudo_Thang_PieChart_2 bd_2;
    FragmentTransaction ft;

    public static Bieudo_Thang newInstance() {
        Bieudo_Thang fragment = new Bieudo_Thang();
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

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.bieudo_thang, null);

        ft = getFragmentManager().beginTransaction();
        bd_1 = Bieudo_Thang_PieChart_1.newInstance();
        ft.replace(R.id.frame_bieudo_1, bd_1);
        ft.commit();

        ft = getFragmentManager().beginTransaction();
        bd_2 = Bieudo_Thang_PieChart_2.newInstance();
        ft.replace(R.id.frame_bieudo_2, bd_2);
        ft.commit();


        return linearLayout;
    }


}
