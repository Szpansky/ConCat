package com.apps.szpansky.concat.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class PickItem extends SimpleFragmentWithList {

    ClickedItem clickedPerson;


    public interface ClickedItem {
        void onItemPick(Long id);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickedPerson = (ClickedItem) context;
    }

    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setTitle(R.string.pick_item);
    }


    public static PickItem newInstance(Data data) {
        PickItem pickItem = new PickItem();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        pickItem.setArguments(bundle);
        return pickItem;
    }


    @Override
    protected String selectStyleKey() {
        return "list_preference_open_all_colors";
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
    addButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddEdit_Item addEdit_item = AddEdit_Item.newInstance();
            addEdit_item.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
        }
    });
    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        clickedPerson.onItemPick(id);
    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        AddEdit_Item addEdit_item = AddEdit_Item.newInstance(id);
        addEdit_item.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
    }


    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
    }


}
