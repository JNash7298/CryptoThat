package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupEvents();
        user = getIntent().getParcelableExtra("user");
        auth = FirebaseAuth.getInstance();
    }

    private void setupEvents(){
        LinearLayout navBar = findViewById(R.id.home_NavagationField);
        for(int item = 0; item < navBar.getChildCount(); item++){
            navBar.getChildAt(item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressEvent(v);
                }
            });
        }
    }

    private void buttonPressEvent(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.home_CipherButton:
                break;
            case R.id.home_MessageButton:
                intent = new Intent(this, MessageActivity.class);
                break;
            case R.id.home_SettingButtton:
                break;
            case R.id.home_LogoutButton:
                auth.signOut();
                intent = new Intent(this, LoginActivity.class);
                break;
        }

        if(intent != null){
            startActivity(intent);
        }
    }
}
