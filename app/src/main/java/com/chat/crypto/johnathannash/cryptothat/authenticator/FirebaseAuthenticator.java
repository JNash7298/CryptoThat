package com.chat.crypto.johnathannash.cryptothat.authenticator;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.chat.crypto.johnathannash.cryptothat.activities.LoginActivity;
import com.chat.crypto.johnathannash.cryptothat.activities.RegisterActivity;
import com.chat.crypto.johnathannash.cryptothat.interfaces.Authentication;
import com.chat.crypto.johnathannash.cryptothat.models.LoginDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthenticator implements Authentication{

    private Activity activity;
    private FirebaseAuth auth;
    private boolean loginIsSuccessful = false;
    private FirebaseUser currentUser = null;

    public FirebaseAuthenticator(Activity activity) {
        super();
        this.activity = activity;
    }

    private FirebaseAuthenticator(){
        auth = FirebaseAuth.getInstance();
    }

    public boolean loginWithEmailPassword(Activity activity, LoginDataModel dataModel){

        auth.signInWithEmailAndPassword(dataModel.getEmail(), dataModel.getPassWord())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        checkSuccessful(task);
                    }
                });
        return loginIsSuccessful;
    }

    public void checkCurrentUser() {
        currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    public FirebaseUser getCurrentUser(){
        return currentUser;
    }

    private void checkSuccessful(Task<AuthResult> task){
        if (loginIsSuccessful = task.isSuccessful()) {
            FirebaseUser user = auth.getCurrentUser();
            updateUI(user);
        } else {
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user){
        if(activity instanceof LoginActivity){
            ((LoginActivity) activity).updateUI(user);
        }
        else if(activity instanceof RegisterActivity){
            ((RegisterActivity) activity).updateUI(user);
        }
    }
}
