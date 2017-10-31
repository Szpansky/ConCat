package com.apps.szpansky.concat.main_browsing;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.OpenOrders;
import com.apps.szpansky.concat.fragments.PickItem;
import com.apps.szpansky.concat.simple_data.Item_InPickList;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class OrdersActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, PickItem.ClickedItem {

    private static String browsingColors = "list_preference_browsing_colors";
    private static String OpenAllColor = "list_preference_open_all_colors";
    private OpenOrders ordersFragment;
    private PickItem pickItemFragment;
    private boolean contentChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.setStyle(browsingColors, sharedPreferences));

        setContentView(R.layout.simple_sliding_pane_layout);

        pickItemFragment = PickItem.newInstance(new Item_InPickList(new Database(this)));
        FragmentTransaction manager2 = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_second, pickItemFragment);
        manager2.commit();

        ordersFragment = OpenOrders.newInstance(new Order(new Database(this)));
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_first, ordersFragment);
        manager.commit();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        ordersFragment.refreshFragmentState();
        pickItemFragment.refreshFragmentState();
        contentChanged = true;
    }


    @Override
    public void onItemPick(Long id) {
        Order order = new Order(new Database(this));

        //order.setDatabase(new Database(this));         //TODO throwexeption jezeli nei ustawimy bazydanych

        Long clientId = Order.clickedClientId;

        String[] value = new String[]{clientId.toString(), id.toString(), "1"};

        if (order.insertData(value, null)) {
            Toast.makeText(this, R.string.add_item_notify, Toast.LENGTH_SHORT).show();
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            drawerLayout.closeDrawers();
            ordersFragment.refreshFragmentState();
            contentChanged = true;
        } else {
            Toast.makeText(this, R.string.error_notify_duplicate, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

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

}
