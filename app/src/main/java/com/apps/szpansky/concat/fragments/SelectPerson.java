package com.apps.szpansky.concat.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;


public class SelectPerson extends SimpleFragment {


    ClickedPerson clickedPerson;


    public interface ClickedPerson {
        void onPersonPick(Long id);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickedPerson = (ClickedPerson) context;
    }


    public static SelectPerson newInstance(Data data, String styleKey) {
        SelectPerson selectPerson = new SelectPerson();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        selectPerson.setArguments(bundle);
        return selectPerson;
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEdit_Person addEditPerson = AddEdit_Person.newInstance();
                addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });

    }

    @Override
    public void onListViewClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);

                AddEdit_Person addEditPerson = AddEdit_Person.newInstance(id);
                addEditPerson.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditPerson");

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                clickedPerson.onPersonPick(id);
            }
        });
    }

}
