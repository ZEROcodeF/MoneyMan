package com.team3jp.MoneyMan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team3jp.MoneyMan.Items.ObjectThuchi;

import java.util.ArrayList;
import java.util.List;

public class AdapterObjectThuchi extends ArrayAdapter<ObjectThuchi> {
    Context mcontext;
    ArrayList<ObjectThuchi> arrayList = new ArrayList<ObjectThuchi>();


    public AdapterObjectThuchi(Context context, int resource, List<ObjectThuchi> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.arrayList = new ArrayList<ObjectThuchi>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        AdapterObjectThuchi.ViewHolder viewHolder;

        if (rowView == null) {

            LayoutInflater inflate = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflate.inflate(R.layout.custom_row_cacgiaodich, null);

            viewHolder = new AdapterObjectThuchi.ViewHolder();
            viewHolder.avatar = (ImageView) rowView.findViewById(R.id.icon);
            viewHolder.hoatdong = (TextView) rowView.findViewById(R.id.muc);
            viewHolder.tenvi = (TextView) rowView.findViewById(R.id.tenvi);
            viewHolder.sotien = (TextView) rowView.findViewById(R.id.tien);
            viewHolder.ngaytao = (TextView) rowView.findViewById(R.id.ngay);

            rowView.setTag(viewHolder);
        } else {

            viewHolder = (AdapterObjectThuchi.ViewHolder) convertView.getTag();
        }

        ObjectThuchi objectThuchi = arrayList.get(position);
        viewHolder.avatar.setImageResource(objectThuchi.getAvatar());
        viewHolder.hoatdong.setText(objectThuchi.getHoatdong());
        viewHolder.tenvi.setText(objectThuchi.getTenvi());
        viewHolder.sotien.setText(objectThuchi.getTien());
        viewHolder.ngaytao.setText(objectThuchi.getNgay());


        return rowView;
    }

    static class ViewHolder {
        ImageView avatar;
        TextView hoatdong;
        TextView tenvi;
        TextView sotien;
        TextView ngaytao;

    }
}
