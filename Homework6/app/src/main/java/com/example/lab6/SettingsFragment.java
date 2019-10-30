package com.example.lab6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("part", "OnCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_fragment);

        Preference checkPreference = findPreference("bool");
        Preference switchPreference = findPreference("switch");
        EditTextPreference editPreference = (EditTextPreference)  getPreferenceManager().findPreference("text");
        editPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        sharedPreferences = preferenceScreen.getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference) && !(p instanceof SwitchPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
        setPreferenceSummary(switchPreference, "");
        setPreferenceSummary(editPreference, "");
        setPreferenceSummary(checkPreference, "");
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("part", "OnSharedPreferenceChanged");
        Preference preference = findPreference(s);
        if (preference != null) {
            if (!(preference instanceof CheckBoxPreference) && !(preference instanceof SwitchPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String value) {
        Log.d("part", "setPreferenceSummary");
        if (preference != null) {
            if (preference instanceof ListPreference) {
                Log.d("part", "ListPreference");
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    listPreference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            }
            else if (preference instanceof EditTextPreference) {
                Log.d("part", "EditTextPreference");
                EditTextPreference editTextPreference = (EditTextPreference) preference;
                String string = editTextPreference.getText();
                Log.d("string", string);
                editTextPreference.setSummary(string);
            }
        }
    }
}
