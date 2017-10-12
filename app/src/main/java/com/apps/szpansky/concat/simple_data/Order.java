package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;

import static com.apps.szpansky.concat.tools.Database.*;


public class Order extends Data {

    public static long clickedClientId;



    @Override
    public int getItemLayoutResourceId() {
        return (R.layout.item_order_view);
    }


    @Override
    public Cursor getCursor() {
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
                ITEM_NUMBER,
                ITEM_NAME,
                ORDER_AMOUNT,
                ORDER_TOTAL
        });
    }


    @Override
    public boolean deleteData(int orderId) {
        return myDB.delete(TABLE_ORDERS, ORDER_ID, orderId);
    }


    @Override
    public boolean updateData(String[] value, String[] keys){
        return myDB.updateRowOrder(value[0],value[1], value[2]);
    }

    @Override
    public boolean insertData(String[] value, String[] keys) {
        return myDB.insertDataToOrders(value[0],value[1],value[2]);
    }

    @Override
    public String getCurrentTable() {
        return Database.TABLE_ORDERS;
    }


    @Override
    public String getTitle() {
        Cursor c = myDB.getRows(TABLE_CLIENTS, CLIENT_ID, Order.clickedClientId);
        int cursorId = c.getInt(2);
        c = myDB.getRows(TABLE_PERSONS, PERSON_ID, cursorId);
        String title = c.getString(1) + " " + c.getString(2);
    return  title;
    }


}
