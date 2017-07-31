package com.apps.szpansky.concat.tools;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;

import com.apps.szpansky.concat.R;


public abstract class SimpleActivity extends AppCompatActivity {

    private boolean  flag = true;
    protected Database myDB;
    protected Bundle toNextActivityBundle = new Bundle();
    protected SimpleCursorAdapter myCursorAdapter;
    protected ListView listView;
    protected FloatingActionButton addButton;
    private AbsListView.OnScrollListener onScrollListener;
    protected Toolbar toolbar;
    private Data data;

    protected abstract void onAddButtonClick();


    protected SimpleActivity(Data data) {
        this.data = data;
    }


    protected ListView setListView() {
        return ((ListView) findViewById(R.id.list_view_simple_view));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_view);

        addButton = (FloatingActionButton) findViewById(R.id.add);

        myDB = new Database(this);

        setToolBar();

        listView = setListView();
        refreshListView();
        onAddButtonClick();
        onScrolling();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.contains("\"")) {
                    data.filter = "";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_simple_view), R.string.search_toast, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    data.filter = newText;
                }
                refreshListView();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshListView();
    }


    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    protected void refreshListView() {
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                data.getItemLayoutResourceId(),
                data.setCursor(myDB),
                data.getFromFieldsNames(),
                data.getToViewIDs(),
                0);
        listView.setAdapter(myCursorAdapter);
        listView.setOnScrollListener(onScrollListener);
    }


    protected void popupForDelete(final int id) {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_popup_alert, null);

        Button buttonYes = (Button) view.findViewById(R.id.dialog_button_yes);
        Button buttonNo = (Button) view.findViewById(R.id.dialog_button_no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.deleteData(id, myDB);
                refreshListView();
                builder.dismiss();
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            builder.dismiss();
            }
        });

        builder.setView(view);
        builder.show();
        listView.clearFocus();
    }


    private void onScrolling() {
        onScrollListener = new AbsListView.OnScrollListener() {
            int previousItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (previousItem != firstVisibleItem) {
                    if (previousItem < firstVisibleItem ) {
                        if(flag) {
                            addButton.hide();
                            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                            toolbar.setActivated(false);
                            flag = false;
                        }
                    } else {
                        if(!flag) {
                            addButton.show();
                            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                            toolbar.setActivated(true);
                            flag = true;
                        }
                    }
                    previousItem = firstVisibleItem;
                }
            }
        };
        onScrollListener.onScroll(listView, listView.getFirstVisiblePosition(), listView.getLastVisiblePosition(), listView.getCount());
    }


    protected void dialogOnClientClick(final int thisId){
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
}


