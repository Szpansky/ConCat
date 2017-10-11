package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;


public class Client extends Data {

    public static int clickedCatalogId;

    @Override
    public String getTitle() {
        return myDB.getRows(Database.TABLE_CATALOGS,Database.CATALOG_ID, Client.clickedCatalogId ).getString(1);
    }


    @Override
    public int getItemLayoutResourceId() {
        return R.layout.item_client_view;
    }


    @Override
    public Cursor getCursor() {
        return myDB.getClients(clickedCatalogId, this.filter);
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
                Database.PERSON_NAME,
                Database.PERSON_SURNAME,
                Database.CLIENT_DATE,
                Database.CLIENT_STATUS,
                Database.ORDER_AMOUNT,
                Database.ORDER_TOTAL
        });
    }


    @Override
    public boolean deleteData(int clientId) {
        boolean flag = true;
        if (!myDB.delete(Database.TABLE_ORDERS, Database.ORDER_CLIENT_ID, clientId)) flag = false;
        if (!myDB.delete(Database.TABLE_CLIENTS, Database.CLIENT_ID, clientId)) flag = false;
        return flag;
    }


    @Override
    public boolean insertData(String[] value) {
        return  myDB.insertDataToClients(value[0],value[1],value[2]);
    }


    @Override
    public boolean updateData(String[] value, String[] keys){
        String where = Database.CLIENT_ID + " = " + clickedItemId;
        return myDB.updateData(value,keys,Database.TABLE_CLIENTS,where);
    }
}
