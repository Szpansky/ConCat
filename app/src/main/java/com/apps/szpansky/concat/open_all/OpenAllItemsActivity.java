package com.apps.szpansky.concat.open_all;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;
import com.apps.szpansky.concat.tools.SimpleFunctions;

import java.util.Calendar;

public class OpenAllItemsActivity extends SimpleActivity {


    public OpenAllItemsActivity() {
        super(new Item(),"list_preference_open_all_colors");
    }

    String discount;
    CheckBox dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100;
    final CheckBox[] dis_all = {dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100};
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
                addEdit_ItemDialog(false);
            }
        });
    }


    private void addEdit_ItemDialog(final boolean isEdit) {

        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_item, null);
        final EditText nr, price, name;

        FloatingActionButton add;

        nr = (EditText) view.findViewById(R.id.add_edit_itemNr);
        name = (EditText) view.findViewById(R.id.add_edit_itemName);
        price = (EditText) view.findViewById(R.id.add_edit_itemPrice);

        dis_all[0] = (CheckBox) view.findViewById(R.id.check_5);
        dis_all[1] = (CheckBox) view.findViewById(R.id.check_10);
        dis_all[2] = (CheckBox) view.findViewById(R.id.check_15);
        dis_all[3] = (CheckBox) view.findViewById(R.id.check_20);
        dis_all[4] = (CheckBox) view.findViewById(R.id.check_25);
        dis_all[5] = (CheckBox) view.findViewById(R.id.check_30);
        dis_all[6] = (CheckBox) view.findViewById(R.id.check_35);
        dis_all[7] = (CheckBox) view.findViewById(R.id.check_40);
        dis_all[8] = (CheckBox) view.findViewById(R.id.check_100);

        add = (FloatingActionButton) view.findViewById(R.id.add_edit_item_fab);

        if (isEdit) {
            String[] currentValues = data.getClickedData();
            nr.setText(currentValues[0]);
            name.setText(currentValues[1]);
            price.setText(currentValues[2]);
            discount = currentValues[3];
            for (int i = 0; i < discount.length(); i++) {
                int discountRevert = discount.length() - 1 - i;
                if (discount.charAt((discountRevert)) == '1') dis_all[i].setChecked(true);
            }
            nr.setFocusable(false);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount = "";
                for (int i = 0; i <= 8; i++) {
                    if (dis_all[i].isChecked()) {
                        discount = "1" + discount;
                    } else {
                        discount = "0" + discount;
                    }
                }

                String number = SimpleFunctions.fillWithZeros(nr.getText().toString(),5);
                String[] value;
                String[] keys;
                if(isEdit){
                    Calendar calendar = Calendar.getInstance();
                    Integer year_x = calendar.get(Calendar.YEAR);
                    Integer month_x = calendar.get(Calendar.MONTH) + 1;
                    Integer day_x = calendar.get(Calendar.DAY_OF_MONTH);
                    String day = day_x.toString();
                    day = SimpleFunctions.fillWithZeros(day, 2);
                    String month = month_x.toString();
                    month = SimpleFunctions.fillWithZeros(month, 2);
                    String thisDate = year_x + "-" + month + "-" + day;

                    value = new String[]{
                            nr.getText().toString(),
                            number,
                            name.getText().toString(),
                            price.getText().toString(),
                            discount,
                            thisDate};

                    keys = new String[]{
                            Database.ITEM_ID,
                            Database.ITEM_NUMBER,
                            Database.ITEM_NAME,
                            Database.ITEM_PRICE,
                            Database.ITEM_DISCOUNT,
                            Database.ITEM_UPDATE_DATE};

                }else {
                    value = new String[]{
                            nr.getText().toString(),
                            number,
                            name.getText().toString(),
                            price.getText().toString(),
                            discount};

                    keys = new String[]{
                            Database.ITEM_ID,
                            Database.ITEM_NUMBER,
                            Database.ITEM_NAME,
                            Database.ITEM_PRICE,
                            Database.ITEM_DISCOUNT};
                }

                boolean flag;
                if (isEdit) flag = data.updateData(value, keys);
                else
                    flag = data.insertData(value, keys);
                if (flag) {
                    Toast.makeText(getBaseContext(), getString(R.string.add_item_notify) + "/" + getString(R.string.edit_item_notify), Toast.LENGTH_SHORT).show();
                    refreshListView();
                } else {
                    Toast.makeText(getBaseContext(), R.string.error_notify, Toast.LENGTH_LONG).show();
                }
                builder.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }


    public void listViewItemClick() {
        flag = true;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;
                data.setClickedItemId(id);

                addEdit_ItemDialog(true);
                return false;
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


   /* private void setAds() {
        mAdView = (AdView) findViewById(R.id.adViewItem);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }*/
}
