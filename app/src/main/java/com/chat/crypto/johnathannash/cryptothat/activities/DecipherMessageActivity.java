package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.adapters.CipherSpinnerAdapter;
import com.chat.crypto.johnathannash.cryptothat.helpers.CipherProgram;
import com.chat.crypto.johnathannash.cryptothat.models.CipherSpinnerData;
import com.chat.crypto.johnathannash.cryptothat.models.MessageData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.firebase.auth.FirebaseAuth;

public class DecipherMessageActivity extends AppCompatActivity {

    private CipherProgram ciphers;
    private Spinner cipherSelector;
    private TextView topView, keyView, bottomView;
    private CipherSpinnerAdapter spinnerAdapter;
    private String room;
    private UserPublicData user, contact;
    private MessageData messageData;
    private boolean appStopping = true, lockInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decipher_message);

        user = getIntent().getParcelableExtra("user");
        contact = getIntent().getParcelableExtra("contact");
        room = getIntent().getStringExtra("room");
        messageData = (MessageData) getIntent().getSerializableExtra("message_data");

        ciphers = new CipherProgram();
        topView = findViewById(R.id.decipherMessage_TopMessageField);
        keyView = findViewById(R.id.decipher_KeyInput);
        bottomView = findViewById(R.id.decipherMessage_BottomMessageField);

        cipherSelector = findViewById(R.id.decipher_CipherNameSelector);
        spinnerAdapter = new CipherSpinnerAdapter(this, R.layout.cipher_spinner_layout, ciphers.getCipherNames());
        spinnerAdapter.setDropDownViewResource(R.layout.cipher_spinner_layout);
        cipherSelector.setAdapter(spinnerAdapter);

        setup();
    }

    private void setup(){
        final TextInputLayout keyWrapper = findViewById(R.id.decipherMessage_InputTextLayout);
        final TextInputLayout topTextWrapper = findViewById(R.id.decipherMessage_PlainTextInputLayout);

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

        for (CipherSpinnerData cipher : ciphers.getCipherNames()) {
            if(cipher.getCipherId().equals(messageData.getCipher_id())){
                int position = spinnerAdapter.getPosition(cipher) + 1;
                cipherSelector.setSelection(position, true);
            }
        }

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

        keyView.setText(messageData.getKey());

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
                    bottomView.setText(ciphers.decipherText(topView.getText().toString(), keyView.getText().toString()));
                }
            }
        });

        topView.setText(messageData.getCipher_text());
        bottomView.setText(messageData.getPlain_text());

        findViewById(R.id.decipherMessage_backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(v);
            }
        });
    }

    private void onClickEvent(View view){
        if(!lockInput){
            lockInput = true;
            Intent intent = null;
            switch (view.getId()){
                case R.id.decipherMessage_backButton:
                    intent = new Intent(this, MessageActivity.class);
                    intent.putExtra("user_data", user);
                    intent.putExtra("contact_data", contact);
                    intent.putExtra("room", room);
                    appStopping = false;
                    break;
            }
            if(intent != null){
                startActivity(intent);
            }
        }
    }

    private void validKeyResponses(String text, TextInputLayout keyWrapper, TextInputLayout topTextWrapper){

        if(ciphers.checkIfKeyIsValid(text)){
            keyWrapper.setErrorEnabled(false);
            topTextWrapper.setErrorEnabled(false);
            topView.setEnabled(true);
            if(!topView.getText().toString().isEmpty()){
                bottomView.setText(ciphers.decipherText(topView.getText().toString(), keyView.getText().toString()));
            }
        }
        else{
            keyWrapper.setError(getString(R.string.decipherMessage_KeyError));
            keyWrapper.setErrorEnabled(true);
            topView.setEnabled(false);
            topTextWrapper.setErrorEnabled(true);
            if(text.length() == 0){
                topTextWrapper.setError(getString(R.string.decipherMessage_TopTextWrapperEmptyKey));
            } else{
                topTextWrapper.setError(getString(R.string.decipherMessage_TopTextWrapperInvalidKey));
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
