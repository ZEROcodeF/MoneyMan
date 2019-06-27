package com.team3jp.MoneyMan.Items;

import android.widget.Button;

public class ObjectTaikhoan {
    int Avatar;
    String Ten;
    String Tienhientai;
    String Ngay;
    Button btnAdd, btnSubtract;

    public ObjectTaikhoan(int avatar, String ten, String tienhientai, String ngay) {
        this.Avatar = avatar;
        this.Ten = ten;
        this.Tienhientai = tienhientai;
        this.Ngay = ngay;
    }

    public String getNgay() {
        return Ngay;
    }

    public int getAvatar() {
        return Avatar;
    }

    public String getTen() {
        return Ten;
    }

    public String getTienhientai() {
        return Tienhientai;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public void setAvatar(int avatar) {
        Avatar = avatar;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public void setTienhientai(String tienhientai) {
        Tienhientai = tienhientai;
    }
}
