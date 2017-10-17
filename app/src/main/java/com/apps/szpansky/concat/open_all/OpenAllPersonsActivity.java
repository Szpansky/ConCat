package com.apps.szpansky.concat.open_all;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditPerson;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OpenAllPersonsActivity extends SimpleActivity {

    public OpenAllPersonsActivity() {
        super(new Person(), "list_preference_open_all_colors");
    }


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


    private void showDialog(long id){
        Dialog_AddEditPerson addEditPerson = Dialog_AddEditPerson.newInstance(id);
        addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditPerson");
    }


    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                data.setClickedItemId(id);
                showDialog(id);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                data.setClickedItemId(id);
                showDialog(id);
            }
        });
    }


    @Override
    public void onBackPressed() {
        onNavigateUp();
    }

}