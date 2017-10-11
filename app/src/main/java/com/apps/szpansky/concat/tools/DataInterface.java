package com.apps.szpansky.concat.tools;


import android.database.Cursor;

/**
 * Created by Marcin on 2017-06-09.
 */

public interface DataInterface {

    boolean deleteData(int id);

    boolean insertData(String[] value);

    boolean updateData(String[] value, String[] keys);

    String getTitle();

    int getItemLayoutResourceId();

    Cursor getCursor();

    String[] getFromFieldsNames();

    int[] getToViewIDs();

}
