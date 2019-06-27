package com.team3jp.MoneyMan;

import android.content.Context;
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
import android.widget.ListView;


public class Cacgiaodich_HienthiDialogcacgiaodich extends DialogFragment {

    MainActivity main;
    Context context = null;

    String[] items;

    ListView mylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cacgiaodich_dialog_luachon, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mylist = (ListView) view.findViewById(R.id.list_dialog_giaodich);

        items = getActivity().getResources().getStringArray(R.array.list_giaodich_cacluachon);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                items);
        mylist.setAdapter(adapter);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        dismiss();
                        fragment = new Cacgiaodich_Thuvao();
                        loadFragment(fragment);
                        break;
                    case 1:
                        dismiss();
                        fragment = new Cacgiaodich_Chira();
                        loadFragment(fragment);
                        break;
                    case 2:
                        dismiss();
                        fragment = new Cacgiaodich_Chovay();
                        loadFragment(fragment);
                        break;
                    case 3:
                        dismiss();
                        fragment = new Cacgiaodich_Vay();
                        loadFragment(fragment);
                        break;
                    case 4:
                        dismiss();
                        fragment = new Cacgiaodich_Cacgiaodichganday();
                        loadFragment(fragment);


                }
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
