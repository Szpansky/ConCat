package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;


public class Item extends Order {

    public static int clickedItemId;


    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_item_view);
    }


    @Override
    public Cursor setCursor(Database myDB) {

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
    public void deleteData(int itemId, Database myDB) {

        myDB.delete(Database.TABLE_ORDERS, Database.ORDER_ITEM_ID, itemId);
        myDB.delete(Database.TABLE_ITEMS, Database.ITEM_ID, itemId);
    }
}
