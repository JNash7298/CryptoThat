package com.chat.crypto.johnathannash.cryptothat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RoomMemberData {

    @Exclude
    String roomName;

    Map<String, Boolean> room;

    public RoomMemberData(){
    }

    @Exclude
    public String getRoomName() {
        return roomName;
    }
    @Exclude
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Map<String, Boolean> getRoom() {
        return room;
    }

    public void setRoom(Map<String, Boolean> room) {
        this.room = room;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(roomName, room);

        return result;
    }
}
