package com.chat.crypto.johnathannash.cryptothat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordSubmitEmailFragment extends Fragment {

    private View view;
    private String email;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle retrieval = this.getArguments();

        if (retrieval != null) {
            email = retrieval.getString("email");
        }

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password_submit_email, container, false);
        setupEvents();
        return view;
    }

    private void setupEvents(){
        Button tempButton = view.findViewById(R.id.forgotPassword_EmailSubmitButton);
        EditText tempText = view.findViewById(R.id.forgotPassword_EmailEntryField);

        tempText.setText(email);

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressEvent(v);
            }
        });
        tempButton = view.findViewById(R.id.forgotPassword_ReturnButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressEvent(v);
            }
        });

        tempText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editEmailText(s.toString());
            }
        });
    }

    private void buttonPressEvent(View view){
        switch (view.getId()){
            case R.id.forgotPassword_EmailSubmitButton:
                sendEmail();
                break;
            case R.id.forgotPassword_ReturnButton:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void editEmailText(String email){
        this.email = email;
    }

    private void sendEmail(){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Reset password email send to " + email + ".", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
