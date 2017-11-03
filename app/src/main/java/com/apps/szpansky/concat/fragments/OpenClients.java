package com.apps.szpansky.concat.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.apps.szpansky.concat.main_browsing.OrdersActivity;
import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.UpdateStatus_Client;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class OpenClients extends SimpleFragmentWithList {


    private final int REQUEST_REFRESH = 2;

    public static OpenClients newInstance(Data data) {
        OpenClients openClients = new OpenClients();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        openClients.setArguments(bundle);
        return openClients;
    }


    @Override
    protected String selectStyleKey() {
        return "list_preference_browsing_colors";
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = getActivity().getWindow().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.END, true);
            }
        });

    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);

        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        Order.clickedClientId = id;
        getActivity().startActivityForResult(intent, REQUEST_REFRESH);

    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        UpdateStatus_Client updateStatusClient = UpdateStatus_Client.newInstance((Client) getDataObject());

        if (getActivity().getSupportFragmentManager().findFragmentByTag("updateStatusClient") == null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(updateStatusClient, "updateStatusClient").commit();
        }
    }


    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_arrow_back_black_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onNavigateUp();
            }
        });
    }


    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_playlist_add_white_24dp, null));
    }

}
