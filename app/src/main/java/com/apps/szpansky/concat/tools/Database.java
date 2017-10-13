package com.apps.szpansky.concat.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.szpansky.concat.R;

import java.util.Calendar;


public class Database extends SQLiteOpenHelper {

    static Context context;

    public static final String DATABASE_NAME = "ConCat.db";
    public static final int DATABASE_VERSION = 4;

    public static final String TABLE_CATALOGS = "CATALOGS";
    public static final String CATALOG_ID = "_id";
    public static final String CATALOG_NUMBER = "NUMBER";
    public static final String CATALOG_DATE_START = "DATE_START";
    public static final String CATALOG_DATE_ENDS = "DATE_ENDS";

    public static final String CATALOG_CLIENT_AMOUNT = "CLIENT_AMOUNT";

    public static final String TABLE_PERSONS = "PERSONS";
    public static final String PERSON_ID = "_id";
    public static final String PERSON_NAME = "NAME";
    public static final String PERSON_SURNAME = "SURNAME";
    public static final String PERSON_ADDRESS = "ADDRESS";
    public static final String PERSON_PHONE = "PHONE";
    public static final String PERSON_NETWORK_ID = "NETWORK_ID";

    public static final String TABLE_ITEMS = "ITEMS";
    public static final String ITEM_ID = "_id";
    public static final String ITEM_NUMBER = "NUMBER";
    public static final String ITEM_PRICE = "PRICE";
    public static final String ITEM_NAME = "NAME";
    public static final String ITEM_DISCOUNT = "DISCOUNT";
    public static final String ITEM_UPDATE_DATE = "UPDATE_DATE";

    public static final String TABLE_CLIENTS = "CLIENTS";
    public static final String CLIENT_ID = "_id";
    public static final String CLIENT_CATALOG_ID = "CATALOG_ID";
    public static final String CLIENT_PERSON_ID = "PERSON_ID";
    public static final String CLIENT_DATE = "DATE";
    public static final String CLIENT_STATUS = "STATUS";

    public static final String TABLE_ORDERS = "ORDERS";
    public static final String ORDER_ID = "_id";
    public static final String ORDER_CLIENT_ID = "CLIENT_ID";
    public static final String ORDER_ITEM_ID = "ITEM_ID";
    public static final String ORDER_AMOUNT = "AMOUNT";
    public static final String ORDER_TOTAL = "TOTAL";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_CATALOGS + " (" +
                CATALOG_ID + " INTEGER PRIMARY KEY NOT NULL," +
                CATALOG_NUMBER + " VARCHAR(30) UNIQUE," +
                CATALOG_DATE_START + " DATE DEFAULT CURRENT_DATE," +
                CATALOG_DATE_ENDS + " DATE DEFAULT CURRENT_DATE)");

        db.execSQL("create table " + TABLE_PERSONS + " (" +
                PERSON_ID + " INTEGER PRIMARY KEY NOT NULL," +
                PERSON_NAME + " VARCHAR(40)," +
                PERSON_SURNAME + " VARCHAR(40)," +
                PERSON_ADDRESS + " VARCHAR(150)," +
                PERSON_PHONE + " VARCHAR(25) NOT NULL," +
                PERSON_NETWORK_ID + " INTEGER DEFAULT 0)");

        db.execSQL("create table " + TABLE_ITEMS + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY NOT NULL," +
                ITEM_NUMBER + " VARCHAR(11) UNIQUE," +
                ITEM_NAME + " VARCHAR(150)," +
                ITEM_PRICE + " DECIMAL NOT NULL," +
                ITEM_DISCOUNT + " VARCHAR(15)," +
                ITEM_UPDATE_DATE + " DATE DEFAULT CURRENT_DATE)");

        db.execSQL("create table " + TABLE_CLIENTS + " (" +
                CLIENT_ID + " INTEGER PRIMARY KEY NOT NULL," +
                CLIENT_CATALOG_ID + " INTEGER," +
                CLIENT_PERSON_ID + " INTEGER," +
                CLIENT_DATE + "  DATE DEFAULT CURRENT_DATE," +
                CLIENT_STATUS + " VARCHAR(25)," +
                "FOREIGN KEY (" + CLIENT_CATALOG_ID + ") REFERENCES " + TABLE_CATALOGS + " (" + CATALOG_ID + ")," +
                "FOREIGN KEY (" + CLIENT_PERSON_ID + ") REFERENCES " + TABLE_PERSONS + " (" + PERSON_ID + "))");

        db.execSQL("create table " + TABLE_ORDERS + " (" +
                ORDER_ID + " INTEGER PRIMARY KEY NOT NULL," +
                ORDER_ITEM_ID + " INTEGER," +
                ORDER_CLIENT_ID + " INTEGER," +
                ORDER_AMOUNT + " SHORTINTEGER," +
                ORDER_TOTAL + " DECIMAL," +
                "FOREIGN KEY (" + ORDER_ITEM_ID + ") REFERENCES " + TABLE_ITEMS + " (" + ITEM_ID + ")," +
                "FOREIGN KEY (" + ORDER_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + " (" + CLIENT_ID + "))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }


    public boolean insertData(String[] values, String[] keys, String table) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            contentValues.put(keys[i], values[i]);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(table, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean updateData(String[] values, String[] keys, String table, long id) {
        ContentValues newValues = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            newValues.put(keys[i], values[i]);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(table, newValues, "_id = " + id, null);
        if (result == 1)
            return true;
        else
            return false;
    }


    public boolean insertDataToClients(String catalogId, String personId, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + CLIENT_CATALOG_ID + ", " + CLIENT_PERSON_ID +
                        " FROM " + TABLE_CLIENTS +
                        " WHERE " + CLIENT_CATALOG_ID + " = " + catalogId + " AND " + CLIENT_PERSON_ID + "=" + personId
                , null);
        if (c.getCount() != 0) return false;     //if exist, quit from adding the same data

        ContentValues contentValues = new ContentValues();
        contentValues.put(CLIENT_CATALOG_ID, catalogId);
        contentValues.put(CLIENT_PERSON_ID, personId);
        contentValues.put(CLIENT_STATUS, status);

        db = this.getWritableDatabase();
        long result = db.insert(TABLE_CLIENTS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertDataToOrders(String personId, String itemId, String amountSTR) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT " + ITEM_PRICE + " " +
                        "FROM " + TABLE_ITEMS + " " +
                        "WHERE " + ITEM_ID + " = " + itemId
                , null);

        c.moveToFirst();
        double price = c.getDouble(0);
        int amount = Integer.parseInt(amountSTR);
        double total = price * amount;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDER_CLIENT_ID, personId);
        contentValues.put(ORDER_ITEM_ID, itemId);
        contentValues.put(ORDER_AMOUNT, amount);
        contentValues.put(ORDER_TOTAL, total);

        db = this.getWritableDatabase();
        long result = db.insert(TABLE_ORDERS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getCatalogs(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT C." + CATALOG_ID + ", " +
                        "C." + CATALOG_NUMBER + ", " +
                        "C." + CATALOG_DATE_START + ", " +
                        "C." + CATALOG_DATE_ENDS + ", " +
                        "COUNT(K." + CLIENT_CATALOG_ID + ") AS " + CATALOG_CLIENT_AMOUNT + " " +
                        "FROM " + TABLE_CATALOGS + " as C " +
                        "LEFT JOIN " + TABLE_CLIENTS + " AS K " +
                        "ON C." + CATALOG_ID + " = K." + CLIENT_CATALOG_ID + " " +
                        "WHERE (" +
                        "C." + CATALOG_ID + " LIKE \"%" + filter + "%\"" + " OR " +
                        "C." + CATALOG_NUMBER + " LIKE \"%" + filter + "%\"" + " OR " +
                        "C." + CATALOG_DATE_START + " LIKE \"%" + filter + "%\"" + " OR " +
                        "C." + CATALOG_DATE_ENDS + " LIKE \"%" + filter + "%\"" + ") " +
                        "GROUP BY C." + CATALOG_ID + " " +
                        "ORDER BY C." + CATALOG_DATE_START + " DESC"
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getPersons(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * " +
                        "FROM " + TABLE_PERSONS + " " +
                        "WHERE " +
                        PERSON_NAME + " LIKE \"%" + filter + "%\"" + " OR " +
                        PERSON_SURNAME + " LIKE \"%" + filter + "%\"" + " OR " +
                        PERSON_ADDRESS + " LIKE \"%" + filter + "%\"" + " OR " +
                        PERSON_PHONE + " LIKE \"%" + filter + "%\"" + " " +
                        "ORDER BY " + PERSON_SURNAME
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getItems(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * " +
                        "FROM " + TABLE_ITEMS + " " +
                        "WHERE " +
                        ITEM_NUMBER + " LIKE \"%" + filter + "%\"" + " OR " +
                        ITEM_NAME + " LIKE \"%" + filter + "%\"" + " " +
                        "ORDER BY " + ITEM_NAME
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getClients(long id, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String workId = id.toString();
        Cursor c = db.rawQuery(
                "SELECT X." + CLIENT_ID + ", " +
                        "X." + PERSON_NAME + ", " +
                        "X." + PERSON_SURNAME + ", " +
                        "X." + CLIENT_CATALOG_ID + ", " +
                        "X." + CLIENT_DATE + ", " +
                        "X." + CLIENT_STATUS + ",  " +
                        "SUM(O." + ORDER_TOTAL + ") as " + ORDER_TOTAL + ", " +
                        "SUM(O." + ORDER_AMOUNT + ") as " + ORDER_AMOUNT + " " +
                        "FROM  (SELECT C." + CLIENT_ID + ", " +
                        "P." + PERSON_NAME + ", " +
                        "P." + PERSON_SURNAME + ", " +
                        "C." + CLIENT_CATALOG_ID + ", " +
                        "C." + CLIENT_DATE + ", " +
                        "C." + CLIENT_STATUS + " " +
                        "FROM " + TABLE_CLIENTS + " AS C " +
                        "JOIN " + TABLE_PERSONS + " AS P " +
                        "ON C." + CLIENT_PERSON_ID + " = P." + PERSON_ID + " " +
                        "GROUP BY C." + CLIENT_ID + ") AS X " +
                        "LEFT JOIN " + TABLE_ORDERS + " AS O " +
                        "ON O." + ORDER_CLIENT_ID + " = X." + CLIENT_ID + " " +
                        "WHERE X." + CLIENT_CATALOG_ID + " = " + id + " AND (" +            //workid
                        "X." + PERSON_NAME + " LIKE \"%" + filter + "%\"" + " OR " +
                        "X." + PERSON_SURNAME + " LIKE \"%" + filter + "%\"" + " OR " +
                        "X." + CLIENT_DATE + " LIKE \"%" + filter + "%\"" + " OR " +
                        "X." + CLIENT_STATUS + " LIKE \"%" + filter + "%\"" + ")" +
                        "GROUP BY X." + CLIENT_ID + " " +
                        "ORDER BY X." + PERSON_SURNAME
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getOrders(long id, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT O." + ORDER_ID + ", " +
                        "O." + ORDER_CLIENT_ID + ", " +
                        "I." + ITEM_NUMBER + ", " +
                        "I." + ITEM_NAME + ", " +
                        "O." + ORDER_AMOUNT + ", " +
                        "O." + ORDER_TOTAL + " " +
                        "FROM " + TABLE_ORDERS + " AS O " +
                        "JOIN " + TABLE_ITEMS + " AS I " +
                        "ON O." + ORDER_ITEM_ID + " = I." + ITEM_ID + " " +
                        "WHERE " + ORDER_CLIENT_ID + " = " + id + " " + " AND (" +
                        ITEM_NAME + " LIKE \"%" + filter + "%\"" + " OR " +
                        "I." + ITEM_NUMBER + " LIKE \"%" + filter + "%\"" + ") " +
                        "ORDER BY " + ITEM_NAME
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getCurrentCatalogInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT  *, MAX(" + CATALOG_DATE_START + ") " +
                        "FROM (" +
                        "SELECT " +
                        "c." + CATALOG_NUMBER + ", " +
                        "count(distinct k." + CLIENT_ID + "), " +
                        "count(o." + ORDER_ID + "), " +
                        "sum(o." + ORDER_AMOUNT + "), " +
                        "count(case k." + CLIENT_STATUS + " when '" + context.getString(R.string.db_status_not_payed) + "' then 1 else null end ), " +
                        "count(case k." + CLIENT_STATUS + " when '" + context.getString(R.string.db_status_payed) + "' then 1 else null end ), " +
                        "count(case k." + CLIENT_STATUS + " when '" + context.getString(R.string.db_status_ready) + "' then 1 else null end ), " +
                        "sum(o." + ORDER_TOTAL + "), " +
                        "c." + CATALOG_DATE_START + ", " +
                        "c." + CATALOG_DATE_ENDS + " " +
                        "FROM " + TABLE_CATALOGS + " as c " +
                        "join " + TABLE_CLIENTS + " as k " +
                        "join " + TABLE_ORDERS + " as o " +
                        "on (c." + CATALOG_ID + " = k." + CLIENT_CATALOG_ID + " and k." + CLIENT_ID + " = o." + ORDER_CLIENT_ID + ") " +
                        "group by c." + CATALOG_ID + ")"
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getCurrentInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * " +
                        "FROM (select " +
                        "count(" + CATALOG_ID + "), " +
                        "1 as filter " +
                        "from " + TABLE_CATALOGS + " " +

                        "union " +
                        "select " +
                        "count(" + CLIENT_STATUS + "), " +
                        "2 as filter " +
                        "from " + TABLE_CLIENTS + " " +

                        "union " +
                        "select " +
                        "count(" + ORDER_ID + "), " +
                        "3 as filter " +
                        "from " + TABLE_ORDERS + " " +

                        "union " +
                        "select " +
                        "sum(" + ORDER_TOTAL + "), " +
                        "4 as filter " +
                        "from " + TABLE_ORDERS + ") " +
                        "order by filter"
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public boolean updateRowOrder(String clientId, String itemId, String countSTR) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = ORDER_CLIENT_ID + " = " + clientId + " AND " + ORDER_ITEM_ID + " = " + itemId;

        Cursor c = db.rawQuery(
                "SELECT " + ITEM_PRICE +
                        " FROM " + TABLE_ITEMS +
                        " WHERE " + ITEM_ID + " = " + itemId, null);
        c.moveToFirst();
        double price = c.getDouble(0);

        c = db.rawQuery(
                "SELECT " + ORDER_AMOUNT +
                        " FROM " + TABLE_ORDERS +
                        " WHERE " + ORDER_CLIENT_ID + " = " + clientId + " AND " + ORDER_ITEM_ID + " = " + itemId, null);
        if (c.getCount() == 0) return false;
        c.moveToFirst();
        double amount = c.getDouble(0);

        int count = Integer.parseInt(countSTR);

        amount = amount + count;
        double total = price * amount;

        ContentValues newValues = new ContentValues();
        newValues.put(ORDER_AMOUNT, amount);
        newValues.put(ORDER_TOTAL, total);
        db = this.getWritableDatabase();
        long result = db.update(TABLE_ORDERS, newValues, where, null);
        if (result == 1)
            return true;
        else
            return false;
    }


    public Cursor getRows(String TABLE_NAME, String WHERE) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT *" +
                        " FROM " + TABLE_NAME +
                        " WHERE " + WHERE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public long getLong(String TABLE_NAME, String COLUMN_NAME, String fromWhere, long toWhere) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + COLUMN_NAME +
                        " FROM " + TABLE_NAME +
                        " WHERE " + fromWhere + " = " + toWhere, null);

        if (c.getCount() == 0) return -1;
        c.moveToFirst();
        return c.getLong(0);
    }


    public boolean delete(String TABLE_NAME, String fromWhere, long toWhere) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = fromWhere + " = " + toWhere;
        long result = db.delete(TABLE_NAME, where, null);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getTable(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT *" +
                        " FROM " + TABLE_NAME
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public boolean updateContentValuesToTable(String tableName, ContentValues newValues, String where) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long result;
            result = db.update(tableName, newValues, where, null);
            if (result == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException se) {
            return false;
        }
    }


    public boolean insertContentValuesToTable(String tableName, ContentValues newValues) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long result = db.insert(tableName, null, newValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException se) {
            return false;
        }
    }
}



