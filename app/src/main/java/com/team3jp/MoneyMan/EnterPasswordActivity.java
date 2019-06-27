package com.team3jp.MoneyMan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteException;

public class EnterPasswordActivity extends AppCompatActivity {
    TextInputEditText checkpassword;
    Button button;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If later than Lolipop -> change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorWhite));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_enter_password);
        //load password
        //SharedPreferences settings= getSharedPreferences("PREFS",0);
        //password =settings.getString("password","");
        checkpassword = (TextInputEditText) findViewById(R.id.edtPassword);
        button = (Button) findViewById(R.id.btnLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute(checkpassword.getText().toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... args) {
            DatabaseHandler handler = MoneyManApplication.getDatabase();
            handler.recycle();
            try {
                MoneyManApplication.getDatabase().setPassword(args[0]);
                handler.update();
            } catch (SQLiteException e) {
                // Most likely wrong password provided
                return false;
            } catch (IllegalArgumentException e) {
                // Illegal password provided. eg. empty password
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                if (!MoneyManApplication.getDatabase().getAllWallet().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    EnterPasswordActivity.this.startActivity(intent);
                    EnterPasswordActivity.this.finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), FirstWalletActivity.class);
                    EnterPasswordActivity.this.startActivity(intent);
                    EnterPasswordActivity.this.finish();
                }
            } else {
                Toast.makeText(EnterPasswordActivity.this, getResources().getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
