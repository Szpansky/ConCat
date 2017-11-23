package com.apps.szpansky.concat.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apps.szpansky.concat.fragments.Main;
import com.apps.szpansky.concat.fragments.OpenCatalogs;
import com.apps.szpansky.concat.fragments.OpenItems;
import com.apps.szpansky.concat.fragments.OpenPersons;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    Fragment[] fragments;
    String[] titles;

    private Main main;
    private OpenCatalogs openCatalogs;
    private OpenPersons openPersons;
    private OpenItems openItems;

    public MyPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);

        main = Main.newInstance();
        openCatalogs = OpenCatalogs.newInstance();
        openPersons = OpenPersons.newInstance();
        openItems = OpenItems.newInstance();
        fragments = new Fragment[]{main, openCatalogs, openPersons, openItems};

        this.titles = titles;

    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int pos) {
      return fragments[pos];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}