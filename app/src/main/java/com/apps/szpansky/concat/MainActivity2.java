package com.apps.szpansky.concat;


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.apps.szpansky.concat.fragments.MainFragment;
import com.apps.szpansky.concat.fragments.OpenCatalogs;
import com.apps.szpansky.concat.fragments.OpenItems;
import com.apps.szpansky.concat.fragments.OpenPersons;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.ZoomOutPageTransformer;

import static com.apps.szpansky.concat.R.id.drawerLayout;


public class MainActivity2 extends AppCompatActivity implements DialogInterface.OnDismissListener {

    OpenCatalogs openCatalogs;
    OpenPersons openPersons;
    OpenItems openItems;
    MainFragment mainFragment;
    ViewPager pager;

    Catalog catalog;
    Person person;
    Item item;

    private NavigationView navigationView;
    private View navViewHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_layout);

        catalog = new Catalog(getString(R.string.orders));
        person = new Person(getString(R.string.persons));
        item = new Item(getString(R.string.items));

        mainFragment = MainFragment.newInstance();
        openCatalogs = OpenCatalogs.newInstance(catalog, "1");
        openPersons = OpenPersons.newInstance(person, "1");
        openItems = OpenItems.newInstance(item, "1");

        setPager();
        setDrawer();
    }


    private void setPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip pagerTitleStrip = new PagerTitleStrip(this);
        pagerTitleStrip.findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return mainFragment;
                case 1:
                    return openCatalogs;
                case 2:
                    return openPersons;
                case 3:
                    return openItems;
                default:
                    return openCatalogs;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Menu główne";
                case 1:
                    return catalog.getTitle();
                case 2:
                    return person.getTitle();
                case 3:
                    return item.getTitle();
                default:
                    return "Menu";

            }
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (openCatalogs.isVisible()) openCatalogs.refreshListView();
        if (openItems.isVisible()) openItems.refreshListView();
        if (openPersons.isVisible()) openPersons.refreshListView();
    }


    @Override
    public void openContextMenu(View view) {
        super.openContextMenu(view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(Gravity.START);
    }


    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(Gravity.START);
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
        } else {
            //finish();
            if (pager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            } else {
                // Otherwise, select the previous step.
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        }
    }


    private void setDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navViewHeader = navigationView.getHeaderView(0);
    }

}
