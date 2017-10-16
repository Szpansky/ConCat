package com.apps.szpansky.concat.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.FileManagement;


public class Dialog_ExportImport extends DialogFragment {

    private String tableName = Database.TABLE_ITEMS;    //default exported/imported content
    private String fileName = "Items.txt";
    private Database myDB;


    public Dialog_ExportImport newInstance() {
        Dialog_ExportImport exportImport = new Dialog_ExportImport();
        exportImport.setStyle(STYLE_NO_TITLE, 0);

        return exportImport;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_export_import, null);
        final Dialog_Loading loading = new Dialog_Loading();

        myDB = new Database(getActivity());

        final String appName = getActivity().getBaseContext().getResources().getString(R.string.app_name);
        Button importDBButton = (Button) view.findViewById(R.id.dialog_ie_button_db_import);
        Button exportDBButton = (Button) view.findViewById(R.id.dialog_ie_button_db_export);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.dialog_ie_radio_group);
        Button importTableButton = (Button) view.findViewById(R.id.dialog_ie_button_folder_import);
        Button exportTableButton = (Button) view.findViewById(R.id.dialog_ie_button_folder_export);
        final boolean EXPORT = true;
        final boolean IMPORT = false;

        importDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.show(getFragmentManager().beginTransaction(), "Dialog_Loading");

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (FileManagement.importExportDB(IMPORT, getActivity().getPackageName(), appName)) {
                            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(view, R.string.backup_error, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        loading.dismiss();
                    }
                }).start();
            }
        });

        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show(getFragmentManager().beginTransaction(), "Dialog_Loading");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (FileManagement.importExportDB(EXPORT, getActivity().getPackageName(), appName)) {
                            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        loading.dismiss();
                    }
                }).start();
            }
        });

        importTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show(getFragmentManager().beginTransaction(), "Dialog_Loading");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (FileManagement.importTXT(fileName, tableName, appName, myDB)) {
                            Snackbar snackbarInfo = Snackbar.make(view, getResources().getString(R.string.updated) + FileManagement.getUpdated() + getResources().getString(R.string.created) + FileManagement.getCreated(), Snackbar.LENGTH_SHORT);
                            snackbarInfo.show();
                        } else {
                            Snackbar snackbarInfo = Snackbar.make(view, R.string.file_does_not_exists, Snackbar.LENGTH_SHORT);
                            snackbarInfo.show();
                        }
                        loading.dismiss();
                    }
                }).start();
            }
        });

        exportTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show(getFragmentManager().beginTransaction(), "Dialog_Loading");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (FileManagement.generateTXT(fileName, tableName, appName, myDB)) {
                            Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            loading.dismiss();
                        }
                    }
                }).start();
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
                    case (R.id.dialog_ie_radio_catalogs):
                        fileName = "Catalogs.txt";
                        tableName = Database.TABLE_CATALOGS;
                        break;
                }
            }
        });


        return view;
    }
}
