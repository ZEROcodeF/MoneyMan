package com.team3jp.MoneyMan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.team3jp.MoneyMan.FixerioAPI.UpdateRatesTask;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    private ActionBar toolbar;

    static boolean doubleBackStack = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_tongquan:
                    toolbar.setTitle(R.string.title_overview);
                    fragment = new Tongquan();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_taikhoan:
                    toolbar.setTitle(R.string.title_account);
                    fragment = new Taikhoan();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_bieudo:
                    toolbar.setTitle(R.string.title_chart);
                    fragment = new Bieudo();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_cacgiaodich:
                    toolbar.setTitle(R.string.title_history);
                    fragment = new Cacgiaodich_Cacgiaodichganday();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MoneyManApplication.getDatabase().getPassword() == null) {
            // onResume will follow up which will start PasswordActivity and setup database password
            Log.d(TAG, "Password can't be found while creating activity. Start PasswordActivity");
            return;
        }

        //If later than Lolipop -> change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle(R.string.overview_title);
        loadFragment(new Tongquan());
        showDialogUpdateTask();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_map) {
            //  toolbar.setTitle("MoneyMap");
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showDialogUpdateTask() {
        FragmentManager fm = getSupportFragmentManager();
        UpdateRatesTask updateRatesTask = new UpdateRatesTask();
        updateRatesTask.show(fm, "");
    }

    @Override
    public void onBackPressed() {
        if (doubleBackStack) {
            //super.onBackPressed();
            finishAndRemoveTask();
            return;
        }
        this.doubleBackStack = true;
        Toast.makeText(this, "Nhấn 1 lần nữa để thoát khỏi ứng dụng", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackStack =false;
            }
        }, 2000);
    }

}
