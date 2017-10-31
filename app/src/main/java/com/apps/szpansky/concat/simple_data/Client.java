package com.apps.szpansky.concat.simple_data;


import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;

import static com.apps.szpansky.concat.tools.Database.*;


public class Client extends Data {

    public static long clickedCatalogId;

    public Client(Database database) {
        super(database);
    }

    @Override
    public String getTitle() {
        String where = CATALOG_ID + " = " + clickedCatalogId;
        return getDatabase().getRows(TABLE_CATALOGS, where).getString(1);
    }


    @Override
    public int getItemLayoutResourceId() {
        return R.layout.item_client_view;
    }


    @Override
    public Cursor getCursor() {
        return getDatabase().getClients(clickedCatalogId, this.filter);
    }


    @Override
    public int[] getToViewIDs() {
        return (new int[]{
                R.id.item_clientName,
                R.id.item_clientSurname,
                R.id.item_clientDate,
                R.id.item_clientOrderStatus,
                R.id.item_clientAmount,
                R.id.item_clientTotal
        });
    }


    @Override
    public String[] getFromFieldsNames() {
        return (new String[]{
                PERSON_NAME,
                PERSON_SURNAME,
                CLIENT_DATE,
                CLIENT_STATUS,
                ORDER_AMOUNT,
                ORDER_TOTAL
        });
    }


    @Override
    public boolean insertData(String[] value, String keys[]) {
        return  getDatabase().insertDataToClients(value[0],value[1],value[2]);
    }


    @Override
    public String getCurrentTable() {
        return Database.TABLE_CLIENTS;
    }

    @Override
    public String[] getClickedItemData() {
        return new String[0];
    }


    @Override
    public boolean deleteData(long clientId) {
        boolean flag = true;
        if (!getDatabase().delete(TABLE_ORDERS, ORDER_CLIENT_ID, clientId)) flag = false;
        if (!getDatabase().delete(TABLE_CLIENTS, CLIENT_ID, clientId)) flag = false;
        return flag;
    }


}
