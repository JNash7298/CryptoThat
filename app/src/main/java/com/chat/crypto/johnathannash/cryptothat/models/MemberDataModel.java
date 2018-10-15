package com.chat.crypto.johnathannash.cryptothat.models;

public class MemberDataModel {
    private String name;
    private String color;

    public MemberDataModel(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberDataModel() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberDataModel{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
