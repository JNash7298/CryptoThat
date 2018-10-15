package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.models.RegisterDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String TAG = "Register";
    private RegisterDataModel registerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupButtonEvents();
        setupFireBaseAuthentication();
    }

    private void setupButtonEvents(){
        final LinearLayout view = findViewById(R.id.register_UsernamePasswordEmailRegisterField);
        for(int child = 0; child < view.getChildCount(); child++){
            if(view.getChildAt(child) instanceof EditText){
                ((EditText) view.getChildAt(child)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        textChanged(view, s.toString());
                    }
                });
            }
            else {
                view.getChildAt(child).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonPressed(v);
                    }
                });
            }
        }
        findViewById(R.id.register_ReturnToLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(v);
            }
        });
    }

    private void setupFireBaseAuthentication(){
        auth = FirebaseAuth.getInstance();
        registerData = new RegisterDataModel();
    }

    private void textChanged(View view, String text){
        switch (view.getId()){
            case R.id.register_EmailTextEntry:
                registerData.setEmail(text);
                break;
            case R.id.register_PasswordTextEntry:
                registerData.setPassWord(text);
                break;
            case R.id.register_PasswordConfirmationTextEntry:
                registerData.setConfirmationPassWord(text);
                break;
            case R.id.register_UsernameTextEntry:
                registerData.setUserName(text);
                break;
        }
    }

    private void buttonPressed(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.RegisterButton:
                    registerAttempt();
                break;
            case R.id.register_ReturnToLoginButton:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    private void registerAttempt() {
        if(registerData.getUserName().isEmpty()){

        } else if(registerData.getEmail().isEmpty()){

        } else if(registerData.getPassWord().isEmpty()){

        } else if(registerData.getConfirmationPassWord().isEmpty()){

        } else if(registerData.getPassWord().equals(registerData.getConfirmationPassWord())){
            auth.createUserWithEmailAndPassword(registerData.getEmail(), registerData.getPassWord())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            checkIfSuccessfulRegister(task);
                        }
                    });
        } else{

        }
    }

    private void checkIfSuccessfulRegister(Task<AuthResult> task){
        if (task.isSuccessful()) {
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = auth.getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(registerData.getUserName())
                    .build();

            if (user != null) {
                user.updateProfile(profileUpdates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
            }

            updateUI(user);
        } else {
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(this, "Registration failed.",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    public void updateUI(FirebaseUser user){
        if(user != null){

        }
    }
}
