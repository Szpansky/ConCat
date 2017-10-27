package com.apps.szpansky.concat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Catalog;
import com.apps.szpansky.concat.main_browsing.ClientsActivity;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;


public class OpenCatalogs extends SimpleFragment {


    public static OpenCatalogs newInstance(Data data, String styleKey) {
        OpenCatalogs openCatalogs = new OpenCatalogs();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        openCatalogs.setArguments(bundle);
        return openCatalogs;
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance();
                addEditCatalog.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });
    }


    @Override
    protected void onListViewClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance(id);
                addEditCatalog.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                Client.clickedCatalogId = id;         //to know which catalog is opened in next activity, that value is static and the same in every copy of that object
                Intent intent = new Intent(getActivity(), ClientsActivity.class);
                startActivity(intent);
            }
        });
    }
}
