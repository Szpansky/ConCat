package com.apps.szpansky.concat.open_all;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditPerson;
import com.apps.szpansky.concat.simple_data.Person;
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
                Dialog_AddEditPerson addEditPerson = Dialog_AddEditPerson.newInstance();
                addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditPerson");
            }
        });
    }


    private void listViewItemClick() {
        flag = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                flag = false;
                Dialog_AddEditPerson addEditPerson = Dialog_AddEditPerson.newInstance(id);
                addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditPerson");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    data.setClickedItemId(id);
                    Dialog_AddEditPerson addEditPerson = Dialog_AddEditPerson.newInstance(id);
                    addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditPerson");
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
            } else if (contactData.length > 1) {
                name.setText(contactData[0]);
                surname.setText(contactData[1]);
            }

            phone.setText(dialerNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}