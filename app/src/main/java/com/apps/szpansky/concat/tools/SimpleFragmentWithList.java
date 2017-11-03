package com.apps.szpansky.concat.tools;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.szpansky.concat.R;

// class which extends that fragment need declare method for class eg. newInstance(Data data, String Style) and set that two argument.  which return fragment with those arguments

public abstract class SimpleFragmentWithList extends BaseFragment {

    public Data getDataObject() {
        return data;
    }

    static int index;
    static int top;

    public void refreshFragmentState() {
        // if(listView != null) {
        index = 0;
        if (listView.isShown()) index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        MyCursorAdapter myCursorAdapter = new MyCursorAdapter(getActivity().getBaseContext(), getDataObject(), getActivity().getSupportFragmentManager(), 0);
        listView.setAdapter(myCursorAdapter);
        listView.setOnScrollListener(onScrollListener);
        if (index != -1) {
            if (top != 0) {
                listView.setSelectionFromTop(index, top);
            } else {
                listView.setSelectionFromTop(index, top + listView.getPaddingTop());
            }
        }
        if (listView.getCount() <= 0) {
            emptyList.setVisibility(View.VISIBLE);
            catPointing.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
            catPointing.setVisibility(View.GONE);
        }
        // }
    }

    protected abstract String selectStyleKey();

    protected abstract void inflateFABView(FloatingActionButton addButton);

    protected abstract void onListViewClick(long id);

    protected abstract void onListViewLongClick(long id);

    protected abstract void inflateNewViewInToolBar(Toolbar toolbar);

    protected abstract Drawable setFABImage();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws NullPointerException {
        setDataFromBundle();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());

        view = (ViewGroup) inflater.inflate(R.layout.simple_view, container, false);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), SimpleFunctions.getBackgroundColor(styleKey, sharedPreferences)));

        setViews();
        inflateFAB();
        inflateToolBar();
        setListListener();
        refreshFragmentState();
        onScrolling();
        return view;
    }


    private boolean flag = true;
    private ListView listView;
    private FloatingActionButton addButton;
    private AbsListView.OnScrollListener onScrollListener;
    private Toolbar toolbar;
    private Data data;
    private String styleKey;
    private TextView emptyList;
    private ImageView catPointing;
    private ViewGroup view;
    private SharedPreferences sharedPreferences;


    private void setViews() {
        listView = view.findViewById(R.id.list_view_simple_view);
        addButton = view.findViewById(R.id.add);
        emptyList = view.findViewById(R.id.empty_textView);
        catPointing = view.findViewById(R.id.cat_pointing);
        catPointing.setImageDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.cat_pointing));
        toolbar = view.findViewById(R.id.toolbar);
        addButton.setImageDrawable(setFABImage());
    }


    private void inflateFAB() {
        inflateFABView(addButton);
    }


    private void setListListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewLongClick(id);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClick(id);
            }
        });
    }


    private void inflateToolBar() {
        toolbar.inflateMenu(R.menu.search);
        if (!data.getTitle().equals("")) {
            toolbar.setTitle(data.getTitle());
        }

        MenuItem item = toolbar.getMenu().findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), SimpleFunctions.getPrimaryColor(selectStyleKey(),sharedPreferences),null));

        inflateNewViewInToolBar(toolbar);

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
                refreshFragmentState();
                return false;
            }
        });
    }


    private void setDataFromBundle() {
        styleKey = selectStyleKey();
        data = (Data) getArguments().getSerializable("data");
        if (data == null || styleKey == null)
            throw new NullPointerException("set arguments (data and styleKey). Data in newInstance(Data data), for styleKey implement method selectStyleKey");
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














