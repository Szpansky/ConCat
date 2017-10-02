package com.apps.szpansky.concat.main_browsing;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.apps.szpansky.concat.for_pick.PickPerson;
import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.simple_data.Order;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleActivity;


public class ClientsActivity extends SimpleActivity {

    private final int BACK_CODE = 1;

    public ClientsActivity() {
        super(new Client());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle();
        addButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_playlist_add_white_24dp));

        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientsActivity.this, PickPerson.class);
                startActivityForResult(intent, BACK_CODE);
            }
        });
    }

    private void setTitle(){
        Cursor c = myDB.getRows(Database.TABLE_CATALOGS,Database.CATALOG_ID, Client.clickedCatalogId );
        String title = c.getString(1);
        this.setTitle(title);
    }


    private void dialogOnClientClick(final int thisId){
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_on_client_click, null);
        Button saveCatalog = (Button) dialogView.findViewById(R.id.buttonOrderSave);
        Button deleteCatalog = (Button) dialogView.findViewById(R.id.buttonOrderDelete);

        deleteCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupForDelete(thisId);
                builder.dismiss();
            }
        });

        saveCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status;
                RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroupOrder);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case (R.id.radioButtonOrderNotPaid):
                        status = getString(R.string.db_status_not_payed);
                        break;
                    case (R.id.radioButtonOrderPaid):
                        status = getString(R.string.db_status_payed);
                        break;
                    case (R.id.radioButtonOrderReady):
                        status = getString(R.string.db_status_ready);
                        break;
                    default:
                        status = getString(R.string.db_status_not_payed);
                        break;
                }
                myDB.updateRowClient(thisId, status);
                refreshListView();
                builder.dismiss();
            }
        });
        builder.setView(dialogView);
        builder.show();
    }


    private void listViewItemClick() {
        final boolean[] flag = new boolean[1];
        flag[0] = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag[0] = false;
                final long thisId = id;

                dialogOnClientClick((int) thisId);

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag[0]) {

                    Intent intent = new Intent(ClientsActivity.this, OrdersActivity.class);
                    Order.clickedClientId = (int) id;
                    startActivity(intent);
                }
                flag[0] = true;
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACK_CODE) {
            if (resultCode == RESULT_OK) {

                Integer personId = data.getIntExtra("personId", 0);
                Integer catalogId = Client.clickedCatalogId;

                myDB.insertDataToClients(catalogId.toString(), personId.toString(), getString(R.string.db_status_not_payed));

                refreshListView();
            }
        }
    }
}
