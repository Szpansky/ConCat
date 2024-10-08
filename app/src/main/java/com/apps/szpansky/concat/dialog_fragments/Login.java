package com.apps.szpansky.concat.dialog_fragments;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apps.szpansky.concat.R;


public class Login extends DialogFragment {

    boolean isLogged;

    public static Login newInstance() {
        Login login = new Login();
        login.setStyle(STYLE_NO_TITLE, 0);
        return login;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_login, container, false);


        final EditText emailEditText = view.findViewById(R.id.dialog_login_email);
        final EditText passwordEditText = view.findViewById(R.id.dialog_login_password);
        Button loginButton = view.findViewById(R.id.dialog_login_loginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isLogged) {
                    Snackbar snackbar = Snackbar.make(view, R.string.coming_soon, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(view, R.string.coming_soon, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });

        return view;
    }
}
