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


import com.apps.szpansky.concat.main_browsing.CatalogsActivity;
import com.apps.szpansky.concat.open_all.OpenAllItemsActivity;
import com.apps.szpansky.concat.open_all.OpenAllPersonsActivity;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.FileManagement;
import com.apps.szpansky.concat.tools.NetworkFunctions;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    public static boolean LOGGED = false;
    public static Integer rewardAmount;

    private static boolean FLOATING_MENU = false;
    private static String styleKey = "list_preference_main_colors";

    private Database myDB = new Database(this);

    private FloatingActionButton fabMain, fabNewCatalog, fabNewPerson, fabNewItem;

    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private GridLayout subFloatingMenu;
    View navViewHeader;

    //private AdView mAdView;
    private RewardedVideoAd mAd;
    AlertDialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setTheme(SimpleFunctions.setStyle(styleKey,sharedPreferences));

        setContentView(R.layout.activity_main);

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
        //mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
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
        final Dialogs dialogs = new Dialogs();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuLogin):
                        dialogs.dialogLoginBuilder(builder = new AlertDialog.Builder(MainActivity.this).create());
                        break;
                    case (R.id.menuMyAccount):
                        dialogs.dialogInformationBuilder(builder = new AlertDialog.Builder(MainActivity.this).create());
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
                        dialogs.dialogExportImportBuilder(builder = new AlertDialog.Builder(MainActivity.this).create());
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

            }
        });

        fabNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                daysLeft = getResources().getString(R.string.error);
                monthsLeft = getResources().getString(R.string.error);

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
    }


    private class Dialogs {
        private String tableName = Database.TABLE_ITEMS;    //default exported/imported content
        private String fileName = "Items.txt";

        private void showProgressBar(boolean active, ProgressBar progressBar, View dialogLayout, AlertDialog builder) {
            if (active) {
                progressBar.setVisibility(View.VISIBLE);
                dialogLayout.setVisibility(View.GONE);
                builder.setCancelable(false);
            } else {
                progressBar.setVisibility(View.GONE);
                dialogLayout.setVisibility(View.VISIBLE);
                builder.setCancelable(true);
            }
        }


        public void dialogExportImportBuilder(final AlertDialog builder) {
            final View dialogView = getLayoutInflater().inflate(R.layout.dialog_export_import, null);
            final View dialogLayout = dialogView.findViewById(R.id.dialog_import_export);
            final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.dialog_import_export_pb);
            final String appName = getBaseContext().getResources().getString(R.string.app_name);
            Button importDBButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_db_import);
            Button exportDBButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_db_export);
            final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.dialog_ie_radio_group);
            Button importTableButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_folder_import);
            Button exportTableButton = (Button) dialogView.findViewById(R.id.dialog_ie_button_folder_export);
            final boolean EXPORT = true;
            final boolean IMPORT = false;

            importDBButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar(true, progressBar, dialogLayout, builder);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (FileManagement.importExportDB(IMPORT, getPackageName(), appName)) {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.backup_error, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressBar(false, progressBar, dialogLayout, builder);
                                }
                            });
                        }
                    }).start();
                }
            });

            exportDBButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar(true, progressBar, dialogLayout, builder);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (FileManagement.importExportDB(EXPORT, getPackageName(), appName)) {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.error_notify, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressBar(false, progressBar, dialogLayout, builder);
                                }
                            });
                        }
                    }).start();
                }
            });

            importTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar(true, progressBar, dialogLayout, builder);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (FileManagement.importTXT(fileName, tableName, appName, myDB)) {
                                Snackbar snackbarInfo = Snackbar.make(dialogView, getResources().getString(R.string.updated) + FileManagement.getUpdated() + getResources().getString(R.string.created) + FileManagement.getCreated(), Snackbar.LENGTH_SHORT);
                                snackbarInfo.show();
                            } else {
                                Snackbar snackbarInfo = Snackbar.make(dialogView, R.string.file_does_not_exists, Snackbar.LENGTH_SHORT);
                                snackbarInfo.show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressBar(false, progressBar, dialogLayout, builder);
                                }
                            });
                        }
                    }).start();
                }
            });

            exportTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar(true, progressBar, dialogLayout, builder);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (FileManagement.generateTXT(fileName, tableName, appName, myDB)) {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.successfully_notify, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(dialogView, R.string.error_notify, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressBar(false, progressBar, dialogLayout, builder);
                                }
                            });
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


        private void dialogInformationBuilder(final AlertDialog builder) {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_information, null);
            final int textViewForFirstTableCount = 8;


            TextView[] textViews = new TextView[12];
            textViews[0] = (TextView) dialogView.findViewById(R.id.current_catalogs_number);
            textViews[1] = (TextView) dialogView.findViewById(R.id.current_clients_amount);
            textViews[2] = (TextView) dialogView.findViewById(R.id.current_ordered_item_amount);
            textViews[3] = (TextView) dialogView.findViewById(R.id.current_pcs_ordered);
            textViews[4] = (TextView) dialogView.findViewById(R.id.current_not_payed_amount);
            textViews[5] = (TextView) dialogView.findViewById(R.id.current_payed_amount);
            textViews[6] = (TextView) dialogView.findViewById(R.id.current_ready_amount);
            textViews[7] = (TextView) dialogView.findViewById(R.id.current_total);

            textViews[8] = (TextView) dialogView.findViewById(R.id.all_catalogs_amount);
            textViews[9] = (TextView) dialogView.findViewById(R.id.all_clients_amount);
            textViews[10] = (TextView) dialogView.findViewById(R.id.all_items_amount);
            textViews[11] = (TextView) dialogView.findViewById(R.id.all_total);

            Button backButton = (Button) dialogView.findViewById(R.id.dialog_info_backBtn);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });

            Cursor c = myDB.getCurrentCatalogInfo();

            for (int i = 0; i < textViewForFirstTableCount; i++) {
                textViews[i].setText(c.getString(i));
            }

            c = myDB.getCurrentInfo();

            for (int i = textViewForFirstTableCount; i < textViews.length; i++) {
                textViews[i].setText(c.getString(0));
                if (!c.isLast()) c.moveToNext();
            }

            c.close();
            myDB.close();
            builder.setView(dialogView);
            builder.show();
        }


        private void dialogLoginBuilder(AlertDialog builder) {
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
            builder.show();
        }
    }
}



