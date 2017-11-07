package com.apps.szpansky.concat.dialog_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.DialogFragment;
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

    private static final int MY_REQUEST_CONTACTS = 13;
    boolean isEdit;
    EditText name, surname, address, phone;
    FloatingActionButton add, openContacts;
    private final int RESULT_PICK_CONTACT = 12;
    Person person;


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
        person = new Person(new Database(getActivity()));

        isEdit = getArguments().getBoolean("isEdit");

        final View view = inflater.inflate(R.layout.dialog_add_edit_person, container, false);

        name = view.findViewById(R.id.add_edit_personName);
        surname = view.findViewById(R.id.add_edit_personSurname);
        address = view.findViewById(R.id.add_edit_personAddress);
        phone = view.findViewById(R.id.add_edit_personPhone);
        add = view.findViewById(R.id.add_edit_person_fab);
        openContacts = view.findViewById(R.id.openContacts);

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


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                        Intent contactPickIntent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(contactPickIntent, RESULT_PICK_CONTACT);
                    }else {

                        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                            Toast.makeText(getActivity(),R.string.permissions_contact_info,
                                    Toast.LENGTH_LONG).show();
                        }

                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},MY_REQUEST_CONTACTS);

                    }
                }else {
                    //normal permissions
                    Intent contactPickIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickIntent, RESULT_PICK_CONTACT);

                }

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
        super.onActivityResult(requestCode,resultCode,data);
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


    public void contactPicked(Intent contactIntent) {
        Cursor cursor = null;
        try {
            String dialerNo = null;
            String dialerName = null;
            Uri uri = contactIntent.getData();
            if(uri != null) cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
