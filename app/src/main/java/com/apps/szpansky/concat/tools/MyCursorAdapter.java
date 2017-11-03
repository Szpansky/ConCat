package com.apps.szpansky.concat.tools;



import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.DeletingData;
import com.apps.szpansky.concat.dialog_fragments.PrintDetail_Catalog;


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
        final Button delete = view.findViewById(R.id.listItem_delete_button);
        delete.setTag(cursor.getString(cursor.getColumnIndex("_id")));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long id = Long.parseLong(delete.getTag().toString());
                data.setClickedItemId(id);
                DeletingData deletingData = DeletingData.newInstance(data);
                fragmentManager.beginTransaction().add(deletingData, "DeletingDialog").commit();
            }
        });

        try {
            Button print = view.findViewById(R.id.printCatalogOrder);  // only catalog got print button
            print.setTag(cursor.getString(cursor.getColumnIndex("_id")));
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final long id = Long.parseLong(delete.getTag().toString());
                    PrintDetail_Catalog printCatalog = PrintDetail_Catalog.newInstance(id);
                    fragmentManager.beginTransaction().add(printCatalog, "InformationCurrentCatalog").commit();
                }
            });
        } catch (NullPointerException e) {

        }

        super.bindView(view, context, cursor);
    }


}


