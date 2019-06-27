package com.team3jp.MoneyMan.Utils;

import java.text.SimpleDateFormat;

public class TimestampUltils {

    public String getDate(long timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
    }

    public String getDateTime(long timestamp) {
        return new SimpleDateFormat("kk:mm:ss dd/MM/yyyy").format(timestamp);
    }
}
