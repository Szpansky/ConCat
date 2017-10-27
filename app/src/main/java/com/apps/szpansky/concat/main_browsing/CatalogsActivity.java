package com.apps.szpansky.concat.main_browsing;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.OpenCatalogs;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.tools.SimpleFunctions;

public class CatalogsActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private static String styleKey = "list_preference_browsing_colors";
    private OpenCatalogs openCatalogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.setStyle(styleKey, sharedPreferences));

        setContentView(R.layout.simple_sliding_pane_layout);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        openCatalogs = OpenCatalogs.newInstance(new Catalog(), styleKey);
        FragmentTransaction manager2 = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_first, openCatalogs);
        manager2.commit();

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        openCatalogs.refreshListView();

    }

    @Override
    public void onBackPressed() {
        onNavigateUp();
    }

}
