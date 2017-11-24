package com.apps.szpansky.concat.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Catalog;
import com.apps.szpansky.concat.main_browsing.ClientsActivity;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class OpenCatalogs extends SimpleFragmentWithList {

    private final int REQUEST_REFRESH = 2;

    public static OpenCatalogs newInstance() {
        OpenCatalogs openCatalogs = new OpenCatalogs();
        return openCatalogs;
    }


    @Override
    protected String selectStyleKey() {
        return "list_preference_browsing_colors";
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
    addButton.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        Client.clickedCatalogId = id;         //to know which catalog is opened in next activity, that value is static and the same in every copy of that object
        Intent intent = new Intent(getActivity(), ClientsActivity.class);
        getActivity().startActivityForResult(intent, REQUEST_REFRESH);

    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance(id);

        if (getActivity().getSupportFragmentManager().findFragmentByTag("DialogAddEditCatalog") == null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(addEditCatalog, "DialogAddEditCatalog").commit();
        }
    }


    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setTitle(R.string.orders);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_menu_black_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().openOptionsMenu();
            }
        });
    }


    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
    }

    @Override
    protected Data setDataObject() {
        return new Catalog(new Database(getActivity().getBaseContext()));
    }
}
