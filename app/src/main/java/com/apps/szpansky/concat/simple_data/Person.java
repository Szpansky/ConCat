package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;

import static com.apps.szpansky.concat.tools.Database.*;


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
                PERSON_NAME,
                PERSON_SURNAME,
                PERSON_PHONE,
                PERSON_ADDRESS
        });
    }


    @Override
    public boolean deleteData(long personId) {
        long clientId;
        boolean flag = true;
            do {
                clientId = myDB.getLong(TABLE_CLIENTS, CLIENT_ID, CLIENT_PERSON_ID, personId);
                if (clientId != -1) {
                    if (!myDB.delete(TABLE_ORDERS, ORDER_CLIENT_ID, clientId)) flag = false;
                    if (!myDB.delete(TABLE_CLIENTS, CLIENT_ID, clientId)) flag = false;
                }
            } while (clientId != -1);
            if (!myDB.delete(TABLE_PERSONS, PERSON_ID, personId)) flag = false;
        return flag;
    }

    @Override
    public String getCurrentTable() {
        return Database.TABLE_PERSONS;
    }


    @Override
    public String getTitle() {
        return "";
    }
}
