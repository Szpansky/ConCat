package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;

import java.util.Calendar;

import static com.apps.szpansky.concat.tools.Database.*;


public class Item extends Data {


    public Item() {
        currentTable = TABLE_ITEMS;
    }

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
                ITEM_NUMBER,
                ITEM_NAME,
                ITEM_PRICE,
                ITEM_UPDATE_DATE
        });
    }


    @Override
    public boolean deleteData(int itemId) {
        boolean flag = true;
        if (!myDB.delete(TABLE_ORDERS, ORDER_ITEM_ID, itemId)) flag = false;
        if (!myDB.delete(TABLE_ITEMS, ITEM_ID, itemId)) flag = false;
        return flag;
    }

    @Override
    public String getCurrentTable() {
        return Database.TABLE_ITEMS;
    }


    @Override
    public String[] getClickedData() {
        Cursor cursor = myDB.getRows(TABLE_ITEMS, ITEM_ID, clickedItemId);
        return new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
    }
}
