package com.apps.szpansky.concat.dialog_fragments;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.apps.szpansky.concat.R;
import com.apps.szpansky.concat.simple_data.Client;
import com.apps.szpansky.concat.tools.Database;


public class UpdateStatus_Client extends DialogFragment {

    Client client;

    public static UpdateStatus_Client newInstance(Client client) {
        UpdateStatus_Client updateStatus = new UpdateStatus_Client();

        Bundle bundle = new Bundle();
        bundle.putSerializable("client", client);
        updateStatus.setArguments(bundle);
        updateStatus.setStyle(STYLE_NO_TITLE, 0);

        return updateStatus;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        client = (Client) getArguments().getSerializable("client");

        final View view = inflater.inflate(R.layout.dialog_on_client_click, container, false);

        Button saveCatalog = view.findViewById(R.id.buttonOrderSave);
        saveCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status;
                RadioGroup radioGroup = view.findViewById(R.id.radioGroupOrder);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case (R.id.radioButtonOrderNotPaid):
                        status = getString(R.string.db_status_not_payed);
                        break;
                    case (R.id.radioButtonOrderPaid):
                        status = getString(R.string.db_status_payed);
                        break;
                    case (R.id.radioButtonOrderReady):
                        status = getString(R.string.db_status_ready);
                        break;
                    default:
                        status = getString(R.string.db_status_not_payed);
                        break;
                }
                String[] keys = new String[]{Database.CLIENT_STATUS};
                String[] value = new String[]{status};
                client.updateData(value, keys);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
