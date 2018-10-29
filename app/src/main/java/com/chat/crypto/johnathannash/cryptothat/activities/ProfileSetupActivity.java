package com.chat.crypto.johnathannash.cryptothat.activities;

import android.app.Dialog;
import android.arch.core.util.Function;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileSetupActivity extends AppCompatActivity {

    private boolean unverified, oneWay;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView userName, email;
    private static final String TAG = "profileSetup";
    private FirebaseDBHandler dbHandler;
    private UserPublicData userData;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        unverified = getIntent().getBooleanExtra("Unverified", false);
        oneWay = getIntent().getBooleanExtra("OneWay", false);
        user = getIntent().getParcelableExtra("user");
        auth = FirebaseAuth.getInstance();
        dbHandler = new FirebaseDBHandler();

        dbHandler.retrievePublicUserData(this);
        setup();
    }

    public void ProvideUserData(UserPublicData userData){
        this.userData = userData;
    }

    private void setup(){
        email = findViewById(R.id.profileSetup_EmailAddressString);
        userName = findViewById(R.id.profileSetup_ProfileName);
        avatar = findViewById(R.id.profileSetup_ProfileImage);

        email.setText(user.getEmail());
        userName.setText(user.getDisplayName());
        if(userData == null){
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.temp_profileColor) & 0x00ffffff);
            avatar.setBackground(new ColorDrawable(Color.parseColor(colorHex)));
        }

        ConstraintLayout temp = findViewById(R.id.profileSetup_Profile);
        for(int index = 0; index < temp.getChildCount(); index++){
            View tempView = temp.getChildAt(index);
            if(tempView instanceof Button){
                tempView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonPress(v);
                    }
                });
            }
        }

        if(unverified){
            ((Button)findViewById(R.id.profileSetup_ReturnButton)).setText(R.string.profileSetup_VerifyButton);
        }
    }

    private void buttonPress(View view){
        switch (view.getId()){
            case R.id.profileSetup_ChangeEmailButton:
                reauthenticateEmailUpdate();
                break;
            case R.id.profileSetup_ChangePasswordButton:
                break;
            case R.id.profileSetup_ChangeUsernameButton:
                requestNewUserName();
                break;
            case R.id.profileSetup_ProfileImageChangeButton:
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profileSetup_ReturnButton:
                if(unverified){
                    verifyEmail();
                }
                else{
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void reauthenticateEmailUpdate() {
        final Dialog relogin = new Dialog(this);
        relogin.setContentView(R.layout.relogin_popup);
        relogin.findViewById(R.id.reloginPopup_LoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider.getCredential(
                        ((EditText) relogin.findViewById(R.id.reloginPopup_Email)).getText().toString()
                        , ((EditText) relogin.findViewById(R.id.reloginPopup_Password)).getText().toString());
                user = auth.getCurrentUser();
                if (user != null) {
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                requestNewEmail();
                            }
                            relogin.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(ProfileSetupActivity.this, "Unable to reauthenticate. Please login back in and try again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileSetupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        relogin.findViewById(R.id.relogin_ForgotPassword).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
        relogin.show();
    }

    private void forgotPassword(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra("recommendedEmail", user.getEmail());
        startActivity(intent);
    }

    private void requestNewEmail(){
        final Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.change_email_popup);

        popup.findViewById(R.id.changeEmailPopup_SubmitButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateEmail(popup, user);
                    }
                });
        popup.show();
    }

    private void requestNewUserName(){
        final Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.change_username_popup);

        popup.findViewById(R.id.changeUsernamePopup_SubmitButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateUsername(popup, user);
                    }
                });
        popup.show();
    }

    private void updateEmail(final Dialog popup, final FirebaseUser user){
        user.updateEmail(
                ((EditText)popup.findViewById(R.id.changeEmailPopup_NewEmail))
                        .getText()
                        .toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateEmailResponse(popup, user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        message(e);
                        Log.d(TAG, e.toString());
                    }
                });
    }

    private void updateUsername(final Dialog popup, final FirebaseUser user){
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(
                        ((EditText)popup.findViewById(R.id.changeUsernamePopup_UsernameTextField))
                        .getText().toString())
                .build();
        user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUsernameResponse(popup, user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message(e);
                Log.d(TAG, e.toString());
            }
        });
    }

    private void message(Exception e){
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void updateEmailResponse(Dialog popup, FirebaseUser user){
        if(!email.getText().equals(user.getEmail())){
            Toast.makeText(this,
                    "Email was set to " + user.getEmail()
                    , Toast.LENGTH_SHORT).show();
        }
        email.setText(user.getEmail());
        popup.dismiss();
    }

    private void updateUsernameResponse(Dialog popup, FirebaseUser user){
        if(!userName.getText().equals(user.getDisplayName())){
            Toast.makeText(this,
                    "Username was set to " + user.getDisplayName()
                    , Toast.LENGTH_SHORT).show();
        }
        userName.setText(user.getDisplayName());

        if(userData != null){
            userData.setName(user.getDisplayName());
            dbHandler.updatePublicUserData(userData);
        }
        popup.dismiss();
    }
    private void verifyEmail(){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileSetupActivity.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                    UserPublicData initial = new UserPublicData();
                    initial.setName(user.getDisplayName());
                    initial.setAvatar("#" + Integer.toHexString(ContextCompat.getColor(ProfileSetupActivity.this, R.color.temp_profileColor) & 0x00ffffff));
                    initial.setUid(user.getUid());
                    dbHandler.setUserPublicData(initial);

                    Intent intent = new Intent(ProfileSetupActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(ProfileSetupActivity.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!oneWay && !user.isEmailVerified()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(!user.isEmailVerified()){
            auth.signOut();
            oneWay = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!user.isEmailVerified()){
            auth.signOut();
        }
    }
}
