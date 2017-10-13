package com.apps.szpansky.concat.for_pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_AddEditItem;
import com.apps.szpansky.concat.open_all.OpenAllItemsActivity;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class PickItem extends SimpleActivity implements DialogInterface.OnDismissListener {

    Integer count = 1;
    private boolean flag = true;

    public PickItem() {
        super(new Item(),"list_preference_picking_colors");
    }


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
                addEditItem.show(getFragmentManager().beginTransaction(),"DialogAddEditItem");
            }
        });
    }


    private void dialogItemCount(final long id) {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_item_count, null);

        Button plusMark = (Button) view.findViewById(R.id.plus_mark);
        Button minusMark = (Button) view.findViewById(R.id.minus_mark);
        Button okButton = (Button) view.findViewById(R.id.button_ok);
        final TextView textCount = (TextView) view.findViewById(R.id.number_of_items);

        plusMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCount = textCount.getText().toString();

                if (itemCount.equals("")) count = 0;
                else count = Integer.parseInt(itemCount) + 1;

                textCount.setText(count.toString());
            }
        });

        minusMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCount = textCount.getText().toString();

                if (itemCount.equals("") || count < 1) count = 0;
                else count = Integer.parseInt(itemCount) - 1;

                textCount.setText(count.toString());
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String itemCount = textCount.getText().toString();

                if (itemCount.equals("")) count = 0;
                else count = Integer.parseInt(itemCount);

                Intent intent = new Intent();
                intent.putExtra("itemId", id);
                intent.putExtra("itemCount", count);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        builder.setCancelable(false);
        builder.setView(view);
        builder.show();
    }


    private void listViewItemClick() {

        flag = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;
                Dialog_AddEditItem addEditItem = Dialog_AddEditItem.newInstance(id);
                addEditItem.show(getFragmentManager().beginTransaction(),"DialogAddEditItem");
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    dialogItemCount(id);
                }
                flag = true;
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        refreshListView();
    }
}
