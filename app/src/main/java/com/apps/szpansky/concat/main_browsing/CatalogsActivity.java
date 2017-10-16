package com.apps.szpansky.concat.main_browsing;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditCatalog;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class CatalogsActivity extends SimpleActivity {


    public CatalogsActivity() {
        super(new Catalog(), "list_preference_browsing_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fiber_new_white_24dp));
        listViewItemClick();
    }


    @Override
    public void onBackPressed() {
        onNavigateUp();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_AddEditCatalog addEditCatalog = new Dialog_AddEditCatalog().newInstance();
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });
    }


    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                Dialog_AddEditCatalog addEditCatalog = new Dialog_AddEditCatalog().newInstance(id);
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                Intent intent = new Intent(CatalogsActivity.this, ClientsActivity.class);
                Client.clickedCatalogId = id;         //to know which catalog is opened in next activity
                startActivity(intent);
            }
        });
    }

}
