package com.apps.szpansky.concat.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.dialog_fragments.Loading;
import com.apps.szpansky.concat.tools.Database;
import com.apps.szpansky.concat.tools.SimpleFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class Main extends Fragment implements RewardedVideoAdListener {

    private View view;
    private String styleKey = "list_preference_main_colors";

    TextView mainCatalogNr;
    TextView mainCatalogMonthsLeft;
    TextView mainCatalogDaysLeft;
    TextView mainCatalogPrice;
    TextView mainCatalogNotPayed;
    View infoView;
    private RewardedVideoAd mAd;
    private AdView mAdView;
    private Button openCatalogs;
    private Button startAds;
    private Loading loading = Loading.newInstance();
    private NavigationView navigationView;
    private View navViewHeader;


    public static Main newInstance() {
        Main main = new Main();

        return main;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getContext().setTheme(SimpleFunctions.setStyle(styleKey, sharedPreferences));

        view = inflater.inflate(R.layout.content_main, container, false);

        view.setBackgroundColor(ContextCompat.getColor(getContext(), SimpleFunctions.setBackgroundColor(styleKey, sharedPreferences)));


        setViews();

        inflateToolBar();

        refreshFragmentState();

        onButtonCLick();

        //setAds();

        return view;
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    private Toolbar toolbar;


    private void setViews() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        infoView = view.findViewById(R.id.main_layout_info);
        mainCatalogNr = (TextView) infoView.findViewById(R.id.main_order_number);
        mainCatalogMonthsLeft = (TextView) infoView.findViewById(R.id.main_order_months_left);
        mainCatalogDaysLeft = (TextView) infoView.findViewById(R.id.main_order_days_left);
        mainCatalogPrice = (TextView) infoView.findViewById(R.id.main_order_price);
        mainCatalogNotPayed = (TextView) infoView.findViewById(R.id.main_order_client_count);
        openCatalogs = (Button) view.findViewById(R.id.openCatalogs);
        startAds = (Button) view.findViewById(R.id.startAd);
        mAdView = (AdView) view.findViewById(R.id.adView);

        navigationView = (NavigationView) getActivity().findViewById(R.id.navView);
        navViewHeader = navigationView.getHeaderView(0);
    }


    private void inflateToolBar() {
        toolbar.setTitle(R.string.app_name);

        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_menu_black_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().openOptionsMenu();
            }
        });
    }


    public void refreshFragmentState() {
        Database myDB = new Database(getActivity());
        Cursor c = myDB.getCurrentCatalogInfo();

        if (c.isNull(0) || c.isNull(4) || c.isNull(7) || c.isNull(9)) {
            infoView.setVisibility(View.GONE);
        } else {
            infoView.setVisibility(View.VISIBLE);
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


    private void onButtonCLick() {
        openCatalogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO zmiana na druga strone

            }
        });

        startAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAd.loadAd(getResources().getString(R.string.ads_reward_main_id), new AdRequest.Builder().build());
                loading.show(getActivity().getFragmentManager().beginTransaction(), "Loading");
            }
        });
    }


    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mAd.setRewardedVideoAdListener(this);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mAd.show();
    }


    @Override
    public void onRewardedVideoAdOpened() {
        if (loading.isVisible()) loading.dismiss();
    }


    @Override
    public void onRewardedVideoStarted() {

    }


    @Override
    public void onRewardedVideoAdClosed() {
       /* Snackbar snackbarInfo = Snackbar.make(findViewById(R.id.drawerLayout), R.string.end, Snackbar.LENGTH_SHORT);
        snackbarInfo.show();
        addPoints(1);
        updateUserInfo();*/
        if (loading.isVisible()) loading.dismiss();
    }


    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(getActivity(), "Added points: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
        addPoints(rewardItem.getAmount());
        updateUserInfo();
    }

    private void updateUserInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView loggedAs = (TextView) navViewHeader.findViewById(R.id.navi_loggedAs);
        loggedAs.setText(sharedPreferences.getString("pref_edit_text_loggedAs", getResources().getString(R.string.pref_def_logged_as)));
        TextView rewardPoints = (TextView) navViewHeader.findViewById(R.id.navi_rewardAmount);
        rewardPoints.setText(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
    }

    private void addPoints(int amount) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer rewardAmount = Integer.parseInt(sharedPreferences.getString("pref_edit_text_rewardAmount", "0"));
        rewardAmount = rewardAmount + amount;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_edit_text_rewardAmount", rewardAmount.toString());
        editor.apply();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
        if (loading.isVisible()) loading.dismiss();
    }


    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Snackbar snackbarInfo = Snackbar.make(view, R.string.failed_to_load_ad, Snackbar.LENGTH_SHORT);
        snackbarInfo.show();
        if (loading.isVisible()) loading.dismiss();
    }


}
