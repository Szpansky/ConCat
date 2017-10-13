package com.apps.szpansky.concat.open_all;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OpenAllPersonsActivity extends SimpleActivity {


    public OpenAllPersonsActivity() {
        super(new Person(), "list_preference_open_all_colors");
    }

    boolean flag = true;
    private static final int RESULT_PICK_CONTACT = 12;
    EditText name, surname, address, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fiber_new_white_24dp));

        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addEdit_PersonDialog(false);
            }
        });
    }


    private void addEdit_PersonDialog(final boolean isEdit) {
        final AlertDialog builder = new AlertDialog.Builder(this).create();


        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_person, null);
        name = (EditText) view.findViewById(R.id.add_edit_personName);
        surname = (EditText) view.findViewById(R.id.add_edit_personSurname);
        address = (EditText) view.findViewById(R.id.add_edit_personAddress);
        phone = (EditText) view.findViewById(R.id.add_edit_personPhone);
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_edit_person_fab);
        FloatingActionButton openContacts = (FloatingActionButton) view.findViewById(R.id.openContacts);

        if (isEdit) {
            String[] currentValue = data.getClickedItemData();
            name.setText(currentValue[0]);
            surname.setText(currentValue[1]);
            address.setText(currentValue[2]);
            phone.setText(currentValue[3]);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    Long test = data.getClickedItemId();

                    String[] values = new String[]{
                            test.toString(),
                            name.getText().toString(),
                            surname.getText().toString(),
                            address.getText().toString(),
                            phone.getText().toString()};

                    String[] keys = new String[]{
                            Database.PERSON_ID,
                            Database.PERSON_NAME,
                            Database.PERSON_SURNAME,
                            Database.PERSON_ADDRESS,
                            Database.PERSON_PHONE};

                    boolean isUpdated = data.updateData(values,keys);

                    if (isUpdated) {
                        Toast.makeText(getBaseContext(), R.string.edit_client_notify, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.error_notify, Toast.LENGTH_LONG).show();
                    }

                } else {

                    String[] values = new String[]{
                            name.getText().toString(),
                            surname.getText().toString(),
                            address.getText().toString(),
                            phone.getText().toString()};

                    String[] keys = new String[]{
                            Database.PERSON_NAME,
                            Database.PERSON_SURNAME,
                            Database.PERSON_ADDRESS,
                            Database.PERSON_PHONE};

                    boolean isInserted = data.insertData(values,keys);

                    if (isInserted) {
                        Toast.makeText(getBaseContext(), R.string.add_client_notify, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.error_notify, Toast.LENGTH_LONG).show();
                    }

                }
                refreshListView();
                builder.dismiss();
            }
        });

        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickIntent, RESULT_PICK_CONTACT);
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
                addEdit_PersonDialog(true);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                if (flag) {

                }
                flag = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        onNavigateUp();
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
}