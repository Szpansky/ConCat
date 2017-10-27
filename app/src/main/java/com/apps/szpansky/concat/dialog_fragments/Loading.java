package com.apps.szpansky.concat.dialog_fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.R;

public class Loading extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    public static Loading newInstance() {
        Loading loading = new Loading();

        loading.setStyle(STYLE_NO_TITLE, 0);
        loading.setCancelable(false);

        return loading;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);

        this.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        return view;
    }
}
