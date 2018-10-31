package com.chat.crypto.johnathannash.cryptothat.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserRequestData implements Parcelable {

    @Exclude
    Map<String, String> message_request;

    public UserRequestData(){

    }

    public Map<String, String> getMessage_request() {
        return message_request;
    }

    public void setMessage_request(Map<String, String> message_request) {
        this.message_request = message_request;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("message_request", message_request);

        return result;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = true;

        if(obj instanceof UserRequestData){
            if(((UserRequestData) obj).message_request.size() != message_request.size()){
                isEqual = false;
            }
            if(isEqual){
                for (String id : ((UserRequestData) obj).message_request.keySet()) {
                    if(isEqual){
                        if(message_request.containsKey(id)){
                            if(!message_request.get(id).equals(((UserRequestData) obj).message_request.get(id))){
                                isEqual = false;
                            }
                        }
                        else {
                            isEqual = false;
                        }
                    }
                }
            }
        }
        else{
            isEqual = false;
        }

        return isEqual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.message_request.size());
        for (Map.Entry<String, String> entry : this.message_request.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected UserRequestData(Parcel in) {
        int request_messageSize = in.readInt();
        this.message_request = new HashMap<String, String>(request_messageSize);
        for (int i = 0; i < request_messageSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.message_request.put(key, value);
        }
    }

    public static final Parcelable.Creator<UserRequestData> CREATOR = new Parcelable.Creator<UserRequestData>() {
        @Override
        public UserRequestData createFromParcel(Parcel source) {
            return new UserRequestData(source);
        }

        @Override
        public UserRequestData[] newArray(int size) {
            return new UserRequestData[size];
        }
    };
}
