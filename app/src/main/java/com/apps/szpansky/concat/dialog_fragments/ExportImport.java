package com.apps.szpansky.concat.dialog_fragments;

import android.app.Activity;
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

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.FileManagement;


public class ExportImport extends DialogFragment implements DialogInterface.OnDismissListener {

    private String tableName = Database.TABLE_ITEMS;    //default exported/imported content
    private String fileName = "Items.txt";
    private Database myDB;
    View view;
    final boolean EXPORT = true;
    final boolean IMPORT = false;
    String appName;


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
                new AsyncImportDB().execute();
            }
        });

        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncExportDB().execute();
            }
        });

        importTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncImportTXT().execute();
            }
        });

        exportTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncExportTXT().execute();
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


    private class AsyncImportDB extends AsyncTask<Void, Void, Void> {
        Loading loading = Loading.newInstance();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (FileManagement.importExportDB(IMPORT, getActivity().getPackageName(), appName)) {
                Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(view, R.string.backup_error, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!loading.isAdded())
            getActivity().getSupportFragmentManager().beginTransaction().add(loading, "Loading").commit();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loading.isVisible()) loading.dismiss();
        }
    }


    private class AsyncExportDB extends AsyncTask<Void, Void, Void> {
        Loading loading = Loading.newInstance();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (FileManagement.importExportDB(EXPORT, getActivity().getPackageName(), appName)) {
                Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!loading.isAdded())
                getActivity().getSupportFragmentManager().beginTransaction().add(loading, "Loading").commit();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loading.isVisible()) loading.dismiss();
        }
    }


    private class AsyncImportTXT extends AsyncTask<Void, Void, Void> {
        Loading loading = Loading.newInstance();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (FileManagement.importTXT(fileName, tableName, appName, myDB)) {
                Snackbar snackbarInfo = Snackbar.make(view, getResources().getString(R.string.updated) + FileManagement.getUpdated() + getResources().getString(R.string.created) + FileManagement.getCreated(), Snackbar.LENGTH_SHORT);
                snackbarInfo.show();
            } else {
                Snackbar snackbarInfo = Snackbar.make(view, R.string.file_does_not_exists, Snackbar.LENGTH_SHORT);
                snackbarInfo.show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!loading.isAdded())
                getActivity().getSupportFragmentManager().beginTransaction().add(loading, "Loading").commit();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loading.isVisible()) loading.dismiss();
        }
    }


    private class AsyncExportTXT extends AsyncTask<Void, Void, Void> {
        Loading loading = Loading.newInstance();
        //Loading loading = (Loading) getActivity().getSupportFragmentManager().findFragmentByTag("Loading");

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (FileManagement.generateTXT(fileName, tableName, appName, myDB)) {
                Snackbar snackbar = Snackbar.make(view, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!loading.isAdded())
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
