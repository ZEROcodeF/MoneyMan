package com.team3jp.MoneyMan.Utils;

import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.team3jp.MoneyMan.DatabaseHandler;
import com.team3jp.MoneyMan.MoneyManApplication;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUltils {
    final String TAG = "CurrencyUltils";
    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

    public double convertWithLatestStoredRates(double amount, String currentUnit, String targetUnit) {
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        Log.e(TAG, "Converting " + amount + " from " + currentUnit + " to " + targetUnit);
        return convertWithTimestamp(amount, currentUnit, targetUnit, databaseHandler.getLastestTimestamp());
    }

    public double convertWithNearesTimestamp(double amount, String currentUnit, String targetUnit, long timestamp) {
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        return convertWithTimestamp(amount, currentUnit, targetUnit, databaseHandler.getNearestTimestamp(timestamp));
    }

    public double convertWithTimestamp(double amount, String currentUnit, String targetUnit, long timestamp) {
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        Log.d(TAG, "Getting trate... ");
        double trate = databaseHandler.getRateWithTimestamp(timestamp, targetUnit);
        Log.d(TAG, "Trate is  " + trate);
        Log.d(TAG, "Getting crate... ");
        double crate = databaseHandler.getRateWithTimestamp(timestamp, currentUnit);
        Log.d(TAG, "Crate is  " + crate);
        return (trate > 0 && crate > 0) ? (amount * trate / crate) : amount;
    }

    public String formatCurrencyWithSymbol(double amount, String unit) {
        return formatCurrency(amount) + symbolOf(unit);
    }

    public String formatCurrency(double amount) {
        formatter.setMinimumFractionDigits(0);
        if (amount < 0) {
            return "-" + formatter.format(-amount).substring(1);
        }
        return formatter.format(amount).substring(1);
    }

    public String symbolOf(String unit) {
        switch (unit) {
            case "AUD":
                return "A";
            case "EUR":
                return "€";
            case "GBP":
                return "£";
            case "JPY":
                return "¥";
            case "USD":
                return "$";
            case "VND":
                return "₫";
            default:
                return "0";
        }
    }

    public double MoneyInputWithUnit(EditText edt, String mUnit, TextWatcher textWatcher, CharSequence s) {
        edt.removeTextChangedListener(textWatcher);
        String pStr = s.toString();
        pStr = pStr.replaceAll("[^\\d.]", "");
        Double money = (double) 0;
        if (!pStr.equals("")) {
            char lastCh = pStr.charAt(pStr.length() - 1);
            if (pStr.length()>1&& lastCh == '0' && pStr.charAt(pStr.length()-2)=='.') {
                Log.d("AA","Nothing todo");
            } else {
                if (lastCh != '.') {//Not Empty means Clean String
                    {
                        money = Double.parseDouble(pStr);
                        if (money == 0) {
                            pStr = "0";
                        } else {
                            pStr = this.formatCurrencyWithSymbol(money, mUnit);
                        }

                    }
                }
            }
            if (pStr.charAt(0) == '.') {
                pStr = "0.";
            }
            edt.setText(pStr);
            if (pStr.charAt(pStr.length() - 1) != '.' && pStr.charAt(pStr.length() - 1) != '0') {
                edt.setSelection(edt.getText().toString().length() - 1);
            } else
                edt.setSelection(edt.getText().toString().length());
        } else {
            edt.setText("0");
            edt.setSelection(1); //this means length("0") == 1
        }
        edt.addTextChangedListener(textWatcher);
        return money;
    }
}
