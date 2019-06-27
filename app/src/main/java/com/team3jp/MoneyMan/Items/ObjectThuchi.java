package com.team3jp.MoneyMan.Items;

public class ObjectThuchi {

    int Avatar;
    String Hoatdong;
    String Tenvi;
    String Tien;
    String Ngay;

    public ObjectThuchi(int avatar, String hd, String name, String tien, String ngay) {
        this.Avatar = avatar;
        this.Hoatdong = hd;
        this.Tenvi = name;
        this.Tien = tien;
        this.Ngay = ngay;
    }

    public int getAvatar() {
        return Avatar;
    }

    public String getHoatdong() {
        return Hoatdong;
    }

    public String getTenvi() {
        return Tenvi;
    }

    public String getNgay() {
        return Ngay;
    }

    public String getTien() {
        return Tien;
    }

    public void setAvatar(int avatar) {
        this.Avatar = avatar;
    }

    public void setHoatdong(String hoatdong) {
        this.Hoatdong = hoatdong;
    }

    public void setName(String name) {
        this.Tenvi = name;
    }

    public void setNgay(String ngay) {
        this.Ngay = ngay;
    }

    public void setTien(String tien) {
        this.Tien = tien;
    }
}
