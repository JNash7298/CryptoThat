package com.chat.crypto.johnathannash.cryptothat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chat.crypto.johnathannash.cryptothat.R;

public class ForgotPasswordSubmitEmailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password_submit_email, container, false);
    }

    private void setupEvents(){
        Button tempButton = getActivity().findViewById(R.id.forgotPassword_EmailSubmitButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressEvent(v);
            }
        });
    }

    private void buttonPressEvent(View view){
        switch (view.getId()){
            case R.id.forgotPassword_EmailEntryField:
                sendEmail();
                break;
        }
    }

    private void sendEmail(){
        EditText tempText = getActivity().findViewById(R.id.forgotPassword_EmailEntryField);

    }
}
