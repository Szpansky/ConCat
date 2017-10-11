package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;


public class Catalog extends Data {

    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_catalog_view);
    }


    @Override
    public Cursor getCursor( ) {
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
    public boolean deleteData(int catalogId) {
        int clientId;
        boolean flag = true;
        do {
            clientId = myDB.getInt(Database.TABLE_CLIENTS, Database.CLIENT_ID, Database.CLIENT_CATALOG_ID, catalogId);
            if (clientId != -1) ;{
                if (!myDB.delete(Database.TABLE_ORDERS, Database.ORDER_CLIENT_ID, clientId)) flag = false;
                if (!myDB.delete(Database.TABLE_CLIENTS, Database.CLIENT_ID, clientId)) flag = false;
            }
        } while (clientId != -1);

        if (!myDB.delete(Database.TABLE_CATALOGS, Database.CATALOG_ID, catalogId)) flag = false;
        return flag;
    }


    @Override
    public boolean insertData(String[] value) {
        String[] keys = new String[]{
                Database.CATALOG_NUMBER,
                Database.CATALOG_DATE_START,
                Database.CATALOG_DATE_ENDS};
        boolean isInserted = myDB.insertData(value, keys, Database.TABLE_CATALOGS);
        if (isInserted) {
            return true;
        } else {
           return false;
        }
    }

    @Override
    public boolean updateData(String[] value, String[] keys) {
        String where = Database.CATALOG_ID + " = " + clickedItemId;
        boolean isUpdated =  myDB.updateData(value, keys, Database.TABLE_CATALOGS, where);
        if (isUpdated) {
            return true;
        } else {
          return false;
        }
    }


    @Override
    public String[] getClickedData() {
        Cursor cursor = myDB.getRows(Database.TABLE_CATALOGS, Database.CATALOG_ID, clickedItemId);
        return new String[]{cursor.getString(1),cursor.getString(2),cursor.getString(3)};
    }

    @Override
    public String getTitle() {
        return "";
    }


}
