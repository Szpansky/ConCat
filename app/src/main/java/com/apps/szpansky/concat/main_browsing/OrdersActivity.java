package com.apps.szpansky.concat.main_browsing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.for_pick.PickItem;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OrdersActivity extends SimpleActivity {

    private final int BACK_CODE = 1;

    public OrdersActivity() {
        super(new Order());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor c = myDB.getRows(Database.TABLE_CLIENTS, Database.CLIENT_ID, Order.clickedClientId);
        int cursorId = c.getInt(2);
        c = myDB.getRows(Database.TABLE_PERSONS, Database.PERSON_ID, cursorId);
        String title = c.getString(1) + " " + c.getString(2);
        this.setTitle(title);


        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, PickItem.class);
                startActivityForResult(intent, BACK_CODE);
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

                popupForDelete((int) id);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag[0]) {

                }
                flag[0] = true;
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACK_CODE) {
            if (resultCode == RESULT_OK) {

                Integer itemId = data.getIntExtra("itemId", 0);
                Integer clientId = Order.clickedClientId;

                int count = 1;

                boolean isInserted = myDB.updateRowOrder(clientId, itemId, count);
                if (!isInserted)
                    myDB.insertDataToOrders(clientId.toString(), itemId.toString(), count);

                refreshListView();
            }
        }
    }
}
