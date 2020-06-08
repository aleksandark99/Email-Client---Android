package com.example.email.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.email.R;
import com.example.email.repository.Repository;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String LIST_KEY = "sync_list";
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangedListener;

    private Preference switchPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);

        switchPref = findPreference("enable_sync");

        preferenceChangedListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (Repository.activeAccount == null){
                    //stop job
                    Toast.makeText(getActivity(), "You must first select account!", Toast.LENGTH_LONG).show();
                } else {
                    if (key.equals(LIST_KEY)){
                        Preference timeListPreference = findPreference(key);
                        timeListPreference.setSummary(sharedPreferences.getString(key, "test") + " minutes");

                    }
                }

            }
        };


        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //attempt to sync with server when no account is selected
                if ((boolean) newValue && Repository.activeAccount == null){
                    Toast.makeText(getActivity(), "pOKUSJA DA SE SELEKTUJE ", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    Toast.makeText(getActivity(), "pOKUSJA DA SE deSELEKTUJE ", Toast.LENGTH_LONG).show();
                    return true; //allow deselect
                }

            };
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangedListener);
    }
}
