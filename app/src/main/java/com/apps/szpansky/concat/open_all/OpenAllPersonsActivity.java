package com.apps.szpansky.concat.open_all;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.add_edit.AddEditPersonActivity;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.SimpleActivity;

public class OpenAllPersonsActivity extends SimpleActivity {


    public OpenAllPersonsActivity() {
        super(new Person());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listViewItemClick();
    }


    @Override
    protected void onAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenAllPersonsActivity.this, AddEditPersonActivity.class);
                startActivity(intent);
            }
        });
    }


    private void listViewItemClick() {
        final boolean[] flag = new boolean[1];
        flag[0] = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag[0] = false;
                popupForDelete((int) id);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (flag[0]) {
                    Intent intent = new Intent(OpenAllPersonsActivity.this, AddEditPersonActivity.class);

                    toNextActivityBundle.putBoolean("isEdit", true);
                    toNextActivityBundle.putInt("personId", (int) id);

                    intent.putExtras(toNextActivityBundle);
                    startActivity(intent);
                }
                flag[0] = true;
            }
        });
    }
}