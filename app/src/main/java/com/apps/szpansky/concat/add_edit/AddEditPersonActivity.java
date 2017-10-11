package com.apps.szpansky.concat.add_edit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AddEditPersonActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 12;

    AdView mAdView;

    private EditText name;
    private EditText surname;
    private EditText address;
    private EditText phone;
    private FloatingActionButton add, openContacts;

    private Integer thisId = 0;
    private Boolean isEdit = false;

    private Database myDB;
    private Bundle bundle;
    private Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_person);
        setAds();

        myDB = new Database(this);
        bundle = getIntent().getExtras();

        name = (EditText) findViewById(R.id.add_edit_personName);
        surname = (EditText) findViewById(R.id.add_edit_personSurname);
        address = (EditText) findViewById(R.id.add_edit_personAddress);
        phone = (EditText) findViewById(R.id.add_edit_personPhone);
        add = (FloatingActionButton) findViewById(R.id.add_edit_person_fab);
        openContacts = (FloatingActionButton) findViewById(R.id.openContacts);

        if (bundle != null) {

            thisId = bundle.getInt("personId");
            isEdit = bundle.getBoolean("isEdit");
        }

        if (isEdit) {

            name.setText(getPersonInfo(1));
            surname.setText(getPersonInfo(2));
            address.setText(getPersonInfo(3));
            phone.setText(getPersonInfo(4));
        }
        onAddClick();
        pickContact();
    }


    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adViewPerson);
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
        boolean isUpdated = myDB.updateRowPerson(thisId,
                name.getText().toString(),
                surname.getText().toString(),
                address.getText().toString(),
                phone.getText().toString());
        if (isUpdated) {
            Toast.makeText(AddEditPersonActivity.this, R.string.edit_client_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditPersonActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();


    }


    private void addData() {
        boolean isInserted = myDB.insertDataToPersons(
                name.getText().toString(),
                surname.getText().toString(),
                address.getText().toString(),
                phone.getText().toString());
        if (isInserted) {
            Toast.makeText(AddEditPersonActivity.this, R.string.add_client_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditPersonActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();
    }


    private String getPersonInfo(int columnIndex) {
        Cursor cursor = myDB.getRows(Database.TABLE_PERSONS, Database.PERSON_ID, thisId);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }

    }


    public void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String dialerNo = null;
            String dialerName = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            dialerNo = cursor.getString(phoneIndex);
            dialerName = cursor.getString(nameIndex);

            String[] contactData = dialerName.split(" ");

            if (contactData.length == 1) {
                name.setText(contactData[0]);
            }else if (contactData.length > 1) {
                name.setText(contactData[0]);
                surname.setText(contactData[1]);
            }

            phone.setText(dialerNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pickContact() {
        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickIntent, RESULT_PICK_CONTACT);
            }
        });

    }
}
