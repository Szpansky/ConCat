package com.apps.szpansky.concat.tools;


public abstract class Data implements DataInterface {

    protected String filter = "";

    public int getClickedItemId() {
        return clickedItemId;
    }

    public void setClickedItemId(int clickedItemId) {
        this.clickedItemId = clickedItemId;
    }

    protected int clickedItemId;

    public Database getMyDB() {
        return myDB;
    }

    public String[] getClickedData(){
        return new String[]{""};
    }

    protected Database myDB;


    public void setDatabase(Database myDB) {
        this.myDB = myDB;
    }

    public boolean updateData(String[] value, String[] keys) {
        return false;
    }

    public String getTitle() {
        return "";
    }

}
