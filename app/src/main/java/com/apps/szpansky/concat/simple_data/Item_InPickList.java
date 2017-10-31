package com.apps.szpansky.concat.simple_data;


import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;

import static com.apps.szpansky.concat.tools.Database.*;


public class Item_InPickList extends Item {

    public Item_InPickList(Database database) {
        super(database);
    }

    @Override
    public int getItemLayoutResourceId() {
        return (R.layout.item_iteminlist_view);
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
