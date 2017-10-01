package com.apps.szpansky.concat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.szpansky.concat.add_edit.AddEditCatalogActivity;
import com.apps.szpansky.concat.add_edit.AddEditItemsActivity;
import com.apps.szpansky.concat.add_edit.AddEditPersonActivity;
import com.apps.szpansky.concat.main_browsing.CatalogsActivity;
import com.apps.szpansky.concat.open_all.OpenAllItemsActivity;
import com.apps.szpansky.concat.open_all.OpenAllPersonsActivity;
import com.apps.szpansky.concat.open_all.OpenAllCatalogsActivity;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.FileManagement;
import com.apps.szpansky.concat.tools.NetworkFunctions;
import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    public static boolean LOGGED = false;
    public static Integer rewardAmount;

    private final static boolean EXPORT = true;
    private final static boolean IMPORT = false;
    private static boolean FLOATING_MENU = false;

    private String tableName = Database.TABLE_ITEMS;    //default exported/imported content
    private String fileName = "Items.txt";

    private Button openCatalogs, startAds;
    private FloatingActionButton fabMain, fabNewCatalog, fabNewPerson, fabNewItem;

    private Animation fabClose, fabOpen, fabRotate, fabRotateBack;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private GridLayout subFloatingMenu;

    //private AdView mAdView;
    private RewardedVideoAd mAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAds();

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fabRotate = AnimationUtils.loadAnimation(this, R.anim.fab_rotate);
        fabRotateBack = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_back);

        setDrawer();
        onNavigationItemClick();
        onStartClick();
        onFloatingButtonClick();
        onFabMenuItemClick();
        onDailyRewardClick();
        getPreferences();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getPreferences();
    }


    private void getPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        View v = navigationView.getHeaderView(0);
        TextView loggedAs = (TextView) v.findViewById(R.id.navi_loggedAs);
        TextView rewardPoints = (TextView) v.findViewById(R.id.navi_rewardAmount);
        loggedAs.setText(sharedPreferences.getString("pref_edit_text_loggedAs", getResources().getString(R.string.pref_def_logged_as)));
        rewardPoints.setText(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
    }


    private void setAds() {
        //mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
    }


    private void onStartClick() {
        openCatalogs = (Button) findViewById(R.id.openCatalogs);
        openCatalogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Intent_Open_Catalogs = new Intent(MainActivity.this, CatalogsActivity.class);
                MainActivity.this.startActivity(Intent_Open_Catalogs);
            }
        });
    }


    private void onDailyRewardClick() {
        startAds = (Button) findViewById(R.id.startAd);
        startAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAd.loadAd(getResources().getString(R.string.ads_reward_main_id), new AdRequest.Builder().build());
                Snackbar snackbarInfo = Snackbar.make(findViewById(R.id.drawerLayout), R.string.wait_notify, Snackbar.LENGTH_INDEFINITE);
                snackbarInfo.show();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    private void onNavigationItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuLogin):
                        //drawerLayout.closeDrawer(Gravity.START, false);
                        dialogLoginBuilder();
                        break;
                    case (R.id.menuMyAccount):
                        dialogInformationBuilder();
                        break;
                    case (R.id.menuWorks):
                        //drawerLayout.closeDrawer(Gravity.START, false);
                        Intent Intent_Open_Works = new Intent(MainActivity.this, OpenAllCatalogsActivity.class);
                        startActivity(Intent_Open_Works);
                        break;
                    case (R.id.menuClients):
                        //drawerLayout.closeDrawer(Gravity.START, false);
                        Intent Intent_Open_Persons = new Intent(MainActivity.this, OpenAllPersonsActivity.class);
                        startActivity(Intent_Open_Persons);
                        break;
                    case (R.id.menuItems):
                        //drawerLayout.closeDrawer(Gravity.START, false);
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
                        dialogExportImportBuilder();
                        break;
                    case (R.id.menuSetting):
                        Snackbar snackbarInfo = Snackbar.make(findViewById(R.id.drawerLayout), R.string.coming_soon, Snackbar.LENGTH_SHORT);
                        snackbarInfo.show();

                        //Intent Intent_Open_Settings = new Intent(MainActivity.this, SettingsActivity.class);
                        //startActivity(Intent_Open_Settings);
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
                Intent intent = new Intent(MainActivity.this, AddEditCatalogActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        fabNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditPersonActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditItemsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }


    private void dialogLoginBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_login, null);
        final EditText emailEditText = (EditText) dialogView.findViewById(R.id.dialog_login_email);
        final EditText passwordEditText = (EditText) dialogView.findViewById(R.id.dialog_login_password);
        Button loginButton = (Button) dialogView.findViewById(R.id.dialog_login_loginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                LOGGED = NetworkFunctions.logIn(email, password);

                if (LOGGED) {
                    Snackbar snackbar = Snackbar.make(dialogView, R.string.coming_soon, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(dialogView, R.string.coming_soon, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
        builder.setView(dialogView);
        builder.create();
        builder.show();
    }


    private void dialogInformationBuilder() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_information, null);

        Button backButton = (Button) dialogView.findViewById(R.id.dialog_info_backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        Database myDB = new Database(this);
        Cursor c = myDB.getCurrentCatalogInfo();


        TextView currentCatalogNumber = (TextView) dialogView.findViewById(R.id.current_catalogs_number);
        TextView currentCatalogClientsAmount = (TextView) dialogView.findViewById(R.id.current_clients_amount);
        TextView currentCatalogItemOrderedAmount = (TextView) dialogView.findViewById(R.id.current_ordered_item_amount);
        TextView currentCatalogNotPayed = (TextView) dialogView.findViewById(R.id.current_not_payed_amount);
        TextView currentCatalogPayed = (TextView) dialogView.findViewById(R.id.current_payed_amount);
        TextView currentCatalogReady = (TextView) dialogView.findViewById(R.id.current_ready_amount);
        TextView currentCatalogTotal = (TextView) dialogView.findViewById(R.id.current_total);
        currentCatalogNumber.setText(c.getString(0));
        currentCatalogClientsAmount.setText(c.getString(1));
        currentCatalogItemOrderedAmount.setText(c.getString(2));
        currentCatalogNotPayed.setText(c.getString(3));
        currentCatalogPayed.setText(c.getString(4));
        currentCatalogReady.setText(c.getString(5));
        currentCatalogTotal.setText(c.getString(6));

        c = myDB.getCurrentInfo();

        TextView allCatalogsAmount = (TextView) dialogView.findViewById(R.id.all_catalogs_amount);
        TextView allClientsAmount = (TextView) dialogView.findViewById(R.id.all_clients_amount);
        TextView allItemOrderedAmount = (TextView) dialogView.findViewById(R.id.all_items_amount);
        TextView allTotal = (TextView) dialogView.findViewById(R.id.all_total);

        allCatalogsAmount.setText(c.getString(0));
        c.moveToNext();
        allClientsAmount.setText(c.getString(0));
        c.moveToNext();
        allItemOrderedAmount.setText(c.getString(0));
        c.moveToNext();
        allTotal.setText(c.getString(0));

        c.close();
        myDB.close();
        builder.setView(dialogView);
        builder.show();
    }


    private void dialogExportImportBuilder() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_export_import, null);

        Button importDBButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_db_import);
        Button exportDBButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_db_export);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.dialog_ie_radio_group);
        Button importTableButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_folder_import);
        Button exportTableButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_folder_export);

        importDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManagement.importExportDB(findViewById(R.id.drawerLayout), IMPORT, getPackageName());
                        builder.dismiss();
                    }
                }).start();
            }
        });

        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManagement.importExportDB(findViewById(R.id.drawerLayout), EXPORT, getPackageName());
                        builder.dismiss();
                    }
                }).start();
            }
        });

        importTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.pb);
                progressBar.setVisibility(View.VISIBLE);
                Thread importTXT = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManagement.importTXT(dialogView.findViewById(R.id.dialog_import_export), getBaseContext(), fileName, tableName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
                importTXT.start();
            }
        });

        exportTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManagement.generateTXT(dialogView.findViewById(R.id.dialog_import_export), getBaseContext(), fileName, tableName);
                    }
                }).start();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case (R.id.dialog_ie_radio_items):
                        fileName = "Items.txt";
                        tableName = Database.TABLE_ITEMS;
                        break;
                    case (R.id.dialog_ie_radio_persons):
                        fileName = "Persons.txt";
                        tableName = Database.TABLE_PERSONS;
                        break;
                    case (R.id.dialog_ie_radio_catalogs):
                        fileName = "Catalogs.txt";
                        tableName = Database.TABLE_CATALOGS;
                        break;
                }
            }
        });
        builder.setView(dialogView);
        builder.show();
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rewardAmount = Integer.parseInt(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
        rewardAmount++;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("pref_edit_text_rewardAmount", rewardAmount.toString());
        editor.apply();

    }


    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "Added points: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rewardAmount = Integer.parseInt(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
        rewardAmount = rewardAmount + rewardItem.getAmount();

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
    }
}



