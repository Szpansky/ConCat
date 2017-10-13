package com.apps.szpansky.concat.tools;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;


import com.apps.szpansky.concat.R;


public abstract class SimpleActivity extends AppCompatActivity {

    private boolean flag = true;
    protected Bundle toNextActivityBundle = new Bundle();
    protected MyCursorAdapter myCursorAdapter;
    protected ListView listView;
    protected FloatingActionButton addButton;
    private AbsListView.OnScrollListener onScrollListener;
    protected Toolbar toolbar;
    protected Data data;
    private String styleKey;
    AlertDialog builder;

    protected abstract void onAddButtonClick();


    protected SimpleActivity(Data data, String styleKey) {
        this.data = data;
        this.styleKey = styleKey;
    }


    protected ListView setListView() {
        return ((ListView) findViewById(R.id.list_view_simple_view));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setTheme(SimpleFunctions.setStyle(styleKey,sharedPreferences));
        setContentView(R.layout.activity_simple_view);

        addButton = (FloatingActionButton) findViewById(R.id.add);

        data.setDatabase(new Database(this));

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
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        myCursorAdapter = new MyCursorAdapter(getBaseContext(),
                data.getCursor(),
                0,
                data,
                builder = new AlertDialog.Builder(this).create(),
                getLayoutInflater().inflate(R.layout.dialog_popup_alert, null)
                );


        listView.setAdapter(myCursorAdapter);
        listView.setOnScrollListener(onScrollListener);
        if (index != 0) listView.setSelectionFromTop(index, top);
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
                    if (previousItem < firstVisibleItem) {
                        if (flag) {
                            addButton.hide();
                            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                            toolbar.setActivated(false);
                            flag = false;
                        }
                    } else {
                        if (!flag) {
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

}


