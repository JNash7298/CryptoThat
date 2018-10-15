package com.chat.crypto.johnathannash.cryptothat.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.chat.crypto.johnathannash.cryptothat.adapters.MessageAdapter;
import com.chat.crypto.johnathannash.cryptothat.models.MemberDataModel;
import com.chat.crypto.johnathannash.cryptothat.models.MessageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

public class ScaledroneManager implements RoomListener {

    private static final String TAG = "ScaledroneManager";
    private Scaledrone scaledrone;
    private String channelId, roomName;
    private MemberDataModel user;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private Activity activity;

    public ScaledroneManager(String channelId,
                             MemberDataModel user,
                             final String roomName,
                             Context context,
                             ListView messagesView,
                             Activity activity){

        this.channelId = channelId;
        this.roomName = roomName;
        this.user = user;
        this.messagesView = messagesView;
        this.activity = activity;

        messageAdapter = new MessageAdapter(context);
        messagesView.setAdapter(messageAdapter);

        scaledrone = new Scaledrone(channelId, user);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                Log.d(TAG, "Scaledrone connection open");
                scaledrone.subscribe(roomName, ScaledroneManager.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Connected to room " + room.getName());
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberDataModel data = mapper.treeToValue(member.getClientData(), MemberDataModel.class);
            boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
            final MessageModel messageModel = new MessageModel(json.asText(), data, belongsToCurrentUser);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(messageModel);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        if(message.length() > 0){
            scaledrone.publish(roomName, message);
        }
    }
}
