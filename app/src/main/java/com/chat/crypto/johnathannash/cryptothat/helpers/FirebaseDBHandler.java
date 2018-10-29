package com.chat.crypto.johnathannash.cryptothat.helpers;

import android.arch.core.util.Function;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chat.crypto.johnathannash.cryptothat.activities.ProfileSetupActivity;
import com.chat.crypto.johnathannash.cryptothat.fragments.ContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.models.UserPrivateData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.chat.crypto.johnathannash.cryptothat.models.UserRequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirebaseDBHandler {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "firebaseDBHandler";

    public FirebaseDBHandler(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void setUserPublicData(UserPublicData publicData){
        reference.child("users").child("public_data").child(user.getUid()).setValue(publicData);
    }

    public void setUserPrivateData(UserPrivateData privateData, String room_id){
        reference.child("users").child("private_data").child(user.getUid()).child("contacts").child(room_id).setValue(privateData);
    }

    public void setUserRequestData(UserRequestData requestData, String uid, String room_id){
        reference.child("users").child("request_data").child(uid).child("message_request").child(room_id).setValue(requestData);
    }

    public void updatePublicUserData(UserPublicData publicData){
        Map<String, Object> publicDataValues = publicData.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("users/public_data/" + user.getUid(), publicDataValues);

        reference.updateChildren(update);
    }

    public void updatePrivateUserData(UserPrivateData privateData, String room_id){
        Map<String, Object> privateDataValues = privateData.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("users/private_data/" + user.getUid() + "/contacts/" + room_id, privateDataValues);

        reference.updateChildren(update);
    }

    public void updateRequestUserData(UserRequestData requestData, String uid, String room_id){
        Map<String, Object> requestDataValues = requestData.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("users/request_data/" + uid + "/message_request/" + room_id, requestDataValues);

        reference.updateChildren(update);
    }

    public void retrievePublicUserData(final ProfileSetupActivity requester){
        reference.child("users").child("public_data").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requester.ProvideUserData(dataSnapshot.getValue(UserPublicData.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage() + "-retrievePublicUserData");
            }
        });
    }

    public void retrieveAllUsers(final ContactsFragment requester){
        reference.child("users").child("public_data")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> userData = dataSnapshot.getChildren();
                List<UserPublicData> publicData = new ArrayList<>();

                for(DataSnapshot data : userData){
                    UserPublicData retrievedData = data.getValue(UserPublicData.class);
                    publicData.add(retrievedData);
                }

                if(publicData.size() == 1){
                    requester.newUserAvaliable(publicData.get(0));
                }
                else{
                    requester.newUserAvaliable(publicData);
                }

                retrieveAllUserContacts(requester);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage() + "-retrieveAllUsers");
            }
        });
    }

    public void retrieveAllUserContacts(final ContactsFragment requester){
        reference.child("users").child("private_data").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserPrivateData privateData = dataSnapshot.getValue(UserPrivateData.class);
                requester.userPrivateDate(privateData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage() + "-retrieveAllUserContacts");
            }
        });
    }
}
