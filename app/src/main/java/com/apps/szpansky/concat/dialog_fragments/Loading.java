package com.apps.szpansky.concat.dialog_fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.R;

public class Loading extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // method for sdk 16
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation()== Surface.ROTATION_0)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation()== Surface.ROTATION_90)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getActivity().getWindowManager().getDefaultDisplay().getRotation()== Surface.ROTATION_270)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    public static Loading newInstance() {
        Loading loading = new Loading();

        loading.setStyle(STYLE_NO_TITLE, R.style.LoadingDialog);
        loading.setCancelable(false);

        return loading;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);


        return view;
    }
}
