package com.apps.szpansky.concat.main_browsing;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.OpenClients;
import com.apps.szpansky.concat.fragments.PickPerson;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.simple_data.Person_InPickList;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class ClientsActivity extends FragmentActivity implements DialogInterface.OnDismissListener, PickPerson.ClickedPerson {

    private static String browsingColors = "list_preference_browsing_colors";
    private OpenClients clientsFragment;
    private PickPerson pickPersonFragment;
    private boolean contentChanged = false;
    final int REQUEST_REFRESH = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.getStyleFromSharedPref(browsingColors, sharedPreferences));

        setContentView(R.layout.frame_with_navigation_layout);

        pickPersonFragment = PickPerson.newInstance(new Person_InPickList(new Database(this)));
        FragmentTransaction manager2 = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_second, pickPersonFragment);
        manager2.commit();

        clientsFragment = OpenClients.newInstance(new Client(new Database(this)));
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_first, clientsFragment);
        manager.commit();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        clientsFragment.refreshFragmentState();
        pickPersonFragment.refreshFragmentState();
        contentChanged = true;
    }


    @Override
    public void onPersonPick(Long id) {
        Client client = new Client(new Database(this));

        Long catalogId = Client.clickedCatalogId;

        String[] value = new String[]{catalogId.toString(), id.toString(), getString(R.string.db_status_not_payed)};
        String[] keys = new String[]{Database.CLIENT_CATALOG_ID, Database.CLIENT_PERSON_ID, Database.CLIENT_STATUS};

        if (client.insertData(value, keys)) {
            Toast.makeText(this, R.string.add_client_notify, Toast.LENGTH_SHORT).show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                    drawerLayout.closeDrawers();
                }
            }, 250);

            clientsFragment.refreshFragmentState();
            contentChanged = true;
        } else {
            Toast.makeText(this, R.string.error_notify_duplicate, Toast.LENGTH_SHORT).show();
        }
        client = null;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.closeDrawers();
        } else {
            if (contentChanged) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                onNavigateUp();
            } else {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                onNavigateUp();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case (REQUEST_REFRESH): {
                    clientsFragment.refreshFragmentState();
                    contentChanged = true;
                    break;
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
    }
}
