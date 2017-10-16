package com.apps.szpansky.concat.tools;


import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.fragments.Dialog_DeletingData;
import com.apps.szpansky.concat.fragments.Dialog_PrintCatalogInfo;


public class MyCursorAdapter extends SimpleCursorAdapter {
    Data data;
    FragmentManager fragmentManager;


    public MyCursorAdapter(Context context, Data data, FragmentManager fragmentManager, int flags) {
        super(context, data.getItemLayoutResourceId(), data.getCursor(), data.getFromFieldsNames(), data.getToViewIDs(), flags);
        this.data = data;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final Button delete = (Button) view.findViewById(R.id.listItem_delete_button);
        delete.setTag(cursor.getString(cursor.getColumnIndex("_id")));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long id = Long.parseLong(delete.getTag().toString());
                data.setClickedItemId(id);
                Dialog_DeletingData deletingData = new Dialog_DeletingData().newInstance(data);
                deletingData.show(fragmentManager.beginTransaction(), "Dialog_Information");
            }
        });

        try {
            Button print = (Button) view.findViewById(R.id.printCatalogOrder);  // only catalog got print button
            print.setTag(cursor.getString(cursor.getColumnIndex("_id")));
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final long id = Long.parseLong(delete.getTag().toString());
                    Dialog_PrintCatalogInfo printCatalog = new Dialog_PrintCatalogInfo().newInstance(id);
                    printCatalog.show(fragmentManager.beginTransaction(), "Dialog_Information");
                }
            });
        } catch (NullPointerException e) {

        }

        super.bindView(view, context, cursor);
    }


}


