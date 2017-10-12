package com.apps.szpansky.concat.tools;


import android.database.Cursor;

/**
 * Created by Marcin on 2017-06-09.
 */

public interface DataInterface {

    boolean deleteData(long id);

    boolean updateData(String[] value, String[] keys);

    boolean insertData(String[] value, String[] keys);

    String getCurrentTable();

    String getTitle();

    int getItemLayoutResourceId();

    Cursor getCursor();

    String[] getFromFieldsNames();

    int[] getToViewIDs();

}
