package com.apps.szpansky.concat.dialog_fragments;

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


public class DeletingData extends DialogFragment {

    public static DeletingData newInstance(Data data) {
        DeletingData deletingData = new DeletingData();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        deletingData.setArguments(bundle);
        deletingData.setStyle(STYLE_NO_TITLE, 0);

        return deletingData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = (ViewGroup) inflater.inflate(R.layout.dialog_deleting, container, false);

        final Data data = (Data) getArguments().getSerializable("data");
        Button buttonYes = view.findViewById(R.id.dialog_button_yes);
        Button buttonNo = view.findViewById(R.id.dialog_button_no);
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
