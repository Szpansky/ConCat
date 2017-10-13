package com.apps.szpansky.concat.tools;

public abstract class Data implements DataInterface {

    protected String filter = "";
    protected long clickedItemId;
    protected String currentTable;

    public Database getMyDB() {
        return myDB;
    }

    protected Database myDB;

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
        return myDB.updateData(value,keys, currentTable, clickedItemId);
    }

    public boolean insertData(String[] value, String[] keys) {
        return myDB.insertData(value,keys,currentTable);
    }

    public String getTitle() {
        return "";
    }

}
