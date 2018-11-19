package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageActivity extends AppCompatActivity{

    private static final String TAG = "MessageActivity";
    private UserPublicData userData, contactData;
    private String room;
    private FirebaseDBHandler dbHandler;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private ValueEventListener roomListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        dbHandler = new FirebaseDBHandler();

        userData = getIntent().getParcelableExtra("user_data");
        contactData = getIntent().getParcelableExtra("contact_data");
        room = getIntent().getStringExtra("room");

        MessageData sendMessage = (MessageData) getIntent().getSerializableExtra("sendMessageData");
        if(sendMessage != null){
            sendMessage(sendMessage);
        }

        messageList = findViewById(R.id.message_MessageRecycleView);
        messageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        messageAdapter = new MessageAdapter(this, userData, contactData);
        messageList.setAdapter(messageAdapter);

        setupEvent();
    }

    public void messageSelected(MessageData messageData){
        Intent intent = new Intent(this, DecipherMessageActivity.class);
        intent.putExtra("user", userData);
        intent.putExtra("contact", contactData);
        intent.putExtra("room", room);
        intent.putExtra("message_data", messageData);

        startActivity(intent);
    }

    private void setupEvent(){
        ConstraintLayout buttonPanel = findViewById(R.id.message_ButtonControlPanel);
        for(int position = 0; position < buttonPanel.getChildCount(); position++) {
            buttonPanel.getChildAt(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonEvent(v);
                }
            });
        }

        roomListener = dbHandler.connectToMessageRoom(this, room);
    }

    private void sendMessage(MessageData messageData){
        dbHandler.pushMessageDataToMessage(room, messageData);
    }

    public void newMessage(List<MessageData> messageData){
        messageAdapter.addToBottom(messageData);
    }

    public LinearLayoutManager getManager(){
        return (LinearLayoutManager) messageList.getLayoutManager();
    }

    private void buttonEvent(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.message_MessageButton:
                intent = new Intent(this, EncipherMessageActivity.class);

                intent.putExtra("user", userData);
                intent.putExtra("contact", contactData);
                intent.putExtra("room", room);
                break;
            case R.id.message_BackButton:
                intent = new Intent(this, HomeActivity.class);
                break;
        }
        if(intent != null){
            dbHandler.removeMessageListener(room, roomListener);
            startActivity(intent);
        }
    }
}
