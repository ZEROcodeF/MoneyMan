/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.team3jp.MoneyMan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Creates the setting from the XML preferences file.
     *
     * @param savedInstanceState
     * @param rootKey            This preference fragment is rooted at the PreferenceScreen with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        final EditTextPreference editTextPreference = (EditTextPreference) findPreference(SettingsActivity.KEY_PREF_USERNAME);
        final ListPreference listPreference = (ListPreference) findPreference(SettingsActivity.KEY_PREF_UNIT);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        editTextPreference.setTitle(sharedPreferences.getString(SettingsActivity.KEY_PREF_USERNAME, ""));
        listPreference.setTitle(sharedPreferences.getString(SettingsActivity.KEY_PREF_UNIT, ""));

        String[] strUnit = getResources().getStringArray(R.array.list_Currency);

        //int str = sharedPreferences.getInt(SettingsActivity.KEY_PREF_UNIT,1);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());


        Log.d("SETTING", "abc is :" + sp.getString(SettingsActivity.KEY_PREF_UNIT, "-1"));

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String yourString = o.toString();
                sharedPreferences.edit().putString(SettingsActivity.KEY_PREF_USERNAME, yourString).apply();
                editTextPreference.setTitle(yourString);
                return true;
            }
        });

        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String yourString = o.toString();
                sharedPreferences.edit().putString(SettingsActivity.KEY_PREF_UNIT, yourString).apply();
                listPreference.setTitle(yourString);
                return true;
            }
        });

    }
}
