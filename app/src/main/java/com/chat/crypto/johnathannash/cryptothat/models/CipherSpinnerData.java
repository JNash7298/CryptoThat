package com.chat.crypto.johnathannash.cryptothat.models;

public class CipherSpinnerData {
    String cipherName, cipherId;

    public CipherSpinnerData(){

    }

    public CipherSpinnerData(String name, String id){
        this.cipherId = id;
        this.cipherName = name;
    }

    public String getCipherName() {
        return cipherName;
    }

    public void setCipherName(String cipherName) {
        this.cipherName = cipherName;
    }

    public String getCipherId() {
        return cipherId;
    }

    public void setCipherId(String cipherId) {
        this.cipherId = cipherId;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;

        if(obj instanceof CipherSpinnerData){
            if(((CipherSpinnerData) obj).getCipherName().equals(cipherName) && ((CipherSpinnerData) obj).getCipherId().equals(cipherId)){
                isEqual = true;
            }
        }

        return isEqual;
    }
}
