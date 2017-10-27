package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;

import static com.apps.szpansky.concat.tools.Database.*;


public class Item_InPickList extends Item {


    public Item_InPickList(String title) {
        super();
        setTitle(title);
    }


    public Item_InPickList() {
        super();
    }


    @Override
    public int getItemLayoutResourceId() {
        return (R.layout.item_iteminlist_view);
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
                R.id.item_itemPrice
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                ITEM_NUMBER,
                ITEM_NAME,
                ITEM_PRICE
        });
    }

}
