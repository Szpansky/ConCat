package com.apps.szpansky.concat.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class Dialog_AddEditItem extends DialogFragment {

    private EditText nr, price, name;
    private String discount ="";
    private CheckBox dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100;
    private final CheckBox[] dis_all = {dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100};
    FloatingActionButton add;
    Database myDb;
    final Item item = new Item();


    boolean isEdit;


    public static Dialog_AddEditItem newInstance(long myIndex) {
        Dialog_AddEditItem dialog_addEditItem = new Dialog_AddEditItem();

        //passing args
        Bundle args = new Bundle();
        args.putLong("myIndex", myIndex);
        args.putBoolean("isEdit", true);
        dialog_addEditItem.setArguments(args);
        dialog_addEditItem.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return dialog_addEditItem;
    }


    public static Dialog_AddEditItem newInstance() {
        Dialog_AddEditItem dialog_addEditItem = new Dialog_AddEditItem();

        Bundle args = new Bundle();
        args.putBoolean("isEdit", false);
        dialog_addEditItem.setArguments(args);
        dialog_addEditItem.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return dialog_addEditItem;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        isEdit = getArguments().getBoolean("isEdit", false);

        myDb = new Database(getActivity());
        item.setDatabase(myDb);

        final View view = inflater.inflate(R.layout.dialog_add_edit_item, null);


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

        //-1 the default nr when user dont enter any value to pass in args
        if (isEdit) {
            loadPreviousData();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount = convertCheckBoxToString(dis_all);

                String number = SimpleFunctions.fillWithZeros(nr.getText().toString(), 5);      //for avon numbers whits 0 like 00123
                String[] value;
                String[] keys;

                if (isEdit) {

                    String thisDate = SimpleFunctions.getCurrentDate();

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

                    if(item.updateData(value,keys)){
                        Snackbar snackbarInfo = Snackbar.make(view,R.string.edit_item_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                        //Dialog_AddEditItem.super.dismiss();

                    } else{
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }

                } else {
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

                    if(item.insertData(value, keys)){
                        Snackbar snackbarInfo = Snackbar.make(view,R.string.add_item_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();

                    } else{
                        Snackbar snackbarInfo = Snackbar.make(view, R.string.error_notify, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();
                    }
                }

            }
        });
        return view;
    }


    private void loadPreviousData() {
        item.setClickedItemId(getArguments().getLong("myIndex"));
        String[] currentValues = item.getClickedItemData();
        nr.setText(currentValues[0]);
        name.setText(currentValues[1]);
        price.setText(currentValues[2]);
        discount = currentValues[3];
        convertStringToCheckBox(discount);
        nr.setFocusable(false);     //its PK, cant be changed
    }


    private String convertCheckBoxToString(CheckBox[] dis_all){
        String discount = "";
        for (int i = 0; i <= 8; i++) {
            if (dis_all[i].isChecked()) {
                discount = "1" + discount;
            } else {
                discount = "0" + discount;
            }
        }
        return discount;
    }

    private void convertStringToCheckBox(String discount){
        for (int i = 0; i < discount.length(); i++) {
            int discountRevert = discount.length() - 1 - i;
            if (discount.charAt((discountRevert)) == '1') dis_all[i].setChecked(true);
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}


