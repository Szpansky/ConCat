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
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;

import com.apps.szpansky.concat.dialog_fragments.AddEdit_Catalog;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Item;
import com.apps.szpansky.concat.dialog_fragments.AddEdit_Person;
import com.apps.szpansky.concat.dialog_fragments.ExportImport;
import com.apps.szpansky.concat.dialog_fragments.InformationCurrentCatalog;
import com.apps.szpansky.concat.dialog_fragments.Login;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.MyPagerAdapter;
import com.apps.szpansky.concat.tools.SimpleFunctions;


public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private final int REQUEST_RECREATE = 1;
    private final int REQUEST_REFRESH = 2;
    private boolean FLOATING_MENU_IS_OPEN = false;

    private ViewPager pager;
    private MyPagerAdapter myPagerAdapter;
    private static String mainColor = "list_preference_main_colors";
    private NavigationView navigationView;
    private View navViewHeader;

    private DrawerLayout drawerLayout;
    private FloatingActionButton fabMain, fabNewCatalog, fabNewPerson, fabNewItem;
    private GridLayout subFloatingMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.setStyle(mainColor, sharedPreferences));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_layout);
        setDrawer();
        setPager();
        onFloatingButtonClick();
        onNavigationItemClick();
    }


    private void setPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip pagerTitleStrip = new PagerTitleStrip(this);
        pagerTitleStrip.findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        //pager.setPageTransformer(true, new ZoomOutPageTransformer());
        String[] titles = new String[]{getString(R.string.main), getString(R.string.orders), getString(R.string.persons), getString(R.string.items)};
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), titles, new Database(this));
        pager.setAdapter(myPagerAdapter);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        myPagerAdapter.notifyDataSetChanged();
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navView);
        navViewHeader = navigationView.getHeaderView(0);

    }


    private void onNavigationItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuLogin):
                        Login login = Login.newInstance();
                        login.show(getFragmentManager().beginTransaction(), "Login");
                        break;
                    case (R.id.menuOrderInfo):
                        InformationCurrentCatalog information = InformationCurrentCatalog.newInstance();
                        information.show(getFragmentManager().beginTransaction(), "InformationCurrentCatalog");
                        break;
                    case (R.id.menuOrders):
                        drawerLayout.closeDrawer(Gravity.LEFT, false);
                        pager.setCurrentItem(1, true);
                        break;
                    case (R.id.menuClients):
                        drawerLayout.closeDrawer(Gravity.LEFT, false);
                        pager.setCurrentItem(2, true);
                        break;
                    case (R.id.menuItems):
                        drawerLayout.closeDrawer(Gravity.LEFT, false);
                        pager.setCurrentItem(3, true);
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
                        exportImport.show(getFragmentManager().beginTransaction(), "Dialog_Exportimport");
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
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case (REQUEST_RECREATE): {
                    recreate();
                    break;
                }
                case (REQUEST_REFRESH):{
                    myPagerAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        if (resultCode == Activity.RESULT_CANCELED) {

        }

    }


    private void onFloatingButtonClick() {
        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        subFloatingMenu = (GridLayout) findViewById(R.id.subFloatingMenu);

        final Animation fabClose, fabOpen, fabRotate, fabRotateBack;
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fabRotate = AnimationUtils.loadAnimation(this, R.anim.fab_rotate);
        fabRotateBack = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_back);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        });
        onFabMenuItemClick();
    }


    public void onFabMenuItemClick() {
        //handler is set for smooth data content chages

        fabNewCatalog = (FloatingActionButton) findViewById(R.id.fabAddCatalog);
        fabNewPerson = (FloatingActionButton) findViewById(R.id.fabAddPerson);
        fabNewItem = (FloatingActionButton) findViewById(R.id.fabAddItem);

        fabNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pager.setCurrentItem(1, true);
                    }
                }, 250);
                AddEdit_Catalog addEditCatalog = AddEdit_Catalog.newInstance();
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
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
                }, 250);
                AddEdit_Person addEditPerson = AddEdit_Person.newInstance();
                addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditPerson");
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
                }, 250);
                AddEdit_Item addEditItem = AddEdit_Item.newInstance();
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditItem");
            }
        });
    }
}
