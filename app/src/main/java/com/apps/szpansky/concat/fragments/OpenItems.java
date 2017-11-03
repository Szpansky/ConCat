package com.apps.szpansky.concat.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;

public class OpenItems extends SimpleFragmentWithList {


    public static OpenItems newInstance(Data data) {
        OpenItems openItems = new OpenItems();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        openItems.setArguments(bundle);
        return openItems;
    }


    @Override
    protected String selectStyleKey() {
        return "list_preference_open_all_colors";
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
    addButton.hide();
    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        showDialog(id);
    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        showDialog(id);
    }


    private void showDialog(long id) {
        AddEdit_Item addEditItem = AddEdit_Item.newInstance(id);
        if (getActivity().getSupportFragmentManager().findFragmentByTag("DialogAddEditItem") == null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(addEditItem, "DialogAddEditItem").commit();
        }
    }


    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setTitle(R.string.items);
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


}
