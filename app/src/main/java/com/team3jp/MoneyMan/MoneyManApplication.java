package com.team3jp.MoneyMan;

import android.app.Application;
import android.view.ViewConfiguration;

import com.team3jp.MoneyMan.Utils.MoneyIndexer;

import net.sqlcipher.database.SQLiteDatabase;

import java.lang.reflect.Field;

public class MoneyManApplication extends Application {
    private static DatabaseHandler mDatabase;
    private static MoneyIndexer moneyIndexer;

    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteDatabase.loadLibs(this);
        mDatabase = new DatabaseHandler(this);
        moneyIndexer = new MoneyIndexer(this);

        // Force show overflow button on Action Bar
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }

    public static DatabaseHandler getDatabase() {
        return mDatabase;
    }

    public static MoneyIndexer getMoneyIndexer() {
        return moneyIndexer;
    }

    public static void setMoneyIndexer(MoneyIndexer moneyIndexer) {
        getMoneyIndexer().clear();
        getMoneyIndexer().copy(moneyIndexer);
    }
}
