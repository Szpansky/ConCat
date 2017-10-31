package com.apps.szpansky.concat.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.fragments.Main;
import com.apps.szpansky.concat.fragments.OpenCatalogs;
import com.apps.szpansky.concat.fragments.OpenItems;
import com.apps.szpansky.concat.fragments.OpenPersons;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.simple_data.Person;


public class MyPagerAdapter extends FragmentPagerAdapter {


   /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        fragments[position] = (Fragment) super.instantiateItem(container, position);
        return fragments[position];
    }*/


    public MyPagerAdapter(FragmentManager fm, String[] titles, Database database) {
        super(fm);

        this.fragmentManager = fm;
        this.titles = titles;

        catalog = new Catalog(database);
        person = new Person(database);
        item = new Item(database);

        openCatalogs = OpenCatalogs.newInstance(catalog);
        openPersons = OpenPersons.newInstance(person);
        openItems = OpenItems.newInstance(item);
        main = Main.newInstance();

        this.fragments = new Fragment[]{main, openCatalogs, openPersons, openItems};        //save created fragment for late instantiateItem
    }


    FragmentManager fragmentManager;
    private String[] titles;

    private Main main;
    private OpenCatalogs openCatalogs;
    private OpenPersons openPersons;
    private OpenItems openItems;

     Catalog catalog;
     Person person;
     Item item;

    Fragment[] fragments;

    @Override
    public Fragment getItem(int pos) {
        return fragments[pos];
    }


    @Override
    public int getCount() {
        return fragments.length;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        openCatalogs.refreshFragmentState();
        openItems.refreshFragmentState();
        openPersons.refreshFragmentState();
        main.refreshFragmentState();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
