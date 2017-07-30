package com.apps.szpansky.concat.simple_data;

import android.database.Cursor;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;


public class Client extends Order {

    public static int clickedCatalogId;


    @Override
    public int getItemLayoutResourceId() {

        return R.layout.item_client_view;
    }


    @Override
    public Cursor setCursor(Database myDB) {
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
    public void deleteData(int clientId, Database myDB) {

        myDB.delete(Database.TABLE_ORDERS, Database.ORDER_CLIENT_ID, clientId);
        myDB.delete(Database.TABLE_CLIENTS, Database.CLIENT_ID, clientId);
    }
}
