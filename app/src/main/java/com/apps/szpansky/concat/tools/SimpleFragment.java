package com.apps.szpansky.concat.tools;


import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.szpansky.concat.R;

// class which extends that fragment need declare method for class eg. newInstance(Data data, String Style) and set that two argument.  which return fragment with those arguments

public abstract class SimpleFragment extends Fragment {


    private boolean flag = true;
    protected MyCursorAdapter myCursorAdapter;
    protected ListView listView;
    protected FloatingActionButton addButton;
    private AbsListView.OnScrollListener onScrollListener;
    protected Toolbar toolbar;
    protected Data data;
    private String styleKey;
    FragmentManager fragmentManager;
    MenuItem item;
    SearchView searchView;
    TextView emptyList;
    ImageView catPointing;


    protected abstract void onAddButtonClick();


    protected abstract void onListViewClick();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) throws NullPointerException {
        styleKey = getArguments().getString("styleKey");
        data = (Data) getArguments().getSerializable("data");
        if (data == null || styleKey == null)
            throw new NullPointerException("set arguments (data and styleKey) eg. in newInstance(Data data, String styleKey)");
        data.setDatabase(new Database(getActivity()));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getActivity().setTheme(SimpleFunctions.setStyle(styleKey, sharedPreferences));

        View view = inflater.inflate(R.layout.activity_simple_view, container, false);

        view.setBackgroundColor(ContextCompat.getColor(getContext(), SimpleFunctions.setBackgroundColor(styleKey, sharedPreferences)));

        addButton = (FloatingActionButton) view.findViewById(R.id.add);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        listView = (ListView) view.findViewById(R.id.list_view_simple_view);
        emptyList = (TextView) view.findViewById(R.id.empty_textView);
        catPointing = (ImageView) view.findViewById(R.id.cat_pointing);

        refreshListView();
        onScrolling();
        onAddButtonClick();
        onListViewClick();
        fragmentManager = getActivity().getFragmentManager();


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        menu.clear();
        toolbar.setTitle(data.getTitle());
        inflater.inflate(R.menu.search, toolbar.getMenu());
        item = toolbar.getMenu().findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.contains("\"")) {
                    data.filter = newText.replace("\"", " ");
                } else {
                    data.filter = newText;
                }
                refreshListView();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }


    public void refreshListView() {
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        myCursorAdapter = new MyCursorAdapter(getActivity().getBaseContext(), data, fragmentManager, 0);
        listView.setAdapter(myCursorAdapter);
        listView.setOnScrollListener(onScrollListener);
        if (top != 0) {
            listView.setSelectionFromTop(index, top);
        } else {
            listView.setSelectionFromTop(index, top + listView.getPaddingTop());
        }
        if (listView.getCount() <= 0) {
            emptyList.setVisibility(View.VISIBLE);
            catPointing.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
            catPointing.setVisibility(View.GONE);
        }
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














