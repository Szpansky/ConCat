package com.apps.szpansky.concat.tools;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
            days = (dy > 0) ? dy + ""  : "0";

            if ((months + days).equals(""))
                return "0 0";
            else
                return (months + " " + days);
        }
    }

    public static int setStyle(String styleKey, SharedPreferences sharedPreferences, String[] colorsKey){

        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[0])){
            return  (R.style.DefaultTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[1])){
            return  (R.style.OpenAllTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[2])){
            return  (R.style.BrowsingTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[3])){
            return  (R.style.PickTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[4])){
            return  (R.style.BlueTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[5])){
            return  (R.style.GreenTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[6])){
            return  (R.style.RedTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[7])){
            return  (R.style.PurpleTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[8])){
            return  (R.style.PinkTheme);
        }else
        if(sharedPreferences.getString(styleKey,"0").equals(colorsKey[9])){
            return  (R.style.GrayTheme);
        }else
            return (R.style.DefaultTheme);
    }


    }

