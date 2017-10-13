package com.apps.szpansky.concat.open_all;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditItem;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OpenAllItemsActivity extends SimpleActivity implements DialogInterface.OnDismissListener {


    public OpenAllItemsActivity() {
        super(new Item(), "list_preference_open_all_colors");
    }

    boolean flag = true;


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
                Dialog_AddEditItem addEditItem = Dialog_AddEditItem.newInstance();
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");

            }
        });
    }


    public void listViewItemClick() {
        flag = true;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;
                data.setClickedItemId(id);
                Dialog_AddEditItem addEditItem = Dialog_AddEditItem.newInstance(id);
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    data.setClickedItemId(id);
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
    public void onDismiss(DialogInterface dialog) {
        refreshListView();
    }


   /* private void setAds() {
        mAdView = (AdView) findViewById(R.id.adViewItem);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }*/
}
