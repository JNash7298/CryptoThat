package com.chat.crypto.johnathannash.cryptothat.models;

public class RegisterDataModel {

    private String userName, passWord, confirmationPassWord, email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getConfirmationPassWord() {
        return confirmationPassWord;
    }

    public void setConfirmationPassWord(String confirmationPassWord) {
        this.confirmationPassWord = confirmationPassWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
