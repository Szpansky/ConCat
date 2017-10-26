package com.apps.szpansky.concat.fragments;


import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

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
        addButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_playlist_add_white_24dp, null));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity().getWindow().findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.END, true);
            }
        });
    }

    @Override
    public void onListViewClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);

                SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) data);
                selectCount_item.show(getActivity().getFragmentManager().beginTransaction(), "SelectCount_Item");

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);

                SelectCount_Item selectCount_item = SelectCount_Item.newInstance((Order) data);
                selectCount_item.show(getActivity().getFragmentManager().beginTransaction(), "SelectCount_Item");

            }
        });
    }


}
