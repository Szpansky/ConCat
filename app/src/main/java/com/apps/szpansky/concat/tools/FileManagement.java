package com.apps.szpansky.concat.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;


public final class FileManagement {

    private static Integer created = 0;
    private static Integer updated = 0;


    public static Integer getCreated() {
        return created;
    }


    public static Integer getUpdated() {
        return updated;
    }


    public static boolean generateTXT(String fileName, String tableName, String appName, Database myDb) {

        Cursor cursor = myDb.getTable(tableName);
        int columnCount = cursor.getColumnCount();
        int count = cursor.getCount();
        String toCopyLine = "";

        try {
            File root = new File(Environment.getExternalStorageDirectory(), appName + "/Exported");
            if (!root.exists()) {
                root.mkdirs();
            }

            File file = new File(root, fileName);

            FileOutputStream fileOut = new FileOutputStream(file, false);
            PrintWriter writer = new PrintWriter(fileOut);

            for (int x = 0; x < count; x++) {
                for (int y = 0; y < columnCount - 1; y++) {    //except the last one
                    toCopyLine = toCopyLine + cursor.getString(y) + "\t";
                }
                toCopyLine = toCopyLine + cursor.getString(columnCount - 1);     //the last one without "\t"

                writer.println(toCopyLine);
                cursor.moveToNext();
                toCopyLine = "";
            }

            writer.flush();
            writer.close();

            myDb.close();
            return true;
        } catch (IOException e) {
            myDb.close();
            return false;
        }
    }


    public static boolean importTXT(String fileName, String tableName,String appName, Database myDB) {
        String where_query;

        Cursor cursor = myDB.getTable(tableName);
        int columnCount = cursor.getColumnCount();
        String[] columnsName = new String[columnCount];

        for (int y = 0; y < columnCount; y++) {
            columnsName[y] = cursor.getColumnName(y);
        }

        try {
            File root = new File(Environment.getExternalStorageDirectory(), appName);
            File file = new File(root, fileName);
            if ((!root.exists()) || (!file.exists())) {
                myDB.close();
                return false;
            }
            ContentValues newValues = new ContentValues();

            FileInputStream fileIn = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileIn, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String copiedLine;
            String[] copiedCells;

            while ((copiedLine = bufferedReader.readLine()) != null) {
                copiedCells = copiedLine.split("\t");

                where_query = columnsName[0] + " = " + copiedCells[0];
                cursor.close();
                cursor = myDB.getRows(tableName, where_query);

                if (cursor.getCount() == 0) {
                    newValues.clear();
                    for (int y = 0; y < columnCount; y++) {
                        newValues.put(columnsName[y], copiedCells[y]);
                    }
                    boolean isInserted = myDB.insertContentValuesToTable(tableName, newValues);
                    if (isInserted) {
                        created++;
                    }

                } else {
                    if (columnsName[columnCount - 1].contains(myDB.ITEM_UPDATE_DATE)) {
                        where_query = columnsName[0] + " = " + copiedCells[0] + " AND "+myDB.ITEM_UPDATE_DATE+" <  '" + copiedCells[columnCount - 1] + "'";
                        cursor.close();
                        cursor = myDB.getRows(tableName, where_query);
                    }
                    if (cursor.getCount() > 0) {
                        newValues.clear();
                        for (int y = 1; y < columnCount; y++) {
                            newValues.put(columnsName[y], copiedCells[y]);
                        }
                        boolean isUpdated = myDB.updateContentValuesToTable(tableName, newValues, where_query);
                        if (isUpdated) {
                            updated++;
                        }
                    }
                }
            }
            cursor.close();
            myDB.close();
            return true;
        } catch (IOException e) {
            cursor.close();
            myDB.close();
            return false;
        }
    }


    public static boolean importExportDB(boolean export, String packageName, String appName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String backupDBPath;
            String appFolderPathOnSD;

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packageName
                        + "//databases//" + Database.DATABASE_NAME;

                if(export){
                    backupDBPath = "/" + appName + "/Exported/" + Database.DATABASE_NAME;
                    appFolderPathOnSD = sd.getPath() + "/" + appName + "/Exported/";
                }else {
                    backupDBPath = "/" + appName + "/" + Database.DATABASE_NAME;
                    appFolderPathOnSD = sd.getPath() + "/" + appName;
                }

                File currentDB;
                File backupDB;

                if (export) {
                    File file = new File(appFolderPathOnSD);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    currentDB = new File(data, currentDBPath);
                    backupDB = new File(sd, backupDBPath);

                } else {
                    backupDB = new File(data, currentDBPath);
                    currentDB = new File(sd, backupDBPath);
                }

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
