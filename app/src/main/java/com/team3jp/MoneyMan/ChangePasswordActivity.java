package com.team3jp.MoneyMan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteException;

public class ChangePasswordActivity extends AppCompatActivity implements TextWatcher {

    TextView txtTypeCurrentPassword, txtTypeNewPassword, txtRetypeNewPassword, txtError;
    TextInputEditText edtTypeCurrentPassword, edtTypeNewPassword, edtRetypeNewPassword;
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        txtTypeCurrentPassword = (TextView) findViewById(R.id.txtTypeCurrentPassword);
        txtTypeNewPassword = (TextView) findViewById(R.id.txtTypeNewPassword);
        txtRetypeNewPassword = (TextView) findViewById(R.id.txtRetypeNewPassword);
        txtError = (TextView) findViewById(R.id.txtError);

        edtTypeCurrentPassword = (TextInputEditText) findViewById(R.id.edtTypeCurrentPassword);
        edtTypeNewPassword = (TextInputEditText) findViewById(R.id.edtTypeNewPassword);
        edtRetypeNewPassword = (TextInputEditText) findViewById(R.id.edtRetypeNewPassword);

        btnApply = (Button) findViewById(R.id.btnApply);

        btnApply.getBackground().setAlpha(125);
        btnApply.setEnabled(false);

        edtTypeCurrentPassword.addTextChangedListener(this);
        edtTypeNewPassword.addTextChangedListener(this);
        edtRetypeNewPassword.addTextChangedListener(this);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: What if password didn't change successfully?
                new PasswordChangeTask().execute();
            }

        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        updateApplyButtonState();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateApplyButtonState();
    }

    @Override
    public void afterTextChanged(Editable s) {
        updateApplyButtonState();
    }

    private void updateApplyButtonState() {
        String oldPassword = edtTypeCurrentPassword.getText().toString();
        String newPassword = edtTypeNewPassword.getText().toString();
        String newConfirmPassword = edtRetypeNewPassword.getText().toString();

        // any of three EditText is empty, we won't accept
        if (oldPassword.equals("") || newPassword.equals("") || newConfirmPassword.equals("")) {
            txtError.setVisibility(View.VISIBLE);
            txtError.setText("Các ô không được trống!");
            btnApply.getBackground().setAlpha(125);
            btnApply.setEnabled(false);
            return;
        }

        if (!newPassword.equals(newConfirmPassword)) {
            if (txtError.getVisibility() != View.VISIBLE) {
                txtError.setVisibility(View.VISIBLE);
                txtError.setText("Mật khẩu mới và nhập lại không giống nhau");
            }
            btnApply.getBackground().setAlpha(125);
            btnApply.setEnabled(false);
            return;
        }

        txtError.setVisibility(View.GONE);
        btnApply.getBackground().setAlpha(255);
        btnApply.setEnabled(true);
    }

    private boolean changePassword(String currentDBPassword, String oldPasswordStr, String newPasswordStr) {
        DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        databaseHandler.recycle();

        try {
            databaseHandler.setPassword(oldPasswordStr);
            databaseHandler.update();
        } catch (SQLiteException e) {
            databaseHandler.recycle();

            if (!oldPasswordStr.equals("") && !currentDBPassword.equals("")) {
                databaseHandler.setPassword(currentDBPassword);
                databaseHandler.update();
            }
            return false;
        }

        String query = String.format("PRAGMA rekey = %s", DatabaseUtils.sqlEscapeString(newPasswordStr));
        databaseHandler.getWritableDatabase().execSQL(query);
        databaseHandler.getWritableDatabase().close();

        databaseHandler.recycle();
        MoneyManApplication.getDatabase().setPassword(newPasswordStr);
        databaseHandler.update();

        return true;

    }

    class PasswordChangeTask extends AsyncTask<Void, Void, Boolean> {
        String currentDBPassword, oldPasswordStr, newPasswordStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnApply.setEnabled(false);
            btnApply.setText("Vui lòng chờ");

            DatabaseHandler db = MoneyManApplication.getDatabase();
            currentDBPassword = db.getPassword();
            oldPasswordStr = edtTypeCurrentPassword.getText().toString();
            newPasswordStr = edtTypeNewPassword.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return changePassword(currentDBPassword, oldPasswordStr, newPasswordStr);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            btnApply.setEnabled(true);
            btnApply.setText("Đổi mật khẩu");
            if (result) {
                Toast.makeText(ChangePasswordActivity.this, "Đổi password thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu hiện tại vừa nhập không đúng", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
