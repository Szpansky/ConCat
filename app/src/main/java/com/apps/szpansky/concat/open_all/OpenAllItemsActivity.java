package com.apps.szpansky.concat.open_all;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OpenAllItemsActivity extends SimpleActivity {

    public OpenAllItemsActivity() {
        super(new Item(), "list_preference_open_all_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fiber_new_white_24dp));
        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddEdit_Item addEditItem = AddEdit_Item.newInstance();
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
            }
        });
    }


    public void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                data.setClickedItemId(id);
                AddEdit_Item addEditItem = AddEdit_Item.newInstance(id);
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                data.setClickedItemId(id);
                AddEdit_Item addEditItem = AddEdit_Item.newInstance(id);
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
            }
        });
    }


    @Override
    public void onBackPressed() {
        onNavigateUp();
    }

}
