package com.apps.szpansky.concat.open_all;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.add_edit.AddEditPersonActivity;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.SimpleActivity;

public class OpenAllPersonsActivity extends SimpleActivity {


    public OpenAllPersonsActivity() {
        super(new Person(),"list_preference_open_all_colors");
    }

    boolean flag = true;


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
                Intent intent = new Intent(OpenAllPersonsActivity.this, AddEditPersonActivity.class);
                startActivity(intent);
            }
        });
    }


    private void listViewItemClick() {
        flag = true;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                flag = false;

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (flag) {
                    Intent intent = new Intent(OpenAllPersonsActivity.this, AddEditPersonActivity.class);

                    toNextActivityBundle.putBoolean("isEdit", true);
                    toNextActivityBundle.putLong("personId", id);

                    intent.putExtras(toNextActivityBundle);
                    startActivity(intent);
                }
                flag = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        onNavigateUp();
    }
}