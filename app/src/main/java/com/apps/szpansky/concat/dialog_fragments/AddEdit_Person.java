package com.apps.szpansky.concat.dialog_fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.Database;

import static android.app.Activity.RESULT_OK;


public class AddEdit_Person extends DialogFragment {

    Person person = new Person(new Database(getActivity()));
    boolean isEdit;
    EditText name, surname, address, phone;
    FloatingActionButton add, openContacts;
    private final int RESULT_PICK_CONTACT = 12;


    public static AddEdit_Person newInstance(long myIndex) {
        AddEdit_Person _addEditPerson = new AddEdit_Person();

        Bundle bundle = new Bundle();
        bundle.putLong("myIndex", myIndex);
        bundle.putBoolean("isEdit", true);
        _addEditPerson.setArguments(bundle);
        _addEditPerson.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return _addEditPerson;
    }

    public static AddEdit_Person newInstance() {
        AddEdit_Person _addEditPerson = new AddEdit_Person();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", false);
        _addEditPerson.setArguments(bundle);
        _addEditPerson.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return _addEditPerson;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        isEdit = getArguments().getBoolean("isEdit");
        person.setDatabase(new Database(getActivity()));

        final View view = inflater.inflate(R.layout.dialog_add_edit_person, null);

        name = (EditText) view.findViewById(R.id.add_edit_personName);
        surname = (EditText) view.findViewById(R.id.add_edit_personSurname);
        address = (EditText) view.findViewById(R.id.add_edit_personAddress);
        phone = (EditText) view.findViewById(R.id.add_edit_personPhone);
        add = (FloatingActionButton) view.findViewById(R.id.add_edit_person_fab);
        openContacts = (FloatingActionButton) view.findViewById(R.id.openContacts);

        if (isEdit) {
            loadPreviousData();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] value;
                String[] keys;

                if (isEdit) {
                    Long test = person.getClickedItemId();

                    value = new String[]{
                            test.toString(),
                            name.getText().toString(),
                            surname.getText().toString(),
                            address.getText().toString(),
                            phone.getText().toString()};

                    keys = new String[]{
                            Database.PERSON_ID,
                            Database.PERSON_NAME,
                            Database.PERSON_SURNAME,
                            Database.PERSON_ADDRESS,
                            Database.PERSON_PHONE};

                    if (person.updateData(value, keys)) {
                        Toast.makeText(getActivity().getBaseContext(), R.string.edit_client_notify, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }

                } else {

                    value = new String[]{
                            name.getText().toString(),
                            surname.getText().toString(),
                            address.getText().toString(),
                            phone.getText().toString()};

                    keys = new String[]{
                            Database.PERSON_NAME,
                            Database.PERSON_SURNAME,
                            Database.PERSON_ADDRESS,
                            Database.PERSON_PHONE};

                    if (person.insertData(value, keys)) {
                        Toast.makeText(getActivity().getBaseContext(), R.string.add_client_notify, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }
                }
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

        return view;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }


    private void loadPreviousData() {
        person.setClickedItemId(getArguments().getLong("myIndex"));
        String[] currentValue = person.getClickedItemData();
        name.setText(currentValue[0]);
        surname.setText(currentValue[1]);
        address.setText(currentValue[2]);
        phone.setText(currentValue[3]);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            dialerNo = cursor.getString(phoneIndex);
            dialerName = cursor.getString(nameIndex);

            cursor.close();
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
