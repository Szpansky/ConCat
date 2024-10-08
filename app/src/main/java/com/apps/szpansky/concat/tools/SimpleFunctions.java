package com.apps.szpansky.concat.tools;

import android.content.SharedPreferences;

import com.apps.szpansky.concat.R;

import java.util.Calendar;

public final class SimpleFunctions {


    public static String fillWithZeros(String value, int length) {
        while (value.length() < length) {
            value = "0" + value;
        }
        return value;
    }


    public static String getTimeLeft(String date) { // dateFormat = "yyyy-MM-dd"
        String[] DateSplit = date.split("-");
        int month = Integer.parseInt(DateSplit[1]) - 1, // if month is november  then subtract by 1
                year = Integer.parseInt(DateSplit[0]), day = Integer
                .parseInt(DateSplit[2]);
        Calendar now = Calendar.getInstance();

        int dy = day - Calendar.getInstance().get(Calendar.DATE),
                mnth = month - Calendar.getInstance().get(Calendar.MONTH),
                daysinmnth = 32 - dy;

        Calendar end = Calendar.getInstance();
        end.set(year, month, day);

        if (mnth != 0) {
            if (dy != 0) {
                if (dy < 0) {
                    dy = (dy + daysinmnth) % daysinmnth;
                    mnth--;
                }
                if (mnth < 0) {
                    mnth = (mnth + 12) % 12;
                }
            }
        }

        if (now.after(end)) {
            return "0 0";
        } else {
            String months, days;
            months = (mnth > 0) ? mnth + "" : "0";
            days = (dy > 0) ? dy + "" : "0";

            if ((months + days).equals(""))
                return "0 0";
            else
                return (months + " " + days);
        }
    }

    public static int getStyleFromSharedPref(String styleKey, SharedPreferences sharedPreferences) {
        switch (sharedPreferences.getString(styleKey, "0")) {
            case ("DefaultTheme"):
                return R.style.DefaultTheme;
            case ("DefaultOpenAllTheme"):
                return R.style.OpenAllTheme;
            case ("DefaultBrowsingTheme"):
                return R.style.BrowsingTheme;
            case ("DefaultPickTheme"):
                return R.style.PickTheme;
            case ("BlueTheme"):
                return R.style.BlueTheme;
            case ("GreenTheme"):
                return R.style.GreenTheme;
            case ("RedTheme"):
                return R.style.RedTheme;
            case ("PurpleTheme"):
                return R.style.PurpleTheme;
            case ("PinkTheme"):
                return R.style.PinkTheme;
            case ("GrayTheme"):
                return R.style.GrayTheme;
            default:
                return R.style.DefaultTheme;
        }
    }


    public static int getBackgroundColor(String styleKey, SharedPreferences sharedPreferences) {
        switch (sharedPreferences.getString(styleKey, "0")) {
            case ("DefaultTheme"):
                return R.color.colorPrimary_light;
            case ("DefaultOpenAllTheme"):
                return R.color.colorPrimaryForOpenAll_light;
            case ("DefaultBrowsingTheme"):
                return R.color.colorPrimaryForBrowsing_light;
            case ("DefaultPickTheme"):
                return R.color.colorPrimaryForPick_light;
            case ("BlueTheme"):
                return R.color.bluePrimary_light;
            case ("GreenTheme"):
                return R.color.greenPrimary_light;
            case ("RedTheme"):
                return R.color.redPrimary_light;
            case ("PurpleTheme"):
                return R.color.purplePrimary_light;
            case ("PinkTheme"):
                return R.color.pinkPrimary_light;
            case ("GrayTheme"):
                return R.color.greyPrimary_light;
            default:
                return R.color.colorPrimary_light;
        }
    }


    public static int getPrimaryColor(String styleKey, SharedPreferences sharedPreferences) {
        switch (sharedPreferences.getString(styleKey, "0")) {
            case ("DefaultTheme"):
                return R.color.colorPrimary;
            case ("DefaultOpenAllTheme"):
                return R.color.colorPrimaryForOpenAll;
            case ("DefaultBrowsingTheme"):
                return R.color.colorPrimaryForBrowsing;
            case ("DefaultPickTheme"):
                return R.color.colorPrimaryForPick;
            case ("BlueTheme"):
                return R.color.bluePrimary;
            case ("GreenTheme"):
                return R.color.greenPrimary;
            case ("RedTheme"):
                return R.color.redPrimary;
            case ("PurpleTheme"):
                return R.color.purplePrimary;
            case ("PinkTheme"):
                return R.color.pinkPrimary;
            case ("GrayTheme"):
                return R.color.greyPrimary;
            default:
                return R.color.colorPrimary;
        }
    }


    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Integer year_x = calendar.get(Calendar.YEAR);
        Integer month_x = calendar.get(Calendar.MONTH) + 1;
        Integer day_x = calendar.get(Calendar.DAY_OF_MONTH);
        String day = day_x.toString();
        day = fillWithZeros(day, 2);
        String month = month_x.toString();
        month = fillWithZeros(month, 2);
        return year_x + "-" + month + "-" + day;
    }
}

