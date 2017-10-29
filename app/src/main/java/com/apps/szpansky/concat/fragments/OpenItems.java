package com.apps.szpansky.concat.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;

public class OpenItems extends SimpleFragment {


    public static OpenItems newInstance(Data data, String styleKey) {
        OpenItems openItems = new OpenItems();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        openItems.setArguments(bundle);
        return openItems;
    }


    @Override
    protected void onAddButtonClick() {
        AddEdit_Item addEditItem = AddEdit_Item.newInstance();
        addEditItem.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditItem");
    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        AddEdit_Item addEditItem = AddEdit_Item.newInstance(id);
        addEditItem.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditItem");
    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        AddEdit_Item addEditItem = AddEdit_Item.newInstance(id);
        addEditItem.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditItem");
    }


    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
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
