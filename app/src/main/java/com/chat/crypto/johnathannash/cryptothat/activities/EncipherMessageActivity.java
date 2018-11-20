package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.adapters.CipherSpinnerAdapter;
import com.chat.crypto.johnathannash.cryptothat.helpers.CipherProgram;
import com.chat.crypto.johnathannash.cryptothat.models.CipherSpinnerData;
import com.chat.crypto.johnathannash.cryptothat.models.MessageData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.firebase.auth.FirebaseAuth;

public class EncipherMessageActivity extends AppCompatActivity {

    private CipherProgram ciphers;
    private Spinner cipherSelector;
    private TextView topView, keyView, bottomView;
    private CipherSpinnerAdapter spinnerAdapter;
    private String room;
    private UserPublicData user, contact;
    private boolean appStopping = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encipher_message);

        user = getIntent().getParcelableExtra("user");
        contact = getIntent().getParcelableExtra("contact");
        room = getIntent().getStringExtra("room");

        ciphers = new CipherProgram();
        topView = findViewById(R.id.encipherMessage_TopMessageField);
        keyView = findViewById(R.id.encipher_KeyInput);
        bottomView = findViewById(R.id.encipherMessage_BottomMessageField);

        cipherSelector = findViewById(R.id.encipher_CipherNameSelector);
        spinnerAdapter = new CipherSpinnerAdapter(this, R.layout.cipher_spinner_layout, ciphers.getCipherNames());
        spinnerAdapter.setDropDownViewResource(R.layout.cipher_spinner_layout);
        cipherSelector.setAdapter(spinnerAdapter);

        setup();
    }

    private void setup(){
        final TextInputLayout keyWrapper = findViewById(R.id.encipherMessage_InputTextLayout);
        final TextInputLayout topTextWrapper = findViewById(R.id.encipherMessage_PlainTextInputLayout);

        cipherSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    keyView.setEnabled(false);
                    topView.setEnabled(false);
                    return;
                }
                else{
                    CipherSpinnerData temp = (CipherSpinnerData) parent.getItemAtPosition(position - 1);
                    ciphers.setCurrentCipher(temp.getCipherId());
                    keyView.setEnabled(true);
                    validKeyResponses(keyView.getText().toString(), keyWrapper, topTextWrapper);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        keyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validKeyResponses(s.toString(), keyWrapper, topTextWrapper);
            }
        });

        topView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!keyWrapper.isErrorEnabled()){
                    bottomView.setText(ciphers.encipherText(topView.getText().toString(), keyView.getText().toString()));
                }
            }
        });

        findViewById(R.id.encipherMessage_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(v);
            }
        });
        findViewById(R.id.encipherMessage_sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(v);
            }
        });
    }

    private void onClickEvent(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.encipherMessage_backButton:
                intent = new Intent(this, MessageActivity.class);
                intent.putExtra("user_data", user);
                intent.putExtra("contact_data", contact);
                intent.putExtra("room", room);
                appStopping = false;
                break;
            case R.id.encipherMessage_sendButton:
                MessageData tempMessage = new MessageData();
                tempMessage.setSender(user.getUid());
                tempMessage.setCipher_id(ciphers.getCipherId());
                tempMessage.setCipher_text(bottomView.getText().toString());
                tempMessage.setPlain_text(topView.getText().toString());
                tempMessage.setKey(keyView.getText().toString());

                intent = new Intent(this, MessageActivity.class);
                intent.putExtra("user_data", user);
                intent.putExtra("contact_data", contact);
                intent.putExtra("room", room);
                intent.putExtra("sendMessageData", tempMessage);
                appStopping = false;
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    private void validKeyResponses(String text, TextInputLayout keyWrapper, TextInputLayout topTextWrapper){

        if(ciphers.checkIfKeyIsValid(text)){
            keyWrapper.setErrorEnabled(false);
            topTextWrapper.setErrorEnabled(false);
            topView.setEnabled(true);
            if(!topView.getText().toString().isEmpty()){
                bottomView.setText(ciphers.encipherText(topView.getText().toString(), keyView.getText().toString()));
            }
        }
        else{
            keyWrapper.setError(getString(R.string.encipherKey_Error));
            keyWrapper.setErrorEnabled(true);
            topView.setEnabled(false);
            topTextWrapper.setErrorEnabled(true);
            if(text.length() == 0){
                topTextWrapper.setError(getString(R.string.encipherMessage_TopTextWrapperEmptyKey));
            } else{
                topTextWrapper.setError(getString(R.string.encipherMessage_TopTextWrapperInvalidKey));
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(appStopping){
            FirebaseAuth.getInstance().signOut();
        }
    }
}
