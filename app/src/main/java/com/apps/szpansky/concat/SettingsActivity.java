package com.apps.szpansky.concat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.apps.szpansky.concat.tools.Database;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public String appVersion;
    private PackageInfo packageInfo;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Database myDB = new Database(this);
    private static String DELETE_CODE = "5723";
    private boolean flag = false;


    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        editor = sharedPreferences.edit();
        editor.putString("pref_edit_text_version", this.appVersion);
        editor.apply();
    }


    public void setPackageInfo() {
        try {
            this.packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        flag = false;
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setPackageInfo();
        setAppVersion(packageInfo.versionName);


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {
            case ("pref_edit_text_data_base_clean"): {
                if (sharedPreferences.getString(key, "0").equals(DELETE_CODE)) {
                    editor.putString(key, getResources().getString(R.string.pref_def_data_base_exist));
                    editor.apply();
                    Toast.makeText(this, R.string.successfully_notify, Toast.LENGTH_SHORT).show();
                    this.deleteDatabase(myDB.getDatabaseName());
                }
            }
        }
    flag = true;
    }


    @Override
    public void onBackPressed() {
        if(flag) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
        } else{
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
        }
        onNavigateUp();
    }


    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
        }
    }

}
