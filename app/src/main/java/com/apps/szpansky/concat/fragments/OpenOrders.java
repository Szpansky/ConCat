package com.apps.szpansky.concat.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.SelectCount_Item;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class OpenOrders extends SimpleFragmentWithList {


    public static OpenOrders newInstance(Data data) {
        OpenOrders openOrders = new OpenOrders();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        openOrders.setArguments(bundle);
        return openOrders;
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

        SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) getDataObject());
        getActivity().getFragmentManager().beginTransaction().add(selectCount_item, "SelectCount_Item").commit();
    }

    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);

        SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) getDataObject());
        getActivity().getFragmentManager().beginTransaction().add(selectCount_item, "SelectCount_Item").commit();
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
