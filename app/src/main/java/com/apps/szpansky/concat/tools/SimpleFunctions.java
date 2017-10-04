package com.apps.szpansky.concat.tools;


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




    }

