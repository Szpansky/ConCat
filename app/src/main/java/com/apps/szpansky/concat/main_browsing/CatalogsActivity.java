package com.apps.szpansky.concat.main_browsing;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import java.util.Calendar;


public class CatalogsActivity extends SimpleActivity {

    private boolean flag = true;
    private EditText catalogDateStart;
    private EditText catalogDateEnd;


    public CatalogsActivity() {
        super(new Catalog(), "list_preference_browsing_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fiber_new_white_24dp));
        listViewItemClick();
    }


    @Override
    public void onBackPressed() {
        onNavigateUp();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEdit_CatalogDialog(false);
            }
        });
    }


    private void addEdit_CatalogDialog(final boolean isEdit) {

        final Calendar calendar = Calendar.getInstance();
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        final View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_catalog, null);
        final EditText catalogNumber = (EditText) view.findViewById(R.id.add_edit_catalogNumber);
        catalogDateStart = (EditText) view.findViewById(R.id.add_edit_catalogDateStart);
        catalogDateEnd = (EditText) view.findViewById(R.id.add_edit_catalogDateEnd);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_edit_catalog_fab);

        catalogDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CatalogsActivity.this, pickStartDate, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        catalogDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CatalogsActivity.this, pickEndDate,  calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (isEdit) {
            String[] currentValues = data.getClickedData();
            catalogNumber.setText(currentValues[0]);
            catalogDateStart.setText(currentValues[1]);
            catalogDateEnd.setText(currentValues[2]);
        }

        fab.setOnClickListener(new View.OnClickListener() {
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

                boolean flag;
                if (isEdit) flag = data.updateData(value, keys);
                else
                    flag = data.insertData(value,keys);
                if (flag) {
                    Toast.makeText(getBaseContext(), getString(R.string.add_catalog_notify) + "/" + getString(R.string.edit_catalog_notify), Toast.LENGTH_SHORT).show();
                    refreshListView();
                } else {
                    Toast.makeText(getBaseContext(), R.string.error_notify, Toast.LENGTH_LONG).show();
                }
                builder.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }


    private void listViewItemClick() {
        flag = true;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                flag = false;
                addEdit_CatalogDialog(true);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                if (flag) {
                    Intent intent = new Intent(CatalogsActivity.this, ClientsActivity.class);
                    Client.clickedCatalogId = id;         //to know which catalog is opened is next activity
                    startActivity(intent);
                }
                flag = true;
            }
        });
    }


    final DatePickerDialog.OnDateSetListener pickStartDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthString = SimpleFunctions.fillWithZeros(Integer.toString(month+1), 2);
            String dayString = SimpleFunctions.fillWithZeros(Integer.toString(dayOfMonth), 2);
            catalogDateStart.setText(year + "-" + monthString + "-" + dayString);
        }
    };


    final DatePickerDialog.OnDateSetListener pickEndDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthString = SimpleFunctions.fillWithZeros(Integer.toString(month+1), 2);
            String dayString = SimpleFunctions.fillWithZeros(Integer.toString(dayOfMonth), 2);
            catalogDateEnd.setText(year + "-" + monthString + "-" + dayString);
        }
    };
}
