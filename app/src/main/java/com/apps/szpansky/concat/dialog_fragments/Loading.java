package com.apps.szpansky.concat.dialog_fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.R;

public class Loading extends DialogFragment {


    public static Loading newInstance() {
        Loading loading = new Loading();

        loading.setStyle(STYLE_NO_TITLE, 0);
        loading.setCancelable(false);

        return loading;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);

        this.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        return view;
    }
}
