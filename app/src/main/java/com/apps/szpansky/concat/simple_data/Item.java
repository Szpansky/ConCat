package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;

import java.util.Calendar;


public class Item extends Data {


    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_item_view);
    }


    @Override
    public Cursor getCursor() {

        return myDB.getItems(this.filter);
    }


    @Override
    public int[] getToViewIDs() {

        return (new int[]{
                R.id.item_itemId,
                R.id.item_itemName,
                R.id.item_itemPrice,
                R.id.item_itemUpdateDate
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                Database.ITEM_NUMBER,
                Database.ITEM_NAME,
                Database.ITEM_PRICE,
                Database.ITEM_UPDATE_DATE
        });
    }


    @Override
    public boolean deleteData(int itemId) {
        boolean flag = true;
        if (!myDB.delete(Database.TABLE_ORDERS, Database.ORDER_ITEM_ID, itemId)) flag = false;
        if (!myDB.delete(Database.TABLE_ITEMS, Database.ITEM_ID, itemId)) flag = false;
        return flag;
    }


    @Override
    public boolean insertData(String[] value) {
        final String[] keys = new String[]{
                Database.ITEM_ID,
                Database.ITEM_NUMBER,
                Database.ITEM_NAME,
                Database.ITEM_PRICE,
                Database.ITEM_DISCOUNT};
        return myDB.insertData(value, keys, Database.TABLE_ITEMS);
    }


    @Override
    public boolean updateData(String[] value, String[] keys) {
        String where = Database.ITEM_ID + " = " + clickedItemId;
        return myDB.updateData(value, keys, Database.TABLE_ITEMS, where);
    }


    @Override
    public String[] getClickedData() {
        Cursor cursor = myDB.getRows(Database.TABLE_ITEMS, Database.ITEM_ID, clickedItemId);
        return new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
    }
}
