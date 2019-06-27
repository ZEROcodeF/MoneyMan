package com.team3jp.MoneyMan.Items;


import com.team3jp.MoneyMan.DatabaseHandler;
import com.team3jp.MoneyMan.MoneyManApplication;

public class Logs {

    String log_datetime;
    int log_id;
    int log_action;     // 1: thu nhập          2: chi phí      3: đi vay       4: cho vay
    String log_description;     // mục thu chi
    double log_money;
    String log_unit;
    long log_timestamp;
    String log_note;

    public Logs(String datetime, int id, int action, String des, double money, String unit, long timestamp, String note) {
        this.log_datetime = datetime;
        this.log_id = id;
        this.log_action = action;
        this.log_description = des;
        this.log_money = money;
        this.log_unit = unit;
        this.log_timestamp = timestamp;
        this.log_note = note;
    }

    public String getLog_datetime() {
        return log_datetime;
    }

    public int getLog_id() {
        return log_id;
    }

    public int getLog_action() {
        return log_action;
    }

    public String getLog_description() {
        return log_description;
    }

    public double getLog_money() {
        return log_money;
    }

    public String getLog_unit() {
        return log_unit;
    }

    public long getLog_timestamp() {
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        return log_timestamp==0?databaseHandler.getLastestTimestamp():log_timestamp;
    }

    public String getLog_note() {
        return log_note;
    }
}
