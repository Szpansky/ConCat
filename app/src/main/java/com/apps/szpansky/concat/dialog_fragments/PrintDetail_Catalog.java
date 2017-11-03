package com.apps.szpansky.concat.dialog_fragments;

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


public class PrintDetail_Catalog extends DialogFragment {

    Database myDb;

    public static PrintDetail_Catalog newInstance(long myIndex) {
        PrintDetail_Catalog printCatalog = new PrintDetail_Catalog();

        Bundle bundle = new Bundle();
        bundle.putLong("myIndex", myIndex);
        printCatalog.setStyle(STYLE_NO_TITLE, 0);
        printCatalog.setArguments(bundle);

        return printCatalog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = (ViewGroup) inflater.inflate(R.layout.dialog_print_catalog, container, false);

        TextView information = view.findViewById(R.id.information);
        Button copy = view.findViewById(R.id.copy_button);

        long id = this.getArguments().getLong("myIndex", 0);

        myDb = new Database(getActivity());
        Cursor c = myDb.printCatalog(id);

        String catalogInfo = "";

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
