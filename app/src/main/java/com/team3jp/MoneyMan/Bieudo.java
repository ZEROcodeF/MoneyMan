package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class Bieudo extends Fragment {

    public static Bieudo newInstance() {
        Bieudo fragment = new Bieudo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.bieudo_activity, null);

        BottomNavigationView navigation = (BottomNavigationView) relativeLayout.findViewById(R.id.bieudo);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new Bieudo_Thang());

        return relativeLayout;

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.bieudo_thang:
                    fragment = new Bieudo_Thang();
                    loadFragment(fragment);
                    return true;

                case R.id.bieudo_ngaychinhxac:
                    fragment = new Bieudo_Ngaychinhxac();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_bieudo, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
