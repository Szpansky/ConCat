package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;


public class Order extends Data {

    public static int clickedClientId;


    @Override
    public int getItemLayoutResourceId() {
        return (R.layout.item_order_view);
    }


    @Override
    public Cursor setCursor(Database myDB) {
        return myDB.getOrders(clickedClientId, this.filter);
    }


    @Override
    public int[] getToViewIDs() {

        return (new int[]{
                R.id.item_orderItemId,
                R.id.item_orderItemName,
                R.id.item_orderAmount,
                R.id.item_orderTotal
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                Database.ITEM_NUMBER,
                Database.ITEM_NAME,
                Database.ORDER_AMOUNT,
                Database.ORDER_TOTAL
        });
    }


    @Override
    public void deleteData(int orderId, Database myDB) {

        myDB.delete(Database.TABLE_ORDERS, Database.ORDER_ID, orderId);
    }
}
