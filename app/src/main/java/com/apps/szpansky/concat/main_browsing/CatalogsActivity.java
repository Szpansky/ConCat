package com.apps.szpansky.concat.main_browsing;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Catalog;
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
        addButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
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
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance();
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });
    }


    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance(id);
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                Client.clickedCatalogId = id;         //to know which catalog is opened in next activity, that value is static and the same in every copy of that object
                Intent intent = new Intent(CatalogsActivity.this, ClientsActivity.class);
                startActivity(intent);
            }
        });
    }

}
