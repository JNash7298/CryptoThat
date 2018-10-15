package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chat.crypto.johnathannash.cryptothat.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupEvents();
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
        }

        if(intent != null){
            startActivity(intent);
        }
    }
}
