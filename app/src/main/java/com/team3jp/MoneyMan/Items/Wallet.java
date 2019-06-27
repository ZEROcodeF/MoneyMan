package com.team3jp.MoneyMan.Items;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {

    //int Avatar;
    int w_ID;
    String w_name;
    int w_type;
    double w_starmoney;
    double w_currentmoney;
    String w_unit;
    String w_startdate;
    String w_enddate;

    public Wallet(int wID, String name, int type, double startmoney, double curentmoney, String unit,
                  String startdate, String enddate) {
        this.w_ID = wID;
        this.w_name = name;
        this.w_type = type;
        this.w_starmoney = startmoney;
        this.w_currentmoney = curentmoney;
        this.w_unit = unit;
        this.w_startdate = startdate;
        this.w_enddate = enddate;
    }

    public Wallet() {
        this.w_ID = -1;
        this.w_starmoney = 0;
        this.w_type = -1;
    }

    public Wallet(int wID, String name, double startmoney, String unit) {
        this.w_ID = wID;
        this.w_name = name;
        this.w_starmoney = startmoney;
        this.w_unit = unit;
    }

    public Wallet(String name, double money, String date) {
        this.w_name = name;
        this.w_starmoney = money;
        this.w_startdate = date;
    }

    protected Wallet(Parcel in) {
        w_ID = in.readInt();
        w_name = in.readString();
        w_type = in.readInt();
        w_starmoney = in.readDouble();
        w_currentmoney = in.readDouble();
        w_unit = in.readString();
        w_startdate = in.readString();
        w_enddate = in.readString();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    //    public int getAvatar(){
//        return Avatar;
//    }
    public void setID(int id) {
        this.w_ID = id;
    }

    public int getID() {
        return this.w_ID;
    }

    public String getName() {
        return this.w_name;
    }

    public void setName(String name) {
        this.w_name = name;
    }

    public int getType() {
        return this.w_type;
    }

    public void setType(int type) {
        this.w_type = type;
    }

    public String getStardate() {
        return this.w_startdate;
    }

    public void setStardate(String date) {
        this.w_startdate = date;
    }

    public String getEnddate() {
        return this.w_enddate;
    }

    public void setEnddate(String date) {
        this.w_enddate = date;
    }

    public double getStarmoney() {
        return this.w_starmoney;
    }

    public void setStarmoney(double money) {
        this.w_starmoney = money;
    }

    public double getCurrentmoney() {
        return this.w_currentmoney;
    }

    public void setCurrentmoney(double money) {
        this.w_currentmoney = money;
    }

    public String getUnit() {
        return this.w_unit;
    }

    public void setUnit(String unit) {
        this.w_unit = unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromparcel(Parcel inParcel) {
        w_ID = inParcel.readInt();
        w_name = inParcel.readString();
        w_type = inParcel.readInt();
        w_starmoney = inParcel.readDouble();
        w_currentmoney = inParcel.readDouble();
        w_unit = inParcel.readString();
        w_startdate = inParcel.readString();
        w_enddate = inParcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(w_ID);
        dest.writeString(w_name);
        dest.writeInt(w_type);
        dest.writeDouble(w_starmoney);
        dest.writeDouble(w_currentmoney);
        dest.writeString(w_unit);
        dest.writeString(w_startdate);
        dest.writeString(w_enddate);
    }


}
