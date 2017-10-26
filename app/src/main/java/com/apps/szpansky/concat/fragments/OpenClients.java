package com.apps.szpansky.concat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.main_browsing.OrdersActivity;
import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.UpdateStatus_Client;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;

public class OpenClients extends SimpleFragment {


    public static OpenClients newInstance(Data data, String styleKey) {
        OpenClients openClients = new OpenClients();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        openClients.setArguments(bundle);
        return openClients;
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

                UpdateStatus_Client updateStatusClient = UpdateStatus_Client.newInstance((Client) data);
                updateStatusClient.show(getActivity().getFragmentManager().beginTransaction(), "Login");

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);

                Intent intent = new Intent(getActivity(), OrdersActivity.class);
                Order.clickedClientId = id;
                startActivity(intent);

            }
        });
    }


}