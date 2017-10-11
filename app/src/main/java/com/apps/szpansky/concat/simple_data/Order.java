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
                Database.ITEM_NUMBER,
                Database.ITEM_NAME,
                Database.ORDER_AMOUNT,
                Database.ORDER_TOTAL
        });
    }


    @Override
    public boolean deleteData(int orderId) {
        return myDB.delete(Database.TABLE_ORDERS, Database.ORDER_ID, orderId);
    }


    @Override
    public boolean insertData(String[] value) {
        return myDB.insertDataToOrders(value[0],value[1],value[2]);
    }


    @Override
    public boolean updateData(String[] value, String[] keys){
        return myDB.updateRowOrder(clickedClientId,clickedItemId,value[0]);
    }




    @Override
    public String getTitle() {
        Cursor c = myDB.getRows(Database.TABLE_CLIENTS, Database.CLIENT_ID, Order.clickedClientId);
        int cursorId = c.getInt(2);
        c = myDB.getRows(Database.TABLE_PERSONS, Database.PERSON_ID, cursorId);
        String title = c.getString(1) + " " + c.getString(2);
    return  title;
    }


}
