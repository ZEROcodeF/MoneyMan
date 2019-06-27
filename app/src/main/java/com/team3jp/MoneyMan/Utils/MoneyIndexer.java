package com.team3jp.MoneyMan.Utils;


import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.team3jp.MoneyMan.SettingsActivity;

public class MoneyIndexer {
    final String TAG = "MoneyIndexer";
    double total, mAUD, mEUR, mGBP, mJPY, mUSD, mVND;
    String defaultUnit;

    public MoneyIndexer(Context context) {
        this.total = 0;
        this.mAUD = 0;
        this.mEUR = 0;
        this.mGBP = 0;
        this.mJPY = 0;
        this.mUSD = 0;
        this.mVND = 0;
        defaultUnit = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsActivity.KEY_PREF_UNIT, "");
        Log.d(TAG, "Default unit is: " + defaultUnit);
    }


    public void copy(MoneyIndexer moneyIndexer) {
        this.total = moneyIndexer.getTotal();
        this.mAUD = moneyIndexer.getAUD();
        this.mEUR = moneyIndexer.getEUR();
        this.mGBP = moneyIndexer.getGBP();
        this.mJPY = moneyIndexer.getJPY();
        this.mUSD = moneyIndexer.getUSD();
        this.mVND = moneyIndexer.getVND();
    }

    public void addFromMoneyIndexer(MoneyIndexer moneyIndexer)
    {
        this.total += moneyIndexer.getTotal();
        this.mAUD += moneyIndexer.getAUD();
        this.mEUR += moneyIndexer.getEUR();
        this.mGBP += moneyIndexer.getGBP();
        this.mJPY += moneyIndexer.getJPY();
        this.mUSD += moneyIndexer.getUSD();
        this.mVND += moneyIndexer.getVND();
    }

    public void subtractFromMoneyIndexer(MoneyIndexer moneyIndexer)
    {
        this.total -= moneyIndexer.getTotal();
        this.mAUD -= moneyIndexer.getAUD();
        this.mEUR -= moneyIndexer.getEUR();
        this.mGBP -= moneyIndexer.getGBP();
        this.mJPY -= moneyIndexer.getJPY();
        this.mUSD -= moneyIndexer.getUSD();
        this.mVND -= moneyIndexer.getVND();
    }

    public void addMoneyWithLatestRates(double amount, String unit) {
        CurrencyUltils currencyUltils = new CurrencyUltils();
        if (!unit.equals(this.getDefaultUnit())) {
            Log.d(TAG, "Default unit is: " + defaultUnit + "Current unit is: " + unit);
            this.total += currencyUltils.convertWithLatestStoredRates(amount, unit, defaultUnit);

        } else {
            total += amount;
        }
        switch (unit) {
            case "AUD":
                this.mAUD += amount;
                break;
            case "EUR":
                this.mEUR += amount;
                break;
            case "GBP":
                this.mGBP += amount;
                break;
            case "JPY":
                this.mJPY += amount;
                break;
            case "USD":
                this.mUSD += amount;
                break;
            case "VND":
                this.mVND += amount;
                break;
        }
    }

    public void addMoneyWithTimestamp(double amount, String unit, long timestamp) {
        CurrencyUltils currencyUltils = new CurrencyUltils();
        if (!unit.equals(this.getDefaultUnit())) {
            Log.d(TAG, "Default unit is: " + defaultUnit + "Current unit is: " + unit);
            this.total += currencyUltils.convertWithNearesTimestamp(amount, unit, defaultUnit, timestamp);
        } else {
            total += amount;
        }
        switch (unit) {
            case "AUD":
                this.mAUD += amount;
                break;
            case "EUR":
                this.mEUR += amount;
                break;
            case "GBP":
                this.mGBP += amount;
                break;
            case "JPY":
                this.mJPY += amount;
                break;
            case "USD":
                this.mUSD += amount;
                break;
            case "VND":
                this.mVND += amount;
                break;
        }
    }

    //Return number of
    public int numberOthersUnit() {
        int num = 0;
        if (this.mAUD > 0 && !getDefaultUnit().equals("AUD")) {
            num++;
        }
        if (this.mEUR > 0 && !getDefaultUnit().equals("EUR")) {
            num++;
        }
        if (this.mGBP > 0 && !getDefaultUnit().equals("GBP")) {
            num++;
        }
        if (this.mJPY > 0 && !getDefaultUnit().equals("JPY")) {
            num++;
        }
        if (this.mUSD > 0 && !getDefaultUnit().equals("USD")) {
            num++;
        }
        if (this.mVND > 0 && !getDefaultUnit().equals("VND")) {
            num++;
        }
        return num;
    }

    public void clear() {
        this.total = 0;
        this.mAUD = 0;
        this.mEUR = 0;
        this.mGBP = 0;
        this.mJPY = 0;
        this.mUSD = 0;
        this.mVND = 0;
    }

    public double getTotal() {
        return total;
    }

    public double getAUD() {
        return mAUD;
    }

    public double getEUR() {
        return mEUR;
    }

    public double getGBP() {
        return mGBP;
    }

    public double getJPY() {
        return mJPY;
    }

    public double getUSD() {
        return mUSD;
    }

    public double getVND() {
        return mVND;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }
}
