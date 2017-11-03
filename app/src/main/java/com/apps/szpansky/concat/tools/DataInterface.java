package com.apps.szpansky.concat.tools;


import android.database.Cursor;


public interface DataInterface {

    boolean deleteData(long id);

    boolean updateData(String[] value, String[] keys);

    boolean insertData(String[] value, String[] keys);

    String getCurrentTable();

    String[] getClickedItemData();

    int getItemLayoutResourceId();

    Cursor getCursor();

    String[] getFromFieldsNames();

    int[] getToViewIDs();

}
