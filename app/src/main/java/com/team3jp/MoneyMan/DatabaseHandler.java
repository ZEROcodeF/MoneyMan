package com.team3jp.MoneyMan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.team3jp.MoneyMan.Items.Logs;
import com.team3jp.MoneyMan.Items.Wallet;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHandler";
    public static final String DBNAME = "moneyman.sqlite";
    public static final int VERSION = 1;

    //enum for currency columns
    enum CurrencyCol {
        AUD, EUR, GBP, JPY, USD, VND;

        public int getValue() {
            return ordinal() + 1;
        }
    }

    //Table Col with Names:
    //Table WALLETS:
    public static final String WALLET_TABLE_NAME = "wallets";
    public static final String WALLET_COL_ID = "w_id"; //key
    public static final String WALLET_COL_NAME = "w_name";
    public static final String WALLET_COL_TYPE = "w_type";
    public static final String WALLET_COL_START_MONEY = "w_start_money";
    public static final String WALLET_COL_CURRENT_MONEY = "w_current_money";
    public static final String WALLET_COL_UNIT = "w_unit";
    public static final String WALLET_COL_START_DATE = "w_start_date";
    public static final String WALLET_COL_END_DATE = "w_end_date";

    //Table LOG:
    public static final String LOG_TABLE_NAME = "logs";
    public static final String LOG_COL_DATETIME = "l_datetime"; //key
    public static final String LOG_COL_WALLET_ID = "l_wallet_id"; //key & get from WALLET_COL_ID
    public static final String LOG_COL_ACTION = "l_action"; //
    public static final String LOG_COL_DESCRIPTION = "l_description";//"Tiền đi ăn" "tiền đi chơi"
    public static final String LOG_COL_MONEY = "l_money"; //amount money of action
    public static final String LOG_COL_UNIT = "l_unit"; //unit when action happened
    public static final String LOG_COL_TIMESTAMP = "l_timestamp"; //For history converter
    public static final String LOG_COL_NOTE = "l_note";

    //Table FREQUENCY: Khoản thu/chi thường xuyên
    public static final String FREQUENCY_TABLE_NAME = "frequency";
    public static final String FREQUENCY_COL_WALLET_ID = "f_wallet_id";
    public static final String FREQUENCY_COL_DATETIME = "f_datetime";
    public static final String FREQUENCY_COL_ACTION = "f_action"; //
    public static final String FREQUENCY_COL_DESCRIPTION = "f_description";//"Tiền đi ăn" "tiền đi chơi"
    public static final String FREQUENCY_COL_MONEY = "f_money"; //số tiền
    public static final String FREQUENCY_COL_UNIT = "f_unit"; //đơn vị
    public static final String FREQUENCY_COL_NOTE = "f_note";
    public static final String FREQUENCY_COL_TYPE = "f_type"; //kiểu lặp lại: hằng ngày, hằng tuần, tháng...

    //Table CURRENCY: the base is EUR
    public static final String CURRENCY_TABLE_NAME = "currencies";
    public static final String CURRENCY_COL_TIMESTAMP = "c_timestamp";
    public static final String CURRENCY_COL_VND = "c_vnd"; //dong
    public static final String CURRENCY_COL_EUR = "c_eur"; //euro
    public static final String CURRENCY_COL_USD = "c_usd"; //US dollar
    public static final String CURRENCY_COL_GBP = "c_gbp"; //pound
    public static final String CURRENCY_COL_JPY = "c_jpy"; //yen
    public static final String CURRENCY_COL_AUD = "c_aud"; //australia dollar

    //Keep only one instance of database throughout application for performace
    private SQLiteDatabase mDatabase = null;

    //Store current password used. Password expires after timeouts.
    private String mPassword = null;

    public DatabaseHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    //Set the password used to unlock database.
    public void setPassword(String password) {
        mPassword = password;
    }

    //get the password
    public String getPassword() {
        return mPassword;
    }

    public void update() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            if (getPassword() == null || getPassword().equals("")) {
                throw new IllegalArgumentException("Password null or not acceptable");
            }
            mDatabase = getWritableDatabase(getPassword());
        }
    }

    public void recycle() {
        close();
        mDatabase = null;
        mPassword = null;
    }

    @Override
    public synchronized void close() {
        super.close();
        if (mDatabase != null) {
            if (!mDatabase.isOpen())
                mDatabase.close();
            mDatabase = null;
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        update();
        return mDatabase;
    }

    public SQLiteDatabase getReadableDatabase() {
        return getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query;
        //WALLETS TABLE
        query = "CREATE TABLE " + WALLET_TABLE_NAME + " ( " +
                WALLET_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                WALLET_COL_NAME + " TEXT NOT NULL, " +
                WALLET_COL_TYPE + " INTEGER NOT NULL, " +

//                WALLET_COL_START_MONEY + " REAL NOT NULL DEFAULT '0', " +
//                WALLET_COL_CURRENT_MONEY + " REAL NOT NULL DEFAULT '0', " +
//                WALLET_COL_UNIT + " TEXT, " +
//                WALLET_COL_START_DATE + " INTEGER, " + //YYYY/MM/DD
//                WALLET_COL_END_DATE + " INTEGER " + //YYYY/MM/DD

                WALLET_COL_START_MONEY + " REAL NOT NULL DEFAULT '0', " +
                WALLET_COL_CURRENT_MONEY + " REAL NOT NULL DEFAULT '0', " +
                WALLET_COL_UNIT + " TEXT, " +
                WALLET_COL_START_DATE + " TEXT, " + //YYYY/MM/DD
                WALLET_COL_END_DATE + " TEXT " + //YYYY/MM/DD
                ") ";
        db.execSQL(query);

        //LOGS TABLE
        query = "CREATE TABLE " + LOG_TABLE_NAME + " ( " +
//                LOG_COL_DATETIME + " INTEGER NOT NULL, " + //YYYY/MM/DD hh:mm:ss
//                LOG_COL_WALLET_ID + " INTEGER NOT NULL, " +
//                LOG_COL_ACTION + " INTEGER, " + //1: thu vào          2: chi ra      3: đi vay       4: cho vay
//                LOG_COL_DESCRIPTION + " TEXT, " +
//                LOG_COL_MONEY + " REAL NOT NULL DEFAULT '0', " +
//                LOG_COL_UNIT + " TEXT, " +
//                LOG_COL_TIMESTAMP + " INTEGER, " +
//                LOG_COL_NOTE + " TEXT, " +

                LOG_COL_DATETIME + " TEXT NOT NULL, " + //YYYY/MM/DD hh:mm:ss
                LOG_COL_WALLET_ID + " INTEGER NOT NULL, " +
                LOG_COL_ACTION + " INTEGER, " + //1: thu vào          2: chi ra      3: đi vay       4: cho vay
                LOG_COL_DESCRIPTION + " TEXT, " +
                LOG_COL_MONEY + " REAL NOT NULL DEFAULT '0', " +
                LOG_COL_UNIT + " TEXT, " +
                LOG_COL_TIMESTAMP + " INTEGER, " +
                LOG_COL_NOTE + " TEXT, " +

                //Make FK and PK:
                String.format("FOREIGN KEY(%s) REFERENCES %s(%s), ", LOG_COL_WALLET_ID, WALLET_TABLE_NAME, WALLET_COL_ID) +
                String.format("PRIMARY KEY (%s, %s)", LOG_COL_DATETIME, LOG_COL_WALLET_ID) +
                ") ";
        db.execSQL(query);

        //frequency
        query = "CREATE TABLE " + FREQUENCY_TABLE_NAME + " ( " +
                FREQUENCY_COL_WALLET_ID + " INTEGER NOT NULL, " +
                FREQUENCY_COL_DATETIME + " TEXT NOT NULL, " + //YYYY/MM/DD hh:mm:ss
                FREQUENCY_COL_ACTION + " INTEGER, " + //1: thu vào          2: chi ra      3: đi vay       4: cho vay
                FREQUENCY_COL_DESCRIPTION + " TEXT, " +
                FREQUENCY_COL_MONEY + " TEXT NOT NULL DEFAULT '0', " +
                FREQUENCY_COL_UNIT + " TEXT, " +
                FREQUENCY_COL_NOTE + " TEXT, " +
                FREQUENCY_COL_TYPE + " INTEGER NOT NULL DEFAULT '0', " +
                //Make FK and PK:
                String.format("FOREIGN KEY(%s) REFERENCES %s(%s), ", FREQUENCY_COL_WALLET_ID, WALLET_TABLE_NAME, WALLET_COL_ID) +
                String.format("PRIMARY KEY (%s, %s)", FREQUENCY_COL_DATETIME, FREQUENCY_COL_WALLET_ID) +
                ") ";
        db.execSQL(query);

        //currency
        query = "CREATE TABLE " + CURRENCY_TABLE_NAME + " ( " +
                CURRENCY_COL_TIMESTAMP + " INTEGER PRIMARY KEY NOT NULL, " +
                CURRENCY_COL_AUD + " REAL, " +
                CURRENCY_COL_EUR + " REAL, " +
                CURRENCY_COL_GBP + " REAL, " +
                CURRENCY_COL_JPY + " REAL, " +
                CURRENCY_COL_USD + " REAL, " +
                CURRENCY_COL_VND + " REAL " +
                ") ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

////////////////////////------------------ BẢNG WALLET ------------------------------------------

    public void addWallet(Wallet wallet) {       // Thêm vào 1 ví mới
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WALLET_COL_ID, wallet.getID());
        values.put(WALLET_COL_NAME, wallet.getName());
        values.put(WALLET_COL_TYPE, wallet.getType());
        values.put(WALLET_COL_START_MONEY, wallet.getStarmoney());
        values.put(WALLET_COL_CURRENT_MONEY, wallet.getCurrentmoney());
        values.put(WALLET_COL_START_DATE, wallet.getStardate());
        values.put(WALLET_COL_END_DATE, wallet.getEnddate());
        values.put(WALLET_COL_UNIT, wallet.getUnit());

        db.insert(WALLET_TABLE_NAME, null, values);
        db.close();

    }

    public Wallet getWallet(int id_Wallet) {     // Trả về 1 ví theo id
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(WALLET_TABLE_NAME, null, WALLET_COL_ID + " = ?",
                new String[]{String.valueOf(id_Wallet)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Wallet wallet = new Wallet(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7));
        cursor.close();
        return wallet;
    }

    public List<Wallet> getAllWallet() {     // lấy ra toàn bộ database trong bang WALLET
        List<Wallet> walletList = new ArrayList<>();

        String query = "SELECT * FROM " + WALLET_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Wallet wallet = new Wallet(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7));

            walletList.add(wallet);
            cursor.moveToNext();
        }
        cursor.close();
        return walletList;
    }

//    public void delete_Wallet_byName(String name) {        // xóa ví theo tên
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(WALLET_TABLE_NAME, WALLET_COL_NAME + " = ?", new String[]{name});
//        db.close();
//    }

    public void deleteWallet(Wallet wallet) {       // xóa ví wallet
        SQLiteDatabase db = this.getWritableDatabase();
        //Xóa logs theo id ví
        db.delete(LOG_TABLE_NAME, LOG_COL_WALLET_ID + " = ?", new String[]{String.valueOf(wallet.getID())});
        // xóa ví theo id
        db.delete(WALLET_TABLE_NAME, WALLET_COL_ID + " = ?", new String[]{String.valueOf(wallet.getID())});
        db.close();
    }


    public void update_CurrentMoneyWallet_byID(int ID_wallet, double money) {          // update current money 1 ví theo id

        Wallet wallet = getWallet(ID_wallet);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WALLET_COL_ID, wallet.getID());
        values.put(WALLET_COL_NAME, wallet.getName());
        values.put(WALLET_COL_TYPE, wallet.getType());
        values.put(WALLET_COL_START_MONEY, wallet.getStarmoney());

//        String current_money = wallet.getCurrentmoney();
//        int m = Integer.parseInt(current_money) + money;
        double m = wallet.getCurrentmoney() + money;

        values.put(WALLET_COL_CURRENT_MONEY, m);
        values.put(WALLET_COL_START_DATE, wallet.getStardate());
        values.put(WALLET_COL_END_DATE, wallet.getEnddate());
        values.put(WALLET_COL_UNIT, wallet.getUnit());

        db.update(WALLET_TABLE_NAME, values, WALLET_COL_ID + " = ?", new String[]{String.valueOf(wallet.getID())});
        // sữa cột ID

        db.close();

    }


    public void updateWallet(Wallet wallet) {          //update 1 ví
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WALLET_COL_ID, wallet.getID());
        values.put(WALLET_COL_NAME, wallet.getName());
        values.put(WALLET_COL_TYPE, wallet.getType());
        values.put(WALLET_COL_START_MONEY, wallet.getStarmoney());
        values.put(WALLET_COL_CURRENT_MONEY, wallet.getCurrentmoney());
        values.put(WALLET_COL_START_DATE, wallet.getStardate());
        values.put(WALLET_COL_END_DATE, wallet.getEnddate());
        values.put(WALLET_COL_UNIT, wallet.getUnit());

        db.update(WALLET_TABLE_NAME, values, WALLET_COL_ID + " = ?", new String[]{String.valueOf(wallet.getID())});
        // sữa cột ID

        db.close();
    }


//    public int getID_Wallet(String name_wallet) {    // trả về id của 1 ví theo tên
//        int id = 0;
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(WALLET_TABLE_NAME, null, WALLET_COL_NAME + " = ?",
//                new String[]{name_wallet}, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Wallet wallet = new Wallet(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
//                cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5),
//                cursor.getString(6), cursor.getString(7));
//
//        id = wallet.getID();
//        cursor.close();
//        return id;
//    }

///////////////----------------------------------- BẢNG LOGS ---------------------------------

    // Xử lí cho bảng Logs

    public void addLogs(Logs logs) {       // Thêm vào 1 ví mới
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOG_COL_DATETIME, logs.getLog_datetime());
        values.put(LOG_COL_WALLET_ID, logs.getLog_id());
        values.put(LOG_COL_ACTION, logs.getLog_action());
        values.put(LOG_COL_DESCRIPTION, logs.getLog_description());
        values.put(LOG_COL_MONEY, logs.getLog_money());
        values.put(LOG_COL_UNIT, logs.getLog_unit());
        values.put(LOG_COL_TIMESTAMP, logs.getLog_timestamp());
        values.put(LOG_COL_NOTE, logs.getLog_note());
        db.insert(LOG_TABLE_NAME, null, values);
        db.close();
    }

    public List<Logs> getAllLogs() {     // lấy ra toàn bộ bảng Logs
        List<Logs> logsList = new ArrayList<>();

        String query = "SELECT * FROM " + LOG_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Logs logs = new Logs(cursor.getString(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getDouble(4), cursor.getString(5),
                    cursor.getInt(6), cursor.getString(7));

            logsList.add(logs);
            cursor.moveToNext();
        }
        cursor.close();
        return logsList;
    }


    ///////////////----------------------------------- BẢNG CURRENCY ---------------------------------
    public void addCurrencyRates(CurrencyRates currencyRates) {       // Thêm vào 1 dữ liệu tỷ giá mới
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CURRENCY_COL_TIMESTAMP, currencyRates.getTimestamp());
        values.put(CURRENCY_COL_AUD, currencyRates.rates.getAUD());
        values.put(CURRENCY_COL_EUR, currencyRates.rates.getEUR());
        values.put(CURRENCY_COL_GBP, currencyRates.rates.getGBP());
        values.put(CURRENCY_COL_JPY, currencyRates.rates.getJPY());
        values.put(CURRENCY_COL_USD, currencyRates.rates.getUSD());
        values.put(CURRENCY_COL_VND, currencyRates.rates.getVND());

        db.insert(CURRENCY_TABLE_NAME, null, values);
        db.close();
    }

    public double getRateWithTimestamp(long timestamp, String unit) {
        String query = "SELECT * FROM " + CURRENCY_TABLE_NAME + " WHERE " + CURRENCY_COL_TIMESTAMP + " = " + timestamp;
        Log.d(TAG, "query:" + query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.d(TAG, "Getting value of: " + unit + " with cursor col ");
        double rate = cursor.getDouble(CurrencyCol.valueOf(unit).getValue());
        cursor.close();
        return rate;
    }

    public long getLastestTimestamp() {
        String query = "SELECT MAX(" + CURRENCY_COL_TIMESTAMP + ") FROM " + CURRENCY_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        long maxTimestamp = cursor.getLong(0);
        cursor.close();
        return maxTimestamp;
    }

    public long getNearestTimestamp(long timestamp){
        String query = "SELECT " + CURRENCY_COL_TIMESTAMP + " FROM " + CURRENCY_TABLE_NAME + " ORDER BY ABS(" + timestamp +"-" + CURRENCY_COL_TIMESTAMP +") DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        long nearestTimestamp = cursor.getLong(0);
        cursor.close();
        return nearestTimestamp;
    }

}
