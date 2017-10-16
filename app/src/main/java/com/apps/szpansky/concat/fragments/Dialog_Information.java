package com.apps.szpansky.concat.fragments;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;


public class Dialog_Information extends DialogFragment {

    Database myDB;

    public Dialog_Information newInstance() {
        Dialog_Information information = new Dialog_Information();

        information.setStyle(STYLE_NO_TITLE, 0);

        return information;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_information, null);

        myDB = new Database(getActivity());

        final int textViewForFirstTableCount = 8;


        TextView[] textViews = new TextView[12];
        textViews[0] = (TextView) view.findViewById(R.id.current_catalogs_number);
        textViews[1] = (TextView) view.findViewById(R.id.current_clients_amount);
        textViews[2] = (TextView) view.findViewById(R.id.current_ordered_item_amount);
        textViews[3] = (TextView) view.findViewById(R.id.current_pcs_ordered);
        textViews[4] = (TextView) view.findViewById(R.id.current_not_payed_amount);
        textViews[5] = (TextView) view.findViewById(R.id.current_payed_amount);
        textViews[6] = (TextView) view.findViewById(R.id.current_ready_amount);
        textViews[7] = (TextView) view.findViewById(R.id.current_total);

        textViews[8] = (TextView) view.findViewById(R.id.all_catalogs_amount);
        textViews[9] = (TextView) view.findViewById(R.id.all_clients_amount);
        textViews[10] = (TextView) view.findViewById(R.id.all_items_amount);
        textViews[11] = (TextView) view.findViewById(R.id.all_total);

        Button backButton = (Button) view.findViewById(R.id.dialog_info_backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Cursor c = myDB.getCurrentCatalogInfo();

        for (int i = 0; i < textViewForFirstTableCount; i++) {
            textViews[i].setText(c.getString(i));
        }

        c = myDB.getCurrentInfo();

        for (int i = textViewForFirstTableCount; i < textViews.length; i++) {
            textViews[i].setText(c.getString(0));
            if (!c.isLast()) c.moveToNext();
        }

        c.close();
        myDB.close();

        return view;
    }
}
