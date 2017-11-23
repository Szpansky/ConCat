package com.apps.szpansky.concat.tools;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.DeletingData;
import com.apps.szpansky.concat.dialog_fragments.PrintDetail_Catalog;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.simple_data.Item_InPickList;


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

        if (data instanceof Catalog) {
            Button print = view.findViewById(R.id.printCatalogOrder);
            print.setTag(cursor.getString(cursor.getColumnIndex("_id")));
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final long id = Long.parseLong(delete.getTag().toString());
                    PrintDetail_Catalog printCatalog = PrintDetail_Catalog.newInstance(id);
                    fragmentManager.beginTransaction().add(printCatalog, "InformationCurrentCatalog").commit();
                }
            });
        }


        if (data instanceof Item && !(data instanceof Item_InPickList)) {
            String discount = cursor.getString(cursor.getColumnIndexOrThrow("DISCOUNT"));

            TextView dis_5 = view.findViewById(R.id.dis_5);
            TextView dis_10 = view.findViewById(R.id.dis_10);
            TextView dis_15 = view.findViewById(R.id.dis_15);
            TextView dis_20 = view.findViewById(R.id.dis_20);
            TextView dis_25 = view.findViewById(R.id.dis_25);
            TextView dis_30 = view.findViewById(R.id.dis_30);
            TextView dis_35 = view.findViewById(R.id.dis_35);
            TextView dis_40 = view.findViewById(R.id.dis_40);
            TextView dis_100 = view.findViewById(R.id.dis_100);

            TextView textView[] = {dis_5, dis_10, dis_15, dis_20, dis_25, dis_30, dis_35, dis_40, dis_100};

            for (int i = 0; i < discount.length(); i++) {
                int discountRevert = discount.length() - 1 - i;
                if (discount.charAt((discountRevert)) == '1') textView[i].setTextColor(Color.GREEN);
                else textView[i].setTextColor(Color.GRAY);
            }

        }


        super.bindView(view, context, cursor);
    }

}


