package com.apps.szpansky.concat.main_browsing;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.OpenClients;
import com.apps.szpansky.concat.fragments.PickPerson;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.simple_data.Person_InPickList;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class ClientsActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, PickPerson.ClickedPerson {

    private static String styleKey = "list_preference_browsing_colors";
    private OpenClients clientsFragment;
    private PickPerson pickPersonFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.setStyle(styleKey, sharedPreferences));

        setContentView(R.layout.simple_sliding_pane_layout);

        pickPersonFragment = PickPerson.newInstance(new Person_InPickList(getResources().getString(R.string.pick_person)), "list_preference_picking_colors");
        FragmentTransaction manager2 = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_second, pickPersonFragment);
        manager2.commit();

        clientsFragment = OpenClients.newInstance(new Client(), styleKey);
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_first, clientsFragment);
        manager.commit();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        clientsFragment.refreshListView();
        pickPersonFragment.refreshListView();
    }


    @Override
    public void onPersonPick(Long id) {
        Client client = new Client();

        client.setDatabase(new Database(this));         //TODO throwexeption jezeli nei ustawimy bazydanych

        Long catalogId = Client.clickedCatalogId;

        String[] value = new String[]{catalogId.toString(), id.toString(), getString(R.string.db_status_not_payed)};
        String[] keys = new String[]{Database.CLIENT_CATALOG_ID, Database.CLIENT_PERSON_ID, Database.CLIENT_STATUS};

        if (client.insertData(value, keys)) {
            Toast.makeText(this, R.string.add_client_notify, Toast.LENGTH_SHORT).show();
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            drawerLayout.closeDrawers();
        } else {
            Toast.makeText(this, R.string.error_notify_duplicate, Toast.LENGTH_SHORT).show();
        }
        clientsFragment.refreshListView();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.closeDrawers();
        } else {
            onNavigateUp();
        }
    }
}
