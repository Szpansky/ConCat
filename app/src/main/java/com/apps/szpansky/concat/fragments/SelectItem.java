package com.apps.szpansky.concat.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.tools.Data;
import com.apps.szpansky.concat.tools.SimpleFragment;


public class SelectItem extends SimpleFragment {


    ClickedItem clickedPerson;


    public interface ClickedItem {
        void onItemPick(Long id);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickedPerson = (ClickedItem) context;
    }


    public static SelectItem newInstance(Data data, String styleKey) {
        SelectItem selectItem = new SelectItem();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putString("styleKey", styleKey);

        selectItem.setArguments(bundle);
        return selectItem;
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_fiber_new_white_24dp, null));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEdit_Item addEdit_item = AddEdit_Item.newInstance();
                addEdit_item.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });

    }

    @Override
    public void onListViewClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);

                AddEdit_Item addEdit_item = AddEdit_Item.newInstance(id);
                addEdit_item.show(getActivity().getFragmentManager().beginTransaction(), "DialogAddEditCatalog");

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                clickedPerson.onItemPick(id);
            }
        });
    }

}
