package com.chat.crypto.johnathannash.cryptothat.activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.helpers.ScaledroneManager;
import com.chat.crypto.johnathannash.cryptothat.models.MemberDataModel;
import com.chat.crypto.johnathannash.cryptothat.models.MessageModel;
import com.chat.crypto.johnathannash.cryptothat.adapters.MessageAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.Random;

public class MessageActivity extends AppCompatActivity{

    private static final String CHANNEL_ID = "pnN50Uh85FrwkWlU";
    private static final String TAG = "MessageModel";
    private String roomName = "observable-tempRoom";
    private Scaledrone scaledrone;
    private ScaledroneManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setupEvent();
        ListView messagesView = findViewById(R.id.message_MessageField);

        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        String name= adjs[(int) Math.floor(Math.random() * adjs.length)] +
                "_" +
                nouns[(int) Math.floor(Math.random() * nouns.length)];
        MemberDataModel user = new MemberDataModel(name, sb.toString().substring(0, 7));


        manager = new ScaledroneManager(CHANNEL_ID, user, roomName, this, messagesView, this);
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
                sendMessage(view);
                break;
        }
    }

    public void sendMessage(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.temp_messsage_layout);

        Button button = dialog.findViewById(R.id.messagePopup_SendMessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox(dialog);
            }
        });

        dialog.show();
    }

    private void messageBox(Dialog box){
        String message = ((EditText)box.findViewById(R.id.messagePopup_MessageText)).getText().toString();
        manager.sendMessage(message);
        box.dismiss();
    }
}
