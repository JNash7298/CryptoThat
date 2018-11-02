package com.chat.crypto.johnathannash.cryptothat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.adapters.MessageAdapter;
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.MessageData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;

public class MessageActivity extends AppCompatActivity{

    private static final String TAG = "MessageActivity";
    private UserPublicData userData, contactData;
    private String room;
    private FirebaseDBHandler dbHandler;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userData = getIntent().getParcelableExtra("user_data");
        contactData = getIntent().getParcelableExtra("contact_data");
        room = getIntent().getStringExtra("room");

        setupEvent();
        messageList = findViewById(R.id.message_MessageRecycleView);
        messageList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void messageSelected(MessageData messageData){

    }

    private void setupEvent(){
        findViewById(R.id.message_MessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEvent(v);
            }
        });
    }

    private void buttonEvent(View view){
        switch (view.getId()){
            case R.id.message_MessageButton:
                break;
        }
    }
}
