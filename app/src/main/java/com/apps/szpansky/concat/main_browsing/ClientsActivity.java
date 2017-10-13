package com.apps.szpansky.concat.main_browsing;


import android.content.Intent;
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
        super(new Client(),"list_preference_browsing_colors");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(data.getTitle());
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


    private void dialogOnClientClick(){
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_on_client_click, null);
        Button saveCatalog = (Button) dialogView.findViewById(R.id.buttonOrderSave);

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
                String[] keys = new String[]{Database.CLIENT_STATUS};
                String[] value = new String[]{status};
                data.updateData(value,keys);
                refreshListView();
                builder.dismiss();
            }
        });
        builder.setView(dialogView);
        builder.show();
    }


    private void listViewItemClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.setClickedItemId(id);
                dialogOnClientClick();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    data.setClickedItemId(id);
                    Intent intent = new Intent(ClientsActivity.this, OrdersActivity.class);
                    Order.clickedClientId = id;
                    startActivity(intent);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (requestCode == BACK_CODE) {
            if (resultCode == RESULT_OK) {

                Long personId = intentData.getLongExtra("personId", 0);
                Long catalogId = Client.clickedCatalogId;

                String[] value = new String[]{catalogId.toString(), personId.toString(), getString(R.string.db_status_not_payed)};
                String[] keys = new String[]{Database.CLIENT_CATALOG_ID, Database.CLIENT_PERSON_ID, Database.CLIENT_STATUS};

                data.insertData(value, keys);


                refreshListView();
            }
        }
    }
}
