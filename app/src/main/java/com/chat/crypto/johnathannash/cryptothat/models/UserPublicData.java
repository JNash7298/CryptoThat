package com.chat.crypto.johnathannash.cryptothat.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserPublicData implements Parcelable {

    @Exclude
    String avatar, name, uid;

    public UserPublicData(){
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("avatar", avatar);

        return result;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        if(obj != null){
            if(obj instanceof UserPublicData){
                if(((UserPublicData) obj).getUid().equals(getUid())){
                    if(((UserPublicData)obj).getName().equals(getName())){
                        if(((UserPublicData) obj).getAvatar().equals(getAvatar())){
                            isEqual = true;
                        }
                    }
                }
            }
        }

        return isEqual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.uid);
    }

    protected UserPublicData(Parcel in) {
        this.avatar = in.readString();
        this.name = in.readString();
        this.uid = in.readString();
    }

    public static final Parcelable.Creator<UserPublicData> CREATOR = new Parcelable.Creator<UserPublicData>() {
        @Override
        public UserPublicData createFromParcel(Parcel source) {
            return new UserPublicData(source);
        }

        @Override
        public UserPublicData[] newArray(int size) {
            return new UserPublicData[size];
        }
    };
}
