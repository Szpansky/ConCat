package com.apps.szpansky.concat.tools;


import android.database.Cursor;

/**
 * Created by Marcin on 2017-06-09.
 */

public interface DataInterface {

    void deleteData(int id, Database myDB);

    int getItemLayoutResourceId();

    Cursor setCursor(Database myDB);

    String[] getFromFieldsNames();

    int[] getToViewIDs();

}
