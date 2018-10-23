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
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = getIntent().getStringExtra("recommendedEmail");

        applyInitialFragment(email);
    }

    private void applyInitialFragment(String email){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        Fragment fragment = new ForgotPasswordSubmitEmailFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.forgotPassword_fragmentContainer, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
