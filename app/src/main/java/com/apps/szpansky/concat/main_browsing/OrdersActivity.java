package com.apps.szpansky.concat.main_browsing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.for_pick.PickItem;
import com.apps.szpansky.concat.fragments.Dialog_AddItemCount;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class OrdersActivity extends SimpleActivity {

    private final int BACK_CODE = 1;

    public OrdersActivity() {
        super(new Order(), "list_preference_browsing_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(data.getTitle());

        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_playlist_add_white_24dp));

        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, PickItem.class);
                startActivityForResult(intent, BACK_CODE);
            }
        });
    }


    private void showDialog(){
        Dialog_AddItemCount addItemCount = new Dialog_AddItemCount().newInstance((Order) data);
        addItemCount.show(getFragmentManager().beginTransaction(), "Dialog_AddItemCount");
    }


    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                showDialog();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                showDialog();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (requestCode == BACK_CODE) {
            if (resultCode == RESULT_OK) {

                Long itemId = intentData.getLongExtra("itemId", 0);
                Integer itemCount = intentData.getIntExtra("itemCount", 1);

                Long clientId = Order.clickedClientId;

                String[] value = new String[]{clientId.toString(), itemId.toString(), itemCount.toString()};

                if (data.insertData(value, null)) {
                    Toast.makeText(this, R.string.add_item_notify, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,R.string.error_notify_duplicate,Toast.LENGTH_SHORT).show();
                }
                refreshListView();
            }
        }
    }

}
