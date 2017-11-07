package com.apps.szpansky.concat.dialog_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.FileManagement;


public class ExportImport extends DialogFragment implements DialogInterface.OnDismissListener {

    private static final int MY_READ_EXTERNAL_STORAGE = 69;
    private static final int MY_WRITE_EXTERNAL_STORAGE = 96;
    private String tableName = Database.TABLE_ITEMS;    //default exported/imported content
    private String fileName = "Items.txt";
    private Database myDB;
    View view;
    final boolean EXPORT = true;
    final boolean IMPORT = false;
    String appName;
    private String clickedButton;


    public static ExportImport newInstance() {
        ExportImport exportImport = new ExportImport();
        exportImport.setStyle(STYLE_NO_TITLE, 0);

        return exportImport;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_export_import, container, false);

        appName = getActivity().getBaseContext().getResources().getString(R.string.app_name);

        myDB = new Database(getActivity());
        Button importDBButton = view.findViewById(R.id.dialog_ie_button_db_import);
        Button exportDBButton = view.findViewById(R.id.dialog_ie_button_db_export);
        final RadioGroup radioGroup = view.findViewById(R.id.dialog_ie_radio_group);
        Button importTableButton = view.findViewById(R.id.dialog_ie_button_folder_import);
        Button exportTableButton = view.findViewById(R.id.dialog_ie_button_folder_export);

        importDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = "ImportDB";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        new MyAsyncTask("ImportDB").execute();
                    }else {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            Toast.makeText(getActivity(),R.string.permissions_data_info,
                                    Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_READ_EXTERNAL_STORAGE);
                    }
                }else {
                    //normal permissions
                    new MyAsyncTask("ImportDB").execute();
                }
            }
        });

        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = "ExportDB";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        new MyAsyncTask("ExportDB").execute();
                    }else {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getActivity(),R.string.permissions_data_info,
                                    Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_WRITE_EXTERNAL_STORAGE);
                    }
                }else {
                    //normal permissions
                    new MyAsyncTask("ExportDB").execute();
                }
            }
        });

        importTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = "ImportTXT";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        new MyAsyncTask("ImportTXT").execute();
                    }else {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            Toast.makeText(getActivity(),R.string.permissions_data_info,
                                    Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_READ_EXTERNAL_STORAGE);
                    }
                }else {
                    //normal permissions
                    new MyAsyncTask("ImportTXT").execute();
                }
            }
        });

        exportTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = "ExportTXT";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        new MyAsyncTask("ExportTXT").execute();
                    }else {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getActivity(),R.string.permissions_data_info,
                                    Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_WRITE_EXTERNAL_STORAGE);
                    }
                }else {
                    //normal permissions
                    new MyAsyncTask("ExportTXT").execute();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case (R.id.dialog_ie_radio_items):
                        fileName = "Items.txt";
                        tableName = Database.TABLE_ITEMS;
                        break;
                    case (R.id.dialog_ie_radio_persons):
                        fileName = "Persons.txt";
                        tableName = Database.TABLE_PERSONS;
                        break;
                }
            }
        });
        return view;
    }


    private void ImportDB() {
        if (FileManagement.importExportDB(IMPORT, getActivity().getPackageName(), appName)) {
            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(view, R.string.backup_error, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void ExportDB() {
        if (FileManagement.importExportDB(EXPORT, getActivity().getPackageName(), appName)) {
            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void ImportTXT() {
        if (FileManagement.importTXT(fileName, tableName, appName, myDB)) {
            Snackbar snackbarInfo = Snackbar.make(view, getResources().getString(R.string.updated) + FileManagement.getUpdated() + getResources().getString(R.string.created) + FileManagement.getCreated(), Snackbar.LENGTH_SHORT);
            snackbarInfo.show();
        } else {
            Snackbar snackbarInfo = Snackbar.make(view, R.string.file_does_not_exists, Snackbar.LENGTH_SHORT);
            snackbarInfo.show();
        }
    }

    private void ExportTXT() {
        if (FileManagement.generateTXT(fileName, tableName, appName, myDB)) {
            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        Loading loading = Loading.newInstance();

        String task;

        public MyAsyncTask(String task) {
            this.task = task;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (task) {
                case "ImportDB": {
                    ImportDB();
                    break;
                }
                case "ExportDB": {
                    ExportDB();
                    break;
                }
                case "ImportTXT": {
                    ImportTXT();
                    break;
                }
                case "ExportTXT": {
                    ExportTXT();
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!loading.isAdded())
                getActivity().getSupportFragmentManager().beginTransaction().add(loading, "Loading").commit();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loading.isVisible()) loading.dismiss();
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

}
