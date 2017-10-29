package com.apps.szpansky.concat.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.SelectCount_Item;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;

public class OpenOrders extends SimpleFragment {


    public static OpenOrders newInstance(Data data, String styleKey) {
        OpenOrders openOrders = new OpenOrders();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        openOrders.setArguments(bundle);
        return openOrders;
    }


    @Override
    protected void onAddButtonClick() {
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().getWindow().findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(Gravity.END, true);
    }

    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);

        SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) getDataObject());
        selectCount_item.show(getActivity().getFragmentManager().beginTransaction(), "SelectCount_Item");
    }

    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);

        SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) getDataObject());
        selectCount_item.show(getActivity().getFragmentManager().beginTransaction(), "SelectCount_Item");
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
