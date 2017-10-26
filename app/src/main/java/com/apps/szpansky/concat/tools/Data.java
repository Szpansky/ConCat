package com.apps.szpansky.concat.tools;

import java.io.Serializable;

public abstract class Data implements DataInterface, Serializable {

    protected String filter = "";
    protected long clickedItemId = 0;
    protected String currentTable;

    public Database getMyDB() {
        return myDB;
    }

    protected transient Database myDB;              //database is not serializable

    public Data() {
        this.currentTable = getCurrentTable();
    }

    public void setClickedItemId(long clickedItemId) {
        this.clickedItemId = clickedItemId;
    }

    public long getClickedItemId() {
        return clickedItemId;
    }

    public void setDatabase(Database myDB) {
        this.myDB = myDB;
    }

    public boolean updateData(String[] value, String[] keys) {
        return myDB.updateData(value, keys, currentTable, clickedItemId);
    }

    public boolean insertData(String[] value, String[] keys) {
        return myDB.insertData(value, keys, currentTable);
    }

    public String getTitle() {
        return "";
    }

}
