package com.apps.szpansky.concat.add_edit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class AddEditCatalogActivity extends AppCompatActivity {

    AdView mAdView;

    private EditText catalogNumber;
    private EditText catalogDateEnd;
    private EditText catalogDateStart;
    private FloatingActionButton add;

    private Integer thisId = 0;
    private Boolean isEdit = false;

    private Database myDB;
    private Bundle bundle;

    private Integer year_x, month_x, day_x;

    static final int DIALOG_ID_START_DATE = 0;
    static final int DIALOG_ID_END_DATE = 1;


    public AddEditCatalogActivity() {
        Calendar calendar = Calendar.getInstance();
        this.year_x = calendar.get(Calendar.YEAR);
        this.month_x = calendar.get(Calendar.MONTH);
        this.day_x = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_catalog);
        setAds();

        myDB = new Database(this);
        bundle = getIntent().getExtras();

        catalogNumber = (EditText) findViewById(R.id.add_edit_catalogNumber);
        String catalogNumberDefText = getResources().getString(R.string.order_number)+":";
        catalogNumber.setText(catalogNumberDefText);
        catalogDateStart = (EditText) findViewById(R.id.add_edit_catalogDateStart);
        catalogDateEnd = (EditText) findViewById(R.id.add_edit_catalogDateEnd);

        add = (FloatingActionButton) findViewById(R.id.add_edit_catalog_fab);

        if (bundle != null) {
            thisId = bundle.getInt("catalogId");
            isEdit = bundle.getBoolean("isEdit");
        }

        if (isEdit) {
            catalogNumber.setText(getCatalogInfo(1));
            catalogDateStart.setText(getCatalogInfo(2));
            catalogDateEnd.setText(getCatalogInfo(3));

           // catalogNumber.setFocusable(false);
        }

        showDialogOnDateClick();

        onAddClick();
    }


    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adViewCatalog);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void onAddClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    updateData();
                } else {
                    addData();
                }
            }
        });
    }


    private void updateData() {
        boolean isUpdated = myDB.updateRowCatalog(thisId,
                catalogNumber.getText().toString(),
                catalogDateStart.getText().toString(),
                catalogDateEnd.getText().toString());
        if (isUpdated) {
            Toast.makeText(AddEditCatalogActivity.this, R.string.edit_catalog_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditCatalogActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();
    }


    private void addData() {
        boolean isInserted = myDB.insertDataToCatalogs(
                catalogNumber.getText().toString(),
                catalogDateStart.getText().toString(),
                catalogDateEnd.getText().toString());
        if (isInserted) {
            Toast.makeText(AddEditCatalogActivity.this, R.string.add_catalog_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditCatalogActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();
    }


    private String getCatalogInfo(int columnIndex) {
        Cursor cursor = myDB.getRows(Database.TABLE_CATALOGS, Database.CATALOG_ID, thisId);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, dateStart, year_x, month_x, day_x);
            case 1:
                return new DatePickerDialog(this, dateEnd, year_x, month_x, day_x);
            default:
                return null;
        }
    }


    private DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            String monthString;
            monthString = SimpleFunctions.fillWithZeros(month_x.toString(), 2);

            day_x = dayOfMonth;
            String dayString;
            dayString = SimpleFunctions.fillWithZeros(day_x.toString(), 2);

            catalogDateStart.setText(year_x + "-" + monthString + "-" + dayString);
        }
    };


    private DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            String monthString;
            monthString = SimpleFunctions.fillWithZeros(month_x.toString(), 2);

            day_x = dayOfMonth;
            String dayString;
            dayString = SimpleFunctions.fillWithZeros(day_x.toString(), 2);

            catalogDateEnd.setText(year_x + "-" + monthString + "-" + dayString);
        }
    };


    public void showDialogOnDateClick() {
        catalogDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_START_DATE);
            }
        });

        catalogDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_END_DATE);
            }
        });
    }
}
