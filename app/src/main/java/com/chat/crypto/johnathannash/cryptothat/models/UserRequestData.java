package com.chat.crypto.johnathannash.cryptothat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserRequestData {

    Map<String, String> request_message;

    public UserRequestData(){

    }

    public Map<String, String> getRequest_message() {
        return request_message;
    }

    public void setRequest_message(Map<String, String> request_message) {
        this.request_message = request_message;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("request_message", request_message);

        return result;
    }
}
