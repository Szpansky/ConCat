package com.apps.szpansky.concat.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragmentWithList;


public class OpenPersons extends SimpleFragmentWithList {


    public static OpenPersons newInstance(Data data) {
        OpenPersons openPersons = new OpenPersons();
        String openAllColor = "list_preference_open_all_colors";

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", openAllColor);

        openPersons.setArguments(bundle);
        return openPersons;
    }


    @Override
    protected void onAddButtonClick() {
        AddEdit_Person addEditPerson = AddEdit_Person.newInstance();
        addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditPerson");
    }

    @Override
    protected void onListViewClick(long id) {
        getDataObject().setClickedItemId(id);
        showDialog(id);
    }

    @Override
    protected void onListViewLongClick(long id) {
        getDataObject().setClickedItemId(id);
        showDialog(id);
    }


    @Override
    protected void inflateNewViewInToolBar(Toolbar toolbar) {
        toolbar.setTitle(R.string.persons);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_menu_black_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().openOptionsMenu();
            }
        });
    }


    private void showDialog(long id) {
        AddEdit_Person addEditPerson = AddEdit_Person.newInstance(id);
        addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditPerson");
    }

    @Override
    protected Drawable setFABImage() {
        return (ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
    }

}
