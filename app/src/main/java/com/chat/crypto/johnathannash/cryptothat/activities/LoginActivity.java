package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.models.LoginDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth auth;
    public LoginDataModel loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupFireBaseAuthentication();
        setupEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    private void setupEvents(){
        final LinearLayout view = findViewById(R.id.login_EmailPasswordLoginField);
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
            else if(view.getChildAt(child) instanceof Button){
                view.getChildAt(child).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonPressed(v);
                    }
                });
            }
            else{
                view.getChildAt(child).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonPressed(v);
                    }
                });
            }
        }
        findViewById(R.id.login_RegisterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(v);
            }
        });
    }

    private void setupFireBaseAuthentication(){
        auth = FirebaseAuth.getInstance();
        loginData = new LoginDataModel();
    }

    private void buttonPressed(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.LoginButton:
                loginAttempt();
                break;
            case R.id.login_RegisterButton:
                intent = new Intent(this, RegisterActivity.class);
                break;
            case R.id.login_ForgotPassword:
                intent = new Intent(this, ForgotPasswordActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    private void textChanged(View view, String text){
        switch(view.getId()){
            case R.id.login_EmailTextEntry:
                loginData.setEmail(text);
                break;
            case R.id.login_PasswordTextEntry:
                loginData.setPassWord(text);
                break;
        }
    }

    private void loginAttempt(){
        if(loginData.getEmail().isEmpty()){

        }
        else if(loginData.getPassWord().isEmpty()){

        }
        else{
            auth.signInWithEmailAndPassword(loginData.getEmail(), loginData.getPassWord())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            checkSuccessfulLogin(task);
                        }
                    });
        }
    }

    private void checkSuccessfulLogin(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = auth.getCurrentUser();
            updateUI(user);
        } else {
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(LoginActivity.this, "Email or Password is incorrect.",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }
}
