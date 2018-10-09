package com.chat.crypto.johnathannash.cryptothat.interfaces;

import android.app.Activity;

import com.chat.crypto.johnathannash.cryptothat.models.LoginDataModel;
import com.google.firebase.auth.FirebaseUser;

public interface Authentication {
    boolean loginWithEmailPassword(Activity activity, LoginDataModel dataModel);
    void checkCurrentUser();
    FirebaseUser getCurrentUser();
}
