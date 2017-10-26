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
import android.widget.TextView;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Order;


public class SelectCount_Item extends DialogFragment {

    Integer count = 0;

    public static SelectCount_Item newInstance(Order order) {
        SelectCount_Item addItemCount = new SelectCount_Item();

        Bundle bundle = new Bundle();
        bundle.putSerializable("order", order);
        addItemCount.setArguments(bundle);
        addItemCount.setStyle(STYLE_NO_TITLE, 0);

        return addItemCount;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_item_count, null);

        final Order order = (Order) getArguments().getSerializable("order");
        final String[] getCurrentData = order.getClickedItemData();

        Button plusMark = (Button) view.findViewById(R.id.plus_mark);
        Button minusMark = (Button) view.findViewById(R.id.minus_mark);
        Button okButton = (Button) view.findViewById(R.id.button_ok);
        final TextView textCount = (TextView) view.findViewById(R.id.number_of_items);
        textCount.setText(getCurrentData[3]);
        count = Integer.parseInt(getCurrentData[3]);


        plusMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCount = textCount.getText().toString();
                if (itemCount.equals("")) count = 0;
                else count = Integer.parseInt(itemCount) + 1;
                textCount.setText(count.toString());
            }
        });

        minusMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCount = textCount.getText().toString();
                if (itemCount.equals("")) count = 0;
                else count = Integer.parseInt(itemCount) - 1;
                textCount.setText(count.toString());
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCount = textCount.getText().toString();
                if (itemCount.equals("") || count < 1) count = 0;
                else count = Integer.parseInt(itemCount);
                String[] value = {
                        getCurrentData[2],
                        getCurrentData[1],
                        count.toString()
                };
                order.updateData(value, new String[]{});
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
