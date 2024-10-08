package com.apps.szpansky.concat.dialog_fragments;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class AddEdit_Catalog extends DialogFragment implements DialogInterface.OnDismissListener {


    EditText catalogNumber, catalogDateStart, catalogDateEnd;
    FloatingActionButton add;
    boolean isEdit;
    Catalog catalog;

    public static AddEdit_Catalog newInstance(long myIndex) {
        AddEdit_Catalog addEditCatalog = new AddEdit_Catalog();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", true);
        bundle.putLong("myIndex", myIndex);
        addEditCatalog.setArguments(bundle);
        addEditCatalog.setStyle(STYLE_NO_TITLE, 0);

        return addEditCatalog;
    }


    public static AddEdit_Catalog newInstance() {
        AddEdit_Catalog addEditCatalog = new AddEdit_Catalog();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", false);
        addEditCatalog.setArguments(bundle);
        addEditCatalog.setStyle(STYLE_NO_TITLE, 0);

        return addEditCatalog;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        catalog = new Catalog(new Database(getActivity()));

        isEdit = getArguments().getBoolean("isEdit");

        final View view = inflater.inflate(R.layout.dialog_add_edit_catalog, container, false);

        catalogNumber = view.findViewById(R.id.add_edit_catalogNumber);
        catalogDateStart = view.findViewById(R.id.add_edit_catalogDateStart);
        catalogDateEnd = view.findViewById(R.id.add_edit_catalogDateEnd);
        add = view.findViewById(R.id.add_edit_catalog_fab);

        final String[] currentDate = SimpleFunctions.getCurrentDate().split("-");

        catalogDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), pickStartDate, Integer.valueOf(currentDate[0]), Integer.valueOf(currentDate[1]) - 1, Integer.valueOf(currentDate[2])).show();
            }
        });

        catalogDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), pickEndDate, Integer.valueOf(currentDate[0]), Integer.valueOf(currentDate[1]) - 1, Integer.valueOf(currentDate[2])).show();
            }
        });

        if (isEdit) {
            catalog.setClickedItemId(getArguments().getLong("myIndex"));
            String[] currentValues = catalog.getClickedItemData();
            catalogNumber.setText(currentValues[0]);
            catalogDateStart.setText(currentValues[1]);
            catalogDateEnd.setText(currentValues[2]);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] keys = new String[]{
                        Database.CATALOG_NUMBER,
                        Database.CATALOG_DATE_START,
                        Database.CATALOG_DATE_ENDS};

                String[] value = new String[]{
                        catalogNumber.getText().toString(),
                        catalogDateStart.getText().toString(),
                        catalogDateEnd.getText().toString()};

                if (isEdit) {
                    if (catalog.updateData(value, keys)) {
                        Toast.makeText(getActivity().getBaseContext(), R.string.edit_catalog_notify, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }

                } else {
                    if (catalog.insertData(value, keys)) {
                        Toast.makeText(getActivity().getBaseContext(), R.string.add_catalog_notify, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify_duplicate, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }


    final DatePickerDialog.OnDateSetListener pickStartDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthString = SimpleFunctions.fillWithZeros(Integer.toString(month + 1), 2);
            String dayString = SimpleFunctions.fillWithZeros(Integer.toString(dayOfMonth), 2);
            catalogDateStart.setText(year + "-" + monthString + "-" + dayString);
        }
    };


    final DatePickerDialog.OnDateSetListener pickEndDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthString = SimpleFunctions.fillWithZeros(Integer.toString(month + 1), 2);
            String dayString = SimpleFunctions.fillWithZeros(Integer.toString(dayOfMonth), 2);
            catalogDateEnd.setText(year + "-" + monthString + "-" + dayString);
        }
    };
}
