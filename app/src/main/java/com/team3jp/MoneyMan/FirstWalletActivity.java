package com.team3jp.MoneyMan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team3jp.MoneyMan.FixerioAPI.UpdateRatesTask;
import com.team3jp.MoneyMan.Items.Wallet;
import com.team3jp.MoneyMan.Utils.CurrencyUltils;
import com.team3jp.MoneyMan.Utils.TimestampUltils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FirstWalletActivity extends AppCompatActivity {
    final public String TAG = "FirstWalletActivity";
    Button btnTao;
    TextView ngaytao;
    EditText tenvi;
    EditText tienbandau;


    String mUnit;
    double mMoney = 0;

    DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
    TimestampUltils timestampUltils = new TimestampUltils();
    CurrencyUltils currencyUltils = new CurrencyUltils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_wallet);

        tenvi = (EditText) findViewById(R.id.edtTenvi);
        tienbandau = (EditText) findViewById(R.id.edtTienbandau);

        ngaytao = (TextView) findViewById(R.id.tvNgaytaovi);
        final Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        ngaytao.setText(sdf.format(date));

        // lấy đơn vị mặc định chọn ở createpassword
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mUnit = settings.getString(SettingsActivity.KEY_PREF_UNIT, "");

        tienbandau.setText("0");
        tienbandau.setSelection(1);
        tienbandau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMoney = currencyUltils.MoneyInputWithUnit(tienbandau, mUnit, this, s);
                Log.d("FrgmntDlgPaidFromWallet", "Return from " + tienbandau.getText().toString() + " -> " + mMoney);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnTao = (Button) findViewById(R.id.btnTao);
        final DatabaseHandler databaseHandler = MoneyManApplication.getDatabase();
        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tenvi.getText().toString().equals("")) {
                    Toast.makeText(FirstWalletActivity.this, R.string.not_empty_wallet_name, Toast.LENGTH_SHORT).show();
                } else {
                    Wallet wallet = createWallet();
                    if (wallet != null) {
                        databaseHandler.addWallet(wallet);
                        databaseHandler.addCurrencyRates(new CurrencyRates(1543555146, new Rates(1.557606, 1, 0.891271, 129.1872, 1.139147, 26518.248928)));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

        });
    }

    private Wallet createWallet() {

        final Date date = new Date();
        String strDateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        List<Wallet> walletList = databaseHandler.getAllWallet();

        Random random = new Random();
        int id = 1;
        random.nextInt();

        int size = walletList.size();
        if (size > 0) id = walletList.get(size - 1).getID() + 1;


        String name = tenvi.getText().toString();
        int type = 0;
        String startmoney = "0", curentmoney = "0";
        if (!tienbandau.getText().toString().equals("")) {
            startmoney = tienbandau.getText().toString();
            //TODO: 2 lines below are important
            startmoney = startmoney.replace(",", "");
            curentmoney = startmoney;
        }
        String startdate = sdf.format(date);
        String enddate = null;

        Wallet wallet = new Wallet(id, name, type, mMoney, mMoney, mUnit, startdate, enddate);

        return wallet;
    }

    private void showDialogUpdateTask() {
        FragmentManager fm = getSupportFragmentManager();
        UpdateRatesTask updateRatesTask = new UpdateRatesTask();
        updateRatesTask.show(fm, "");
    }
}
