package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chat.crypto.johnathannash.cryptothat.R;

public class RegisterActivity extends AppCompatActivity {

    private View[] buttons = new View[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttons[0] = findViewById(R.id.RegisterButton);
        buttons[1] = findViewById(R.id.register_ReturnToLoginButton);

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
            case R.id.RegisterButton:
                break;
            case R.id.register_ReturnToLoginButton:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }
}
