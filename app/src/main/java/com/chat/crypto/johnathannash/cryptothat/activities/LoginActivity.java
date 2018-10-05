package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chat.crypto.johnathannash.cryptothat.R;

public class LoginActivity extends AppCompatActivity {

    private View[] buttons = new View[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttons[0] = findViewById(R.id.LoginButton);
        buttons[1] = findViewById(R.id.login_RegisterButton);

        setupButtonEvents();
    }

    private void setupButtonEvents(){
        for(View button : buttons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressed(v);
                }
            });
        }
    }

    private void buttonPressed(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.LoginButton:
                break;
            case R.id.login_RegisterButton:
                intent = new Intent(this, RegisterActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }
}
