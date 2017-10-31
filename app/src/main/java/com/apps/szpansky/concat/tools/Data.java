package com.apps.szpansky.concat.tools;

import java.io.Serializable;

public abstract class Data implements DataInterface, Serializable {

    protected String filter = "";
    protected long clickedItemId = 0;
    protected String currentTable;

    private transient Database database;              //database is not serializable
    private String title = "";

    public Data(Database database) {
        this.currentTable = getCurrentTable();
        setDatabase(database);
    }

    public void setClickedItemId(long clickedItemId) {
        this.clickedItemId = clickedItemId;
    }

    public long getClickedItemId() {
        return clickedItemId;
    }

    public void setDatabase(Database myDB) {
        this.database = myDB;
    }

    public Database getDatabase() {
        return database;
    }

    public boolean updateData(String[] value, String[] keys) {
        return database.updateData(value, keys, currentTable, clickedItemId);
    }

    public boolean insertData(String[] value, String[] keys) {
        return database.insertData(value, keys, currentTable);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
