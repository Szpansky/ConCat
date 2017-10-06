package com.apps.szpansky.concat.main_browsing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.for_pick.PickItem;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OrdersActivity extends SimpleActivity {

    private final int BACK_CODE = 1;
    private boolean flag = true;

    public OrdersActivity() {
        super(new Order(),"list_preference_browsing_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle();
        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_playlist_add_white_24dp));

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


    private void setTitle(){
        Cursor c = myDB.getRows(Database.TABLE_CLIENTS, Database.CLIENT_ID, Order.clickedClientId);
        int cursorId = c.getInt(2);
        c = myDB.getRows(Database.TABLE_PERSONS, Database.PERSON_ID, cursorId);
        String title = c.getString(1) + " " + c.getString(2);
        this.setTitle(title);
    }


    private void listViewItemClick() {
        flag = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;
                popupForDelete((int) id);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {

                }
                flag = true;
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACK_CODE) {
            if (resultCode == RESULT_OK) {

                Integer itemId = data.getIntExtra("itemId", 0);
                int itemCount = data.getIntExtra("itemCount", 1);
                Integer clientId = Order.clickedClientId;

                boolean isInserted = myDB.updateRowOrder(clientId, itemId, itemCount);
                if (!isInserted)
                    myDB.insertDataToOrders(clientId.toString(), itemId.toString(), itemCount);

                refreshListView();
            }
        }
    }
}
