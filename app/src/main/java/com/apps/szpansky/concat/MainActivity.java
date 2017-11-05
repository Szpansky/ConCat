package com.apps.szpansky.concat;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;

import com.apps.szpansky.concat.dialog_fragments.AddEdit_Catalog;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.dialog_fragments.ExportImport;
import com.apps.szpansky.concat.dialog_fragments.InformationCurrentCatalog;
import com.apps.szpansky.concat.dialog_fragments.Login;
import com.apps.szpansky.concat.fragments.Main;
import com.apps.szpansky.concat.fragments.OpenCatalogs;
import com.apps.szpansky.concat.fragments.OpenItems;
import com.apps.szpansky.concat.fragments.OpenPersons;
import com.apps.szpansky.concat.simple_data.Catalog;
import com.apps.szpansky.concat.simple_data.Item;
import com.apps.szpansky.concat.simple_data.Person;
import com.apps.szpansky.concat.tools.BaseFragment;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.MyPagerAdapter;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends FragmentActivity implements DialogInterface.OnDismissListener {

    private final int REQUEST_RECREATE = 1;
    private final int REQUEST_REFRESH = 2;
    private boolean FLOATING_MENU_IS_OPEN = false;

    private ViewPager pager;
    public MyPagerAdapter myPagerAdapter;
    private static String mainColor = "list_preference_main_colors";
    private NavigationView navigationView;
    private View navViewHeader;

    private DrawerLayout drawerLayout;
    private FloatingActionButton fabMain, fabNewCatalog, fabNewPerson, fabNewItem;
    private GridLayout subFloatingMenu;

    private Main main;
    private OpenCatalogs openCatalogs;
    private OpenPersons openPersons;
    private OpenItems openItems;

    String[] titles;
    Fragment[] fragments;

    AdView adView;
    Button closeAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.getStyleFromSharedPref(mainColor, sharedPreferences));

        super.onCreate(savedInstanceState);

        main = Main.newInstance();
        openCatalogs = OpenCatalogs.newInstance(new Catalog(new Database(getBaseContext())));
        openPersons = OpenPersons.newInstance(new Person(new Database(getBaseContext())));
        openItems = OpenItems.newInstance(new Item(new Database(getBaseContext())));
        fragments = new Fragment[]{main, openCatalogs, openPersons, openItems};

        setContentView(R.layout.pager_layout);

        setAd();
        setDrawer();
        setPager();
        onFloatingButtonClick();
        onNavigationItemClick();
        onFabMenuItemClick();
    }

    private void setAd() {
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final View view = findViewById(R.id.AdViewLayout);
        closeAdView = findViewById(R.id.closeAds);
        closeAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
            }
        });
    }


    private void setPager() {
        pager = findViewById(R.id.pager);
        PagerTitleStrip pagerTitleStrip = new PagerTitleStrip(this);
        pagerTitleStrip.findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        //pager.setPageTransformer(true, new ZoomOutPageTransformer());
        titles = new String[]{getString(R.string.main), getString(R.string.orders), getString(R.string.persons), getString(R.string.items)};
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
        pager.setAdapter(myPagerAdapter);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        BaseFragment fragment = (BaseFragment) myPagerAdapter.getItem(pager.getCurrentItem());
        if(fragment.isVisible()) fragment.refreshFragmentState();
        //myPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(Gravity.START);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
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
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        navViewHeader = navigationView.getHeaderView(0);
    }


    private void onNavigationItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuLogin):
                        Login login = Login.newInstance();
                        getSupportFragmentManager().beginTransaction().add(login, "Login").commit();
                        break;
                    case (R.id.menuOrderInfo):
                        InformationCurrentCatalog information = InformationCurrentCatalog.newInstance();
                        getSupportFragmentManager().beginTransaction().add(information, "InformationCurrentCatalog").commit();
                        break;
                    case (R.id.menuOrders):
                        drawerLayout.closeDrawer(Gravity.START, true);
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pager.setCurrentItem(1, true);
                            }
                        }, 300);
                        break;
                    case (R.id.menuClients):
                        drawerLayout.closeDrawer(Gravity.START, true);
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pager.setCurrentItem(2, true);
                            }
                        }, 300);
                        break;
                    case (R.id.menuItems):
                        drawerLayout.closeDrawer(Gravity.START, true);
                        final Handler handler3 = new Handler();
                        handler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pager.setCurrentItem(3, true);
                            }
                        }, 300);
                        break;
                    case (R.id.showTools):
                        Menu menu = navigationView.getMenu();
                        if (menu.findItem(R.id.subItems).isVisible()) {
                            menu.findItem(R.id.subItems).setVisible(false);
                            item.setIcon(R.mipmap.ic_keyboard_arrow_down_black_24dp);
                        } else {
                            menu.findItem(R.id.subItems).setVisible(true);
                            item.setIcon(R.mipmap.ic_keyboard_arrow_up_black_24dp);
                        }
                        break;
                    case (R.id.menuExportImport):
                        ExportImport exportImport = ExportImport.newInstance();
                        getSupportFragmentManager().beginTransaction().add(exportImport, "Dialog_Exportimport").commit();
                        break;
                    case (R.id.menuSetting):
                        Intent Intent_Open_Settings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(Intent_Open_Settings, REQUEST_RECREATE);
                        break;
                    case (R.id.menuHelpOpinion):
                        Intent Intent_Open_HelpAndOpinion = new Intent(MainActivity.this, HelpAndOpinionActivity.class);
                        startActivity(Intent_Open_HelpAndOpinion);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case (REQUEST_RECREATE): {
                    recreate();
                    break;
                }
                case (REQUEST_REFRESH): {
                    myPagerAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
    }


    private void showHideFABMenu() {
        final Animation fabClose, fabOpen, fabRotate, fabRotateBack;
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fabRotate = AnimationUtils.loadAnimation(this, R.anim.fab_rotate);
        fabRotateBack = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_back);

        if (FLOATING_MENU_IS_OPEN) {
            subFloatingMenu.startAnimation(fabClose);
            fabMain.startAnimation(fabRotateBack);
            fabNewCatalog.setClickable(false);
            fabNewPerson.setClickable(false);
            fabNewItem.setClickable(false);
            FLOATING_MENU_IS_OPEN = false;
            subFloatingMenu.setVisibility(View.GONE);
        } else {
            subFloatingMenu.startAnimation(fabOpen);
            fabMain.startAnimation(fabRotate);
            fabNewCatalog.setClickable(true);
            fabNewPerson.setClickable(true);
            fabNewItem.setClickable(true);
            FLOATING_MENU_IS_OPEN = true;
            subFloatingMenu.setVisibility(View.VISIBLE);
        }
    }


    private void onFloatingButtonClick() {
        fabMain = findViewById(R.id.fabMain);
        subFloatingMenu = findViewById(R.id.subFloatingMenu);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideFABMenu();
            }
        });
    }


    public void onFabMenuItemClick() {
        //handler is set for smooth data content chages
        fabNewCatalog = findViewById(R.id.fabAddCatalog);
        fabNewPerson = findViewById(R.id.fabAddPerson);
        fabNewItem = findViewById(R.id.fabAddItem);

        fabNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager.setCurrentItem(1, true);
                    }
                }, 300);
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance();

                if (getSupportFragmentManager().findFragmentByTag("DialogAddEditCatalog") == null) {
                    getSupportFragmentManager().beginTransaction().add(addEditCatalog, "DialogAddEditCatalog").commit();
                    showHideFABMenu();
                }
            }
        });

        fabNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager.setCurrentItem(2, true);
                    }
                }, 300);
                AddEdit_Person addEditPerson = AddEdit_Person.newInstance();

                if (getSupportFragmentManager().findFragmentByTag("DialogAddEditPerson") == null) {
                    getSupportFragmentManager().beginTransaction().add(addEditPerson, "DialogAddEditPerson").commit();
                    showHideFABMenu();
                }
            }
        });

        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager.setCurrentItem(3, true);
                    }
                }, 300);
                AddEdit_Item addEditItem = AddEdit_Item.newInstance();

                if (getSupportFragmentManager().findFragmentByTag("DialogAddEditItem") == null) {
                    getSupportFragmentManager().beginTransaction().add(addEditItem, "DialogAddEditItem").commit();
                    showHideFABMenu();
                }
            }
        });
    }


    public void setPage(int pos) {
        pager.setCurrentItem(pos);
    }
}
