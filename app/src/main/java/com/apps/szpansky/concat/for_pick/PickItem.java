package com.apps.szpansky.concat.for_pick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.add_edit.AddEditItemsActivity;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class PickItem extends SimpleActivity {


    public PickItem() {
        super(new Item());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickItem.this, AddEditItemsActivity.class);
                PickItem.this.startActivity(intent);
            }
        });
    }


    private void listViewItemClick() {
        final boolean[] flag = new boolean[1];
        flag[0] = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag[0] = false;

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag[0]) {

                    Intent intent = new Intent();
                    intent.putExtra("itemId", (int) id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                flag[0] = true;
            }
        });
    }
}
