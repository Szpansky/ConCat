package com.apps.szpansky.concat.dialog_fragments;


import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.R;

public class Loading extends DialogFragment {

    Activity activity;
    FragmentManager fragmentManager;
    int orientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // method for sdk 16
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_0)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        activity = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
    }


    public static Loading newInstance() {
        Loading loading = new Loading();

        loading.setStyle(STYLE_NO_TITLE, R.style.LoadingDialog);
        loading.setCancelable(false);

        return loading;
    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Loading loading = (Loading) fragmentManager.findFragmentByTag("Loading");

        if (orientation == activity.getWindowManager().getDefaultDisplay().getRotation()) {     //that loop prevents to change orientation when there's still some loading fragment
            if (loading != null) {
                if (!loading.isVisible()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);     //activity is fild in that class for prevent null point exception
                }
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);


        return view;
    }
}
