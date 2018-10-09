package com.chat.crypto.johnathannash.cryptothat.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.fragments.ForgotPasswordSubmitEmailFragment;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        applyInitialFragment();
    }

    private void applyInitialFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new ForgotPasswordSubmitEmailFragment();
        fragmentTransaction.add(R.id.forgotPassword_fragmentContainer, fragment);

        fragmentTransaction.commit();
    }
}
