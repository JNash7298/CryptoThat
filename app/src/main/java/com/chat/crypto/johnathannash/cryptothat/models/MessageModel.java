package com.chat.crypto.johnathannash.cryptothat.models;

public class MessageModel {
    private String text;
    private MemberDataModel data;
    private boolean belongsToCurrentUser;

    public MessageModel(String text, MemberDataModel data, boolean belongsToCurrentUser) {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public MemberDataModel getData() {
        return data;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
