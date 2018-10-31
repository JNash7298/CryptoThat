package com.chat.crypto.johnathannash.cryptothat.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserPrivateData implements Parcelable {

    Map<String, String> contacts;

    public UserPrivateData(){

    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("contacts", contacts);

        return result;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = true;

        if(obj instanceof UserPrivateData){
            if(((UserPrivateData) obj).contacts.size() != contacts.size()){
                isEqual = false;
            }
            if(isEqual){
                for (String id: ((UserPrivateData) obj).getContacts().keySet()) {
                    if(isEqual){
                        if(contacts.containsKey(id)){
                            if(!contacts.get(id).equals(((UserPrivateData) obj).contacts.get(id))){
                                isEqual = false;
                            }
                        }
                        else{
                            isEqual = false;
                        }
                    }
                }
            }
        }
        else {
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
        dest.writeInt(this.contacts.size());
        for (Map.Entry<String, String> entry : this.contacts.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected UserPrivateData(Parcel in) {
        int contactsSize = in.readInt();
        this.contacts = new HashMap<String, String>(contactsSize);
        for (int i = 0; i < contactsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.contacts.put(key, value);
        }
    }

    public static final Parcelable.Creator<UserPrivateData> CREATOR = new Parcelable.Creator<UserPrivateData>() {
        @Override
        public UserPrivateData createFromParcel(Parcel source) {
            return new UserPrivateData(source);
        }

        @Override
        public UserPrivateData[] newArray(int size) {
            return new UserPrivateData[size];
        }
    };
}
