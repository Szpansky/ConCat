package com.apps.szpansky.concat.main_browsing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.add_edit.AddEditCatalogActivity;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class CatalogsActivity extends SimpleActivity {

    private boolean flag = true;

    public CatalogsActivity() {
        super(new Catalog());
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
                Intent intent = new Intent(CatalogsActivity.this, AddEditCatalogActivity.class);
                startActivity(intent);
            }
        });
    }


    private void dialogOnCatalogClick(final long thisid) {

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_question_edit_delete, null);
        Button edit = (Button) dialogView.findViewById(R.id.dialog_button_edit);
        Button delete = (Button) dialogView.findViewById(R.id.dialog_button_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupForDelete((int) thisid);
                builder.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogsActivity.this, AddEditCatalogActivity.class);

                toNextActivityBundle.putInt("catalogId", (int) thisid);
                toNextActivityBundle.putBoolean("isEdit", true);

                intent.putExtras(toNextActivityBundle);
                startActivity(intent);
                builder.dismiss();
            }
        });

        builder.setView(dialogView);
        builder.show();

    }


    private void listViewItemClick() {
        flag = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;

                dialogOnCatalogClick((int) id);

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    Intent intent = new Intent(CatalogsActivity.this, ClientsActivity.class);
                    Client.clickedCatalogId = (int) id;
                    startActivity(intent);
                }
                flag = true;
            }
        });
    }
}
