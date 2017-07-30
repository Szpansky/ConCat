package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;


public class Catalog extends Client {

    public static int clickedCatalogId;


    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_catalog_view);
    }


    @Override
    public Cursor setCursor(Database myDB) {

        return myDB.getCatalogs(this.filter);
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
                Database.CATALOG_NUMBER,
                Database.CATALOG_DATE_START,
                Database.CATALOG_DATE_ENDS,
                Database.CATALOG_CLIENT_AMOUNT
        });
    }


    @Override
    public void deleteData(int catalogId, Database myDB) {

        int clientId;

        do {
            clientId = myDB.getInt(Database.TABLE_CLIENTS, Database.CLIENT_ID, Database.CLIENT_CATALOG_ID, catalogId);
            if (clientId != -1) super.deleteData(clientId, myDB);
        } while (clientId != -1);

        myDB.delete(Database.TABLE_CATALOGS, Database.CATALOG_ID, catalogId);
    }
}
