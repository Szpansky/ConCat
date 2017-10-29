package com.apps.szpansky.concat.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;


public class PickItem extends SimpleFragment {


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
    }


    public static PickItem newInstance(Data data, String styleKey) {
        PickItem pickItem = new PickItem();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        pickItem.setArguments(bundle);
        return pickItem;
    }


    @Override
    protected void onAddButtonClick() {
        AddEdit_Item addEdit_item = AddEdit_Item.newInstance();
        addEdit_item.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
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
