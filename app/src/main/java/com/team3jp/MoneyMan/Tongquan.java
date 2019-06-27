package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Tongquan extends Fragment {
    FloatingActionButton fab, fab_thu, fab_chi;
    boolean check = false;      // chưa bật nút +
    Tongquan_1 tongquan_1;
    Tongquan_2 tongquan_2;
    Tongquan_3 tongquan_3;
    FragmentTransaction ft;

    LinearLayout isNothingBackground;

    public static Tongquan newInstance() {
        Tongquan fragment = new Tongquan();
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
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.tongquan_activity, null);

        isNothingBackground = (LinearLayout)relativeLayout.findViewById(R.id.isNothingBackground);
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();

        if(databaseHandler.getAllLogs().isEmpty())
        {
            isNothingBackground.setVisibility(View.VISIBLE);
        }
        else {

            isNothingBackground.setVisibility(View.GONE);

            ft = getFragmentManager().beginTransaction();

            tongquan_1 = Tongquan_1.newInstance();
            ft.replace(R.id.frame1, tongquan_1);
            ft.commit();

            ft = getFragmentManager().beginTransaction();
            tongquan_2 = Tongquan_2.newInstance();
            ft.replace(R.id.frame2, tongquan_2);
            ft.commit();

            ft = getFragmentManager().beginTransaction();
            tongquan_3 = Tongquan_3.newInstance();
            ft.replace(R.id.frame3, tongquan_3);
            ft.commit();
        }

        fab = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan);
        fab_thu = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan_thu);
        fab_chi = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan_chi);
        Hide_Fab();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == false) {
                    Toast.makeText(getActivity(), "Thêm các khoản Thu nhập và Chi phí trong ví",
                            Toast.LENGTH_SHORT).show();
                    Show_Fab();
                    check = true;
                } else {
                    check = false;
                    Hide_Fab();
                }
            }
        });

        fab_thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Thunhap();

            }
        });

        fab_chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_Chitieu();

            }
        });

        return relativeLayout;
    }

    private void Show_Fab() {
        fab_thu.show();
        fab_chi.show();
    }

    private void Hide_Fab() {
        fab_thu.hide();
        fab_chi.hide();
    }

    private void showDialog_Thunhap() {
        FragmentManager fm = getFragmentManager();
        Tongquan_Fab_Thunhap tongquan_fab_thunhap = new Tongquan_Fab_Thunhap();
        tongquan_fab_thunhap.show(fm, "");
    }

    private void showDialog_Chitieu() {
        FragmentManager fm = getFragmentManager();
        Tongquan_Fab_Chitieu tongquan_fab_chitieu = new Tongquan_Fab_Chitieu();
        tongquan_fab_chitieu.show(fm, "");
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}




//package com.team3jp.MoneyMan;
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//
//public class Tongquan extends Fragment {
//    FloatingActionButton fab, fab_thu, fab_chi;
//    boolean check = false;      // chưa bật nút +
//    Tongquan_1 tongquan_1;
//    Tongquan_2 tongquan_2;
//    Tongquan_3 tongquan_3;
//    FragmentTransaction ft;
//
//    public static Tongquan newInstance() {
//        Tongquan fragment = new Tongquan();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.tongquan_activity, null);
//        ft = getFragmentManager().beginTransaction();
//
//        tongquan_1 = Tongquan_1.newInstance();
//        ft.replace(R.id.frame1, tongquan_1);
//        ft.commit();
//
//        ft = getFragmentManager().beginTransaction();
//        tongquan_2 = Tongquan_2.newInstance();
//        ft.replace(R.id.frame2, tongquan_2);
//        ft.commit();
//
//        ft = getFragmentManager().beginTransaction();
//        tongquan_3 = Tongquan_3.newInstance();
//        ft.replace(R.id.frame3, tongquan_3);
//        ft.commit();
//
//        fab = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan);
//        fab_thu = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan_thu);
//        fab_chi = (FloatingActionButton) relativeLayout.findViewById(R.id.fab_tongquan_chi);
//        Hide_Fab();
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (check == false) {
//                    Toast.makeText(getActivity(), "Thêm các khoản Thu nhập và Chi phí trong ví",
//                            Toast.LENGTH_SHORT).show();
//                    Show_Fab();
//                    check = true;
//                } else {
//                    check = false;
//                    Hide_Fab();
//                }
//            }
//        });
//
//        fab_thu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog_Thunhap();
//
//            }
//        });
//
//        fab_chi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog_Chitieu();
//
//            }
//        });
//
//        return relativeLayout;
//    }
//
//    private void Show_Fab() {
//        fab_thu.show();
//        fab_chi.show();
//    }
//
//    private void Hide_Fab() {
//        fab_thu.hide();
//        fab_chi.hide();
//    }
//
//    private void showDialog_Thunhap() {
//        FragmentManager fm = getFragmentManager();
//        Tongquan_Fab_Thunhap tongquan_fab_thunhap = new Tongquan_Fab_Thunhap();
//        tongquan_fab_thunhap.show(fm, "");
//    }
//
//    private void showDialog_Chitieu() {
//        FragmentManager fm = getFragmentManager();
//        Tongquan_Fab_Chitieu tongquan_fab_chitieu = new Tongquan_Fab_Chitieu();
//        tongquan_fab_chitieu.show(fm, "");
//    }
//
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//}