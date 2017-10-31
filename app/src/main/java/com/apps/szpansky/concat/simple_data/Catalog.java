package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;

import static com.apps.szpansky.concat.tools.Database.*;

public class Catalog extends Data {

    public Catalog(Database database) {
        super(database);
    }

    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_catalog_view);
    }


    @Override
    public Cursor getCursor() {
        return getDatabase().getCatalogs(this.filter);
    }


    @Override
    public int[] getToViewIDs() {

        return (new int[]{
                R.id.item_catalogNumber,
                R.id.item_catalogDateStart,
                R.id.item_catalogDateEnd,
                R.id.item_catalogClientAmount
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                CATALOG_NUMBER,
                CATALOG_DATE_START,
                CATALOG_DATE_ENDS,
                CATALOG_CLIENT_AMOUNT
        });
    }


    @Override
    public boolean deleteData(long catalogId) {
        long clientId;
        boolean flag = true;
        do {
            clientId = getDatabase().getLong(TABLE_CLIENTS, CLIENT_ID, CLIENT_CATALOG_ID, catalogId);
            if (clientId != -1) ;
            {
                if (!getDatabase().delete(TABLE_ORDERS, ORDER_CLIENT_ID, clientId)) flag = false;
                if (!getDatabase().delete(TABLE_CLIENTS, CLIENT_ID, clientId)) flag = false;
            }
        } while (clientId != -1);

        if (!getDatabase().delete(TABLE_CATALOGS, CATALOG_ID, catalogId)) flag = false;
        return flag;
    }


    @Override
    public String getCurrentTable() {
        return Database.TABLE_CATALOGS;
    }


    @Override
    public String[] getClickedItemData() {
        String where = CATALOG_ID + " = " + clickedItemId;
        Cursor cursor = getDatabase().getRows(TABLE_CATALOGS, where);
        return new String[]{cursor.getString(1), cursor.getString(2), cursor.getString(3)};
    }


}
