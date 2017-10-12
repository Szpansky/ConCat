package com.apps.szpansky.concat.tools;


import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.apps.szpansky.concat.R;




public class MyCursorAdapter extends SimpleCursorAdapter {
    Data data;
    AlertDialog builder;
    View builderView;


    public MyCursorAdapter(Context context, Cursor c, int flags, Data data, AlertDialog builder, View builderView) {
        super(context, data.getItemLayoutResourceId(), c, data.getFromFieldsNames(), data.getToViewIDs(), flags);
        this.data = data;
        this.builder = builder;
        this.builderView = builderView;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        final Button button = (Button) view.findViewById(R.id.listItem_delete_button);
        button.setTag(cursor.getString(cursor.getColumnIndex("_id")));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final long id = Long.parseLong(button.getTag().toString());        //TODO create loadermanager
                Button buttonYes = (Button) builderView.findViewById(R.id.dialog_button_yes);
                Button buttonNo = (Button) builderView.findViewById(R.id.dialog_button_no);
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.deleteData(id);
                        notifyDataSetChanged();
                        cursor.requery();
                        builder.dismiss();
                    }
                });
                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });

                builder.setView(builderView);
                builder.show();


            }
        });


        super.bindView(view, context, cursor);
    }


}


