package com.example.email.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.email.R;
import com.example.email.job_service.MyJobScheduler;
import com.example.email.repository.Repository;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String LIST_KEY = "sync_list";
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangedListener;

    private Preference switchPref;

    public static JobScheduler mJobScheduler;
    private JobInfo mJobInfo;
    private final int id = 101;

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

                        //kill existing service
                        if (mJobScheduler != null) mJobScheduler.cancel(id);

                        Preference timeListPreference = findPreference(key);
                        timeListPreference.setSummary(sharedPreferences.getString(key, "test") + " minutes");
                        //start service

                        ComponentName componentName = new ComponentName(getActivity(), MyJobScheduler.class);
                        JobInfo.Builder builder = new JobInfo.Builder(id ,componentName);

                        String stringPeriod = sharedPreferences.getString(key, "15");

                        int period = Integer.valueOf(stringPeriod) * 1000;
                        builder.setPeriodic(period);
                        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                        builder.setPersisted(true); //this job exist even after system reboot

                        mJobInfo = builder.build();

                        mJobScheduler = (JobScheduler) getActivity().getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                        int res = mJobScheduler.schedule(mJobInfo);
                        if (res == JobScheduler.RESULT_SUCCESS) {
                            Log.i("res", "success");
                            Toast.makeText(getActivity(), "Job started..", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("res", "not success");
                            Toast.makeText(getActivity(), "Job not started..", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        };


        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue && Repository.activeAccount == null){
                    Toast.makeText(getActivity(), "Select account for which you want to be notified! ", Toast.LENGTH_LONG).show();
                    return false;

                } else if ((boolean) newValue && Repository.activeAccount != null){

                } else {
                    //stop job service
                    mJobScheduler = (JobScheduler) getActivity().getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    mJobScheduler.cancel(id);
                    Toast.makeText(getActivity(), "Job canceled... ", Toast.LENGTH_LONG).show();
                    return true; //allow deselect
                }
                return true;
            }
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
