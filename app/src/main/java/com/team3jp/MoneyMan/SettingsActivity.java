package com.team3jp.MoneyMan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * This Activity is based on the Empty Activity template.
 * It displays the fragment for the setting.
 */
public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_USERNAME = "username";
    public static final String KEY_PREF_UNIT = "currencyunit";
    public static final String KEY_PREF_FIXERIO_API_KEY = "fixerioapikey";

    /**
     * Replaces the content with the fragment to display it.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
