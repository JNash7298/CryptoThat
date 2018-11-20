package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.RegisterDataModel;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

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
                final int finalChild = child;
                ((EditText) view.getChildAt(child)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        textChanged(view.getChildAt(finalChild), s.toString());
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
        Boolean emptyFields;
        StringBuilder missingFields = new StringBuilder();
        ArrayList<String> missing = new ArrayList<>();

        if((emptyFields = (registerData.getEmail() == null
                || registerData.getEmail().isEmpty()))){
            missing.add("Email");
        }
        if(emptyFields
                || (emptyFields = (registerData.getUserName() == null
                || registerData.getUserName().isEmpty()))){
            missing.add("Username");
        }
        if(emptyFields
                || (emptyFields = (registerData.getPassWord() == null
                || registerData.getPassWord().isEmpty()))){
            missing.add("Password");
        }
        if(emptyFields
                || (emptyFields = (registerData.getConfirmationPassWord() == null
                || registerData.getConfirmationPassWord().isEmpty()))){
            missing.add("Password Confirmation");
        }

        if(!emptyFields){
            if(registerData.getUserName().equalsIgnoreCase("admin")){
                Toast.makeText(this,
                        "The user name " + registerData.getUserName() + " is not a usable username."
                        , Toast.LENGTH_SHORT).show();
            } else if(!registerData.getPassWord().equals(registerData.getConfirmationPassWord())) {
                Toast.makeText(this,
                        "Password and confirmation password are not the same."
                        , Toast.LENGTH_SHORT).show();
            } else if(registerData.getPassWord().length() < 6){
                Toast.makeText(this,
                        "Password is too simple, you need a password of a minimum a 6 characters in length."
                        , Toast.LENGTH_SHORT).show();
            } else{
                auth.createUserWithEmailAndPassword(registerData.getEmail(), registerData.getPassWord())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                checkIfSuccessfulRegister(task);
                            }
                        });
            }
        } else{
            missingFields.append("You are missing");
            for(int field = 0; field < missing.size(); field++){
                if(missing.size() > 1 && field == missing.size() - 1){
                       missingFields.append(", and ").append(missing.get(field));
                } else{
                    missingFields.append(", ").append(missing.get(field));
                }
            }
            missingFields.append(".");
            Toast.makeText(this, missingFields.toString(), Toast.LENGTH_LONG).show();;
        }
    }

    private void checkIfSuccessfulRegister(Task<AuthResult> task){
        if (task.isSuccessful()) {
            Log.d(TAG, "createUserWithEmail:success");
            final FirebaseUser user = auth.getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(registerData.getUserName())
                    .build();

            if (user != null) {
                user.updateProfile(profileUpdates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            updateUI(user);
                        }
                    }
                });
            }
        } else {
            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(this, registerData.getEmail() + " is an unusable email.",
                        Toast.LENGTH_SHORT).show();
            }else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(this, registerData.getEmail() + " is not in valid email format.",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(this, "Registration failed.",
                        Toast.LENGTH_SHORT).show();
            }
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
