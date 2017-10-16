package com.apps.szpansky.concat.fragments;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Database;


public class Dialog_PrintCatalogInfo extends DialogFragment {

    Database myDb;

    public Dialog_PrintCatalogInfo newInstance(long myIndex) {
        Dialog_PrintCatalogInfo printCatalog = new Dialog_PrintCatalogInfo();

        Bundle bundle = new Bundle();
        bundle.putLong("myIndex", myIndex);
        printCatalog.setStyle(STYLE_NO_TITLE, 0);
        printCatalog.setArguments(bundle);

        return printCatalog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_print_catalog, null);
        TextView information = (TextView) view.findViewById(R.id.information);
        Button copy = (Button) view.findViewById(R.id.copy_button);

        long id = this.getArguments().getLong("myIndex", 0);

        myDb = new Database(getActivity());
        Cursor c = myDb.printCatalog(id);

        String catalogInfo = " | " + getResources().getString(R.string.code) + " | " + getResources().getString(R.string.amount) + " | " + getResources().getString(R.string.item_name) + "\n\n";

        for (int i = 0; i < c.getCount(); i++) {
            for (int x = 0; x < c.getColumnCount(); x++) {
                catalogInfo = catalogInfo + " | " + c.getString(x);
            }
            catalogInfo = catalogInfo + "\n\n";
            c.moveToNext();
        }
        information.setText(catalogInfo);

        final String finalCatalogInfo = catalogInfo;
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("catalogInfo", finalCatalogInfo);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), R.string.successfully_notify, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        return view;
    }
}
