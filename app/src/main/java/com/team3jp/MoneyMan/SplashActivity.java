package com.team3jp.MoneyMan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If later than Lolipop -> change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        setContentView(R.layout.activity_splash);
        //load the pasword
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        username = settings.getString(SettingsActivity.KEY_PREF_USERNAME, "");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkExistingDatabase() || username.equals("")) {
                    //if there is no database or no username
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    //if there is a password
                    Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 2000);

    }

    private boolean checkExistingDatabase() {
        File dbFile = getDatabasePath(DatabaseHandler.DBNAME);
        return dbFile.exists();
    }
}
