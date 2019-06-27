package com.team3jp.MoneyMan;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.Fragments.FragmentDialogAddToWallet;
import com.team3jp.MoneyMan.Fragments.FragmentDialogPaidFromWallet;
import com.team3jp.MoneyMan.Items.ObjectTaikhoan;

import java.util.ArrayList;
import java.util.List;

public class AdapterObjectTaikhoan extends ArrayAdapter<ObjectTaikhoan> {
    Context mcontext;
    ArrayList<ObjectTaikhoan> arrayList = new ArrayList<ObjectTaikhoan>();
    FragmentManager fragmentManager;

    public AdapterObjectTaikhoan(Context context, int resource, List<ObjectTaikhoan> objects, FragmentManager fm) {
        super(context, resource, objects);
        this.mcontext = context;
        this.arrayList = new ArrayList<ObjectTaikhoan>(objects);
        this.fragmentManager = fm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        AdapterObjectTaikhoan.ViewHolder viewHolder;

        if (rowView == null) {

            LayoutInflater inflate = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflate.inflate(R.layout.custom_row_taikhoan, null);

            viewHolder = new AdapterObjectTaikhoan.ViewHolder();
            viewHolder.Avatar = (ImageView) rowView.findViewById(R.id.icon);
            viewHolder.Ten = (TextView) rowView.findViewById(R.id.ten);
            viewHolder.Tienhientai = (TextView) rowView.findViewById(R.id.tienhientai);
            viewHolder.Ngay = (TextView) rowView.findViewById(R.id.ngaytao);
            viewHolder.btnAdd = (Button)rowView.findViewById(R.id.btnAdd);
            viewHolder.btnSubtract = (Button)rowView.findViewById(R.id.btnSubtract);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (AdapterObjectTaikhoan.ViewHolder) convertView.getTag();
        }

        ObjectTaikhoan objectTaikhoan = arrayList.get(position);
        viewHolder.Avatar.setImageResource(objectTaikhoan.getAvatar());
        viewHolder.Ten.setText(objectTaikhoan.getTen());
        viewHolder.Tienhientai.setText(objectTaikhoan.getTienhientai());
        viewHolder.Ngay.setText(objectTaikhoan.getNgay());
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = FragmentDialogAddToWallet.newInstance(position);
                dialogFragment.show(fragmentManager, "dialogAddToWallet");
            }
        });
        viewHolder.btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = FragmentDialogPaidFromWallet.newInstance(position);
                dialogFragment.show(fragmentManager, "dialogAddToWallet");
            }
        });
        return rowView;
    }

    static class ViewHolder {
        ImageView Avatar;
        TextView Ten;
        TextView Tienhientai;
        TextView Ngay;
        Button btnAdd;
        Button btnSubtract;
    }
}