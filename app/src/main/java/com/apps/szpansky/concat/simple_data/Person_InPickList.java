package com.apps.szpansky.concat.simple_data;

import com.apps.szpansky.concat.R;

import static com.apps.szpansky.concat.tools.Database.*;



public class Person_InPickList extends Person {

    @Override
    public int getItemLayoutResourceId() {
        return R.layout.item_personinlist_view;
    }


    public Person_InPickList() {
        super();
    }

    public Person_InPickList(String title) {
        super();
        setTitle(title);
    }


    @Override
    public int[] getToViewIDs() {

        return (new int[]{
                R.id.item_personName,
                R.id.item_personSurname,
                R.id.item_personAddress
        });
    }


    @Override
    public String[] getFromFieldsNames() {

        return (new String[]{
                PERSON_NAME,
                PERSON_SURNAME,
                PERSON_ADDRESS
        });
    }
}
