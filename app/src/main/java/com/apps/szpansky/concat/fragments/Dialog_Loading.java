package com.apps.szpansky.concat.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.szpansky.concat.R;

public class Dialog_Loading extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, null);

        this.setCancelable(false);
        this.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        return view;
    }
}