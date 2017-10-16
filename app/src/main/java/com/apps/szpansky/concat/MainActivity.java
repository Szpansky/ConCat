package com.apps.szpansky.concat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.apps.szpansky.concat.fragments.Dialog_AddEditCatalog;
import com.apps.szpansky.concat.fragments.Dialog_AddEditItem;
import com.apps.szpansky.concat.fragments.Dialog_AddEditPerson;
import com.apps.szpansky.concat.fragments.Dialog_ExportImport;
import com.apps.szpansky.concat.fragments.Dialog_Information;
import com.apps.szpansky.concat.fragments.Dialog_Loading;
import com.apps.szpansky.concat.fragments.Dialog_Login;
import com.apps.szpansky.concat.main_browsing.CatalogsActivity;
import com.apps.szpansky.concat.open_all.OpenAllItemsActivity;
import com.apps.szpansky.concat.open_all.OpenAllPersonsActivity;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    public static Integer rewardAmount;

    private static boolean FLOATING_MENU = false;
    private static String styleKey = "list_preference_main_colors";

    private Database myDB = new Database(this);

    private FloatingActionButton fabMain, fabNewCatalog, fabNewPerson, fabNewItem;

    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private GridLayout subFloatingMenu;
    View navViewHeader;

    private AdView mAdView;
    private RewardedVideoAd mAd;
    Dialog_Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(SimpleFunctions.setStyle(styleKey, sharedPreferences));
        setContentView(R.layout.activity_main);
        loading = new Dialog_Loading();
        setAds();
        setMainInfo();
        setDrawer();
        onNavigationItemClick();
        onStartClick();
        onFloatingButtonClick();
        onFabMenuItemClick();
        onDailyRewardClick();
        updateUserInfo();
    }


    private void updateUserInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView loggedAs = (TextView) navViewHeader.findViewById(R.id.navi_loggedAs);
        loggedAs.setText(sharedPreferences.getString("pref_edit_text_loggedAs", getResources().getString(R.string.pref_def_logged_as)));
        TextView rewardPoints = (TextView) navViewHeader.findViewById(R.id.navi_rewardAmount);
        rewardPoints.setText(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
    }


    private void setAds() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
    }


    private void onStartClick() {
        Button openCatalogs = (Button) findViewById(R.id.openCatalogs);
        openCatalogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Intent_Open_Catalogs = new Intent(MainActivity.this, CatalogsActivity.class);
                MainActivity.this.startActivity(Intent_Open_Catalogs);
            }
        });
    }


    private void onDailyRewardClick() {
        Button startAds = (Button) findViewById(R.id.startAd);
        startAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAd.loadAd(getResources().getString(R.string.ads_reward_main_id), new AdRequest.Builder().build());
                loading.show(getFragmentManager().beginTransaction(),"Loading");
                loading.setCancelable(true);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navViewHeader = navigationView.getHeaderView(0);
    }


    private void onNavigationItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuLogin):
                        Dialog_Login login = new Dialog_Login().newInstance();
                        login.show(getFragmentManager().beginTransaction(), "Dialog_Login");
                        break;
                    case (R.id.menuMyAccount):
                        Dialog_Information information = new Dialog_Information().newInstance();
                        information.show(getFragmentManager().beginTransaction(), "Dialog_Information");
                        break;
                    case (R.id.menuClients):
                        Intent Intent_Open_Persons = new Intent(MainActivity.this, OpenAllPersonsActivity.class);
                        startActivity(Intent_Open_Persons);
                        break;
                    case (R.id.menuItems):
                        Intent Intent_Open_Items = new Intent(MainActivity.this, OpenAllItemsActivity.class);
                        startActivity(Intent_Open_Items);
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
                        Dialog_ExportImport exportImport = new Dialog_ExportImport().newInstance();
                        exportImport.show(getFragmentManager().beginTransaction(), "Dialog_Exportimport");
                        break;
                    case (R.id.menuSetting):
                        Intent Intent_Open_Settings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(Intent_Open_Settings);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (FLOATING_MENU) {
                            subFloatingMenu.startAnimation(fabClose);
                            fabMain.startAnimation(fabRotateBack);
                            fabNewCatalog.setClickable(false);
                            fabNewPerson.setClickable(false);
                            fabNewItem.setClickable(false);
                            FLOATING_MENU = false;
                            subFloatingMenu.setVisibility(View.GONE);
                        } else {
                            subFloatingMenu.startAnimation(fabOpen);
                            fabMain.startAnimation(fabRotate);
                            fabNewCatalog.setClickable(true);
                            fabNewPerson.setClickable(true);
                            fabNewItem.setClickable(true);
                            FLOATING_MENU = true;
                            subFloatingMenu.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }


    public void onFabMenuItemClick() {
        fabNewCatalog = (FloatingActionButton) findViewById(R.id.fabAddCatalog);
        fabNewPerson = (FloatingActionButton) findViewById(R.id.fabAddPerson);
        fabNewItem = (FloatingActionButton) findViewById(R.id.fabAddItem);

        fabNewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_AddEditCatalog addEditCatalog = new Dialog_AddEditCatalog().newInstance();
                addEditCatalog.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });

        fabNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_AddEditPerson addEditPerson = new Dialog_AddEditPerson().newInstance();
                addEditPerson.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });

        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_AddEditItem addEditItem = new Dialog_AddEditItem().newInstance();
                addEditItem.show(getFragmentManager().beginTransaction(), "DialogAddEditCatalog");
            }
        });
    }


    private void setMainInfo() {
        TextView mainCatalogNr = (TextView) findViewById(R.id.main_order_number);
        TextView mainCatalogMonthsLeft = (TextView) findViewById(R.id.main_order_months_left);
        TextView mainCatalogDaysLeft = (TextView) findViewById(R.id.main_order_days_left);
        TextView mainCatalogPrice = (TextView) findViewById(R.id.main_order_price);
        TextView mainCatalogNotPayed = (TextView) findViewById(R.id.main_order_client_count);
        View view = findViewById(R.id.main_layout_info);

        Cursor c = myDB.getCurrentCatalogInfo();

        if (c.isNull(0) || c.isNull(4) || c.isNull(7) || c.isNull(9)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            mainCatalogNr.setText(c.getString(0));
            String notPayed = c.getString(4);
            notPayed = (notPayed.equals("1")) ? notPayed + " " + getResources().getString(R.string.order) : notPayed + " " + getResources().getString(R.string.orders);
            mainCatalogNotPayed.setText(notPayed);
            String price = c.getString(7) + getResources().getString(R.string.money_shortcut);
            mainCatalogPrice.setText(price);
            String daysLeft;
            String monthsLeft;

            String endDate = c.getString(9);
            if (endDate.equals("")) {
                daysLeft = "-";
                monthsLeft = "-";

            } else {
                String[] date;
                date = SimpleFunctions.getTimeLeft(endDate).split(" ");
                if (!date[0].isEmpty()) {
                    daysLeft = date[1];
                    daysLeft = (daysLeft.equals("1")) ? daysLeft + " " + getResources().getString(R.string.day) : daysLeft + " " + getResources().getString(R.string.days);
                    mainCatalogDaysLeft.setText(daysLeft);
                }
                if (!date[0].isEmpty()) {
                    monthsLeft = date[0];
                    monthsLeft = (monthsLeft.equals("1") || monthsLeft.equals("0")) ? monthsLeft + " " + getResources().getString(R.string.month) : monthsLeft + " " + getResources().getString(R.string.months);
                    mainCatalogMonthsLeft.setText(monthsLeft);
                }
            }
        }
        c.close();
        myDB.close();
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        mAd.show();
    }


    @Override
    public void onRewardedVideoAdOpened() {

    }


    @Override
    public void onRewardedVideoStarted() {

    }


    @Override
    public void onRewardedVideoAdClosed() {
        Snackbar snackbarInfo = Snackbar.make(findViewById(R.id.drawerLayout), R.string.end, Snackbar.LENGTH_SHORT);
        snackbarInfo.show();
        addPoints(1);
        updateUserInfo();
        loading.dismiss();
    }


    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "Added points: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
        addPoints(rewardItem.getAmount());
        updateUserInfo();
    }


    private void addPoints(int amount) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rewardAmount = Integer.parseInt(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
        rewardAmount = rewardAmount + amount;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("pref_edit_text_rewardAmount", rewardAmount.toString());
        editor.apply();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {

    }


    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Snackbar snackbarInfo = Snackbar.make(findViewById(R.id.drawerLayout), R.string.failed_to_load_ad, Snackbar.LENGTH_SHORT);
        snackbarInfo.show();
        if(loading.isVisible())loading.dismiss();
    }
}



