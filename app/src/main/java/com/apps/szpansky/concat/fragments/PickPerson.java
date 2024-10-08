package com.apps.szpansky.concat.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.simple_data.Person_InPickList;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class PickPerson extends SimpleFragmentWithList {

    ClickedPerson clickedPerson;

    public interface ClickedPerson {
        void onPersonPick(Long id);
    }


    public static PickPerson newInstance() {
        PickPerson pickPerson = new PickPerson();
        return pickPerson;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickedPerson = (ClickedPerson) context;
    }

    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setTitle(R.string.pick_person);

    }


    @Override
    protected String selectStyleKey() {
        return "list_preference_open_all_colors";
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEdit_Person addEditPerson = AddEdit_Person.newInstance();

                if (getActivity().getSupportFragmentManager().findFragmentByTag("DialogAddEditPerson") == null) {
                    getActivity().getSupportFragmentManager().beginTransaction().add(addEditPerson, "DialogAddEditPerson").commit();
                }
            }
        });
    }


    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        clickedPerson.onPersonPick(id);
    }


    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        AddEdit_Person addEditPerson = AddEdit_Person.newInstance(id);

        if (getActivity().getSupportFragmentManager().findFragmentByTag("DialogAddEditPerson") == null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(addEditPerson, "DialogAddEditPerson").commit();
        }
    }


    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
    }


    @Override
    protected Data setDataObject() {
        return new Person_InPickList(new Database(getActivity().getBaseContext()));
    }
}
