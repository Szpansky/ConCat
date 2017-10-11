package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;
import android.database.SQLException;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;


public class Person extends Data {

    public static int clickedPersonId;


    @Override
    public int getItemLayoutResourceId() {

        return (R.layout.item_person_view);
    }


    @Override
    public Cursor getCursor() {

        return myDB.getPersons(this.filter);
    }


    @Override
    public int[] getToViewIDs() {

        return (new int[]{
                R.id.item_personName,
                R.id.item_personSurname,
                R.id.item_personPhone,
                R.id.item_personAddress
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                Database.PERSON_NAME,
                Database.PERSON_SURNAME,
                Database.PERSON_PHONE,
                Database.PERSON_ADDRESS
        });
    }


    @Override
    public boolean deleteData(int personId) {
        int clientId;
        boolean flag = true;
            do {
                clientId = myDB.getInt(Database.TABLE_CLIENTS, Database.CLIENT_ID, Database.CLIENT_PERSON_ID, personId);
                if (clientId != -1) {
                    if (!myDB.delete(Database.TABLE_ORDERS, Database.ORDER_CLIENT_ID, clientId)) flag = false;
                    if (!myDB.delete(Database.TABLE_CLIENTS, Database.CLIENT_ID, clientId)) flag = false;
                }
            } while (clientId != -1);
            if (!myDB.delete(Database.TABLE_PERSONS, Database.PERSON_ID, personId)) flag = false;
        return flag;
    }

    @Override
    public boolean updateData(String[] value, String[] keys) {
        String where = Database.PERSON_ID + " = " + clickedItemId;
        boolean isUpdated = myDB.updateData(value,keys,Database.TABLE_PERSONS,where);
        if (isUpdated) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean insertData(String[] value) {
        String[] keys = new String[]{
                Database.PERSON_NAME,
                Database.PERSON_SURNAME,
                Database.PERSON_ADDRESS,
                Database.PERSON_PHONE};

        boolean isInserted = myDB.insertData(value, keys, Database.TABLE_PERSONS);
        if (isInserted) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String getTitle() {
        return "";
    }
}
