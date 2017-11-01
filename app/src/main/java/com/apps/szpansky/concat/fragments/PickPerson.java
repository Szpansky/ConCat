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
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class PickPerson extends SimpleFragmentWithList {

    ClickedPerson clickedPerson;

    public interface ClickedPerson {
        void onPersonPick(Long id);
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


    public static PickPerson newInstance(Data data) {
        PickPerson pickPerson = new PickPerson();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);

        pickPerson.setArguments(bundle);
        return pickPerson;
    }


    @Override
    protected String selectStyleKey() {
        String openAllColor = "list_preference_open_all_colors";
        return openAllColor;
    }


    @Override
    protected void inflateFABView(FloatingActionButton addButton) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEdit_Person addEditPerson = AddEdit_Person.newInstance();
                addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
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
        addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditPerson");
    }


    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
    }
}
