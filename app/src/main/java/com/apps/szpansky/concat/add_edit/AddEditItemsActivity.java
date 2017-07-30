package com.apps.szpansky.concat.add_edit;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class AddEditItemsActivity extends AppCompatActivity {

    AdView mAdView;

    private EditText nr, price, name;

    private CheckBox dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100;

    private CheckBox[] dis_all = {dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100};

    private FloatingActionButton add;

    private Integer thisId = 0;
    private String discount;
    private Boolean isEdit = false;


    private Database myDB;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_items);

        myDB = new Database(this);
        bundle = getIntent().getExtras();

        nr = (EditText) findViewById(R.id.add_edit_itemNr);
        name = (EditText) findViewById(R.id.add_edit_itemName);
        price = (EditText) findViewById(R.id.add_edit_itemPrice);

        dis_all[0] = (CheckBox) findViewById(R.id.check_5);
        dis_all[1] = (CheckBox) findViewById(R.id.check_10);
        dis_all[2] = (CheckBox) findViewById(R.id.check_15);
        dis_all[3] = (CheckBox) findViewById(R.id.check_20);
        dis_all[4] = (CheckBox) findViewById(R.id.check_25);
        dis_all[5] = (CheckBox) findViewById(R.id.check_30);
        dis_all[6] = (CheckBox) findViewById(R.id.check_35);
        dis_all[7] = (CheckBox) findViewById(R.id.check_40);
        dis_all[8] = (CheckBox) findViewById(R.id.check_100);

        add = (FloatingActionButton) findViewById(R.id.add_edit_item_fab);

        if (bundle != null) {
            thisId = bundle.getInt("itemId");
            isEdit = bundle.getBoolean("isEdit");
        }

        if (isEdit) {
            nr.setText(getItemInfo(1));
            name.setText(getItemInfo(2));
            price.setText(getItemInfo(3));
            discount = getItemInfo(4);

            for (int i = 0; i < discount.length(); i++) {
                int discountRevert = discount.length() - 1 - i;
                if (discount.charAt((discountRevert)) == '1') dis_all[i].setChecked(true);
            }
            nr.setFocusable(false);
        }
        onAddClick();

        setAds();
    }


    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adViewItem);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void onAddClick() {
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
                if (isEdit) {
                    updateData();
                } else {
                    addData();
                }
            }
        });
    }


    private void updateData() {
        boolean isUpdated = myDB.updateRowItem(thisId,
                name.getText().toString(),
                price.getText().toString(),
                discount);
        if (isUpdated) {
            Toast.makeText(AddEditItemsActivity.this, R.string.edit_item_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditItemsActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();
    }


    private void addData() {
        String number = nr.getText().toString();

        number = SimpleFunctions.fillWithZeros(number, 5);

        boolean isInserted = myDB.insertDataToItems(
                nr.getText().toString(),
                number,
                name.getText().toString(),
                price.getText().toString(),
                discount.toString());
        if (isInserted) {
            Toast.makeText(AddEditItemsActivity.this, R.string.add_item_notify, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddEditItemsActivity.this, R.string.error_notify, Toast.LENGTH_LONG).show();
        }
        finish();
    }


    private String getItemInfo(int columnIndex) {
        Cursor cursor = myDB.getRows(Database.TABLE_ITEMS, Database.ITEM_ID, thisId);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}
