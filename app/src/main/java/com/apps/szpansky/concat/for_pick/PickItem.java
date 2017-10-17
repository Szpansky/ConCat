package com.apps.szpansky.concat.for_pick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditItem;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class PickItem extends SimpleActivity {

    Integer count = 1;

    public PickItem() {
        super(new Item(), "list_preference_picking_colors");
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
                Dialog_AddEditItem addEditItem = Dialog_AddEditItem.newInstance();
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
            }
        });
    }

    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog_AddEditItem addEditItem = Dialog_AddEditItem.newInstance(id);
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("itemId", id);
                intent.putExtra("itemCount", count);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
