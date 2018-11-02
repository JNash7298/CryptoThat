package com.chat.crypto.johnathannash.cryptothat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MessageData {

    String sender, cipher_text, plain_text, cipher_id, key;
    Object timestamp;

    public MessageData(){
        timestamp = ServerValue.TIMESTAMP;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCipher_text() {
        return cipher_text;
    }

    public void setCipher_text(String cipher_text) {
        this.cipher_text = cipher_text;
    }

    public String getPlain_text() {
        return plain_text;
    }

    public void setPlain_text(String plain_text) {
        this.plain_text = plain_text;
    }

    public String getCipher_id() {
        return cipher_id;
    }

    public void setCipher_id(String cipher_id) {
        this.cipher_id = cipher_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public long getTimestamp() {
        return (long)timestamp;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("cipher_id", cipher_id);
        result.put("plain_text", plain_text);
        result.put("cipher_text", cipher_text);
        result.put("sender", sender);
        result.put("timestamp", timestamp);

        return result;
    }
}
