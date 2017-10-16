package com.apps.szpansky.concat.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.tools.Data;


public class Dialog_DeletingData extends DialogFragment {

    public Dialog_DeletingData newInstance(Data data) {
        Dialog_DeletingData deletingData = new Dialog_DeletingData();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        deletingData.setArguments(bundle);
        deletingData.setStyle(STYLE_NO_TITLE, 0);

        return deletingData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_popup_alert, null);

        final Data data = (Data) getArguments().getSerializable("data");
        Button buttonYes = (Button) view.findViewById(R.id.dialog_button_yes);
        Button buttonNo = (Button) view.findViewById(R.id.dialog_button_no);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.deleteData(data.getClickedItemId());
                dismiss();
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
