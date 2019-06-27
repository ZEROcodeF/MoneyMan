package com.team3jp.MoneyMan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText username;
    TextInputEditText password;
    Button Login;
    String[] donvi;//={"VND","USD","EURO"};
    Spinner spinner;
    String mUnit;

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

        setContentView(R.layout.activity_create_password);
        spinner = (Spinner) findViewById(R.id.spnTiente);
        donvi = getResources().getStringArray(R.array.list_Currency);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_unit_item, donvi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        mUnit = donvi[0];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnit = donvi[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        username = (EditText) findViewById(R.id.edtUsername);
        password = (TextInputEditText) findViewById(R.id.edtPassword);
        Login = (Button) findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mpassword = password.getText().toString();
                if (!mpassword.equals("")) {
                    String text1 = username.getText().toString();
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(SettingsActivity.KEY_PREF_USERNAME, text1);
                    editor.putString(SettingsActivity.KEY_PREF_UNIT, mUnit);
                    editor.apply();
                    new LoginTask().execute(mpassword);
                } else {
                    Toast.makeText(CreatePasswordActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        // echo on the textbox the user's selection
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

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
                Intent intent = new Intent(getApplicationContext(), FirstWalletActivity.class);
                CreatePasswordActivity.this.startActivity(intent);
                CreatePasswordActivity.this.finish();
            } else {
                Toast.makeText(CreatePasswordActivity.this, R.string.incorrect_password, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
