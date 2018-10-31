package com.chat.crypto.johnathannash.cryptothat.helpers;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chat.crypto.johnathannash.cryptothat.activities.HomeActivity;
import com.chat.crypto.johnathannash.cryptothat.activities.ProfileSetupActivity;
import com.chat.crypto.johnathannash.cryptothat.fragments.ContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.fragments.FindContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.models.RoomMemberData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPrivateData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.chat.crypto.johnathannash.cryptothat.models.UserRequestData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDBHandler {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "firebaseDBHandler";
    private ValueEventListener listener;

    public FirebaseDBHandler(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void setUserPublicData(UserPublicData publicData){
        reference.child("users").child("public_data").child(user.getUid()).setValue(publicData);
    }

    public void pushUserPrivateData(String room_id, String contact){
        Map<String, String> newContact = new HashMap<>();
        newContact.put(contact, room_id);
        reference.child("users").child("private_data").child(user.getUid()).child("contacts").push().setValue(newContact);
    }

    public void pushUserRequestData(String uid, String room_id){
        Map<String, String> request = new HashMap<>();
        request.put(user.getUid(), room_id);
        reference.child("users").child("request_data").child(uid).child("message_request").push().setValue(request);
    }

    public void updatePublicUserData(UserPublicData publicData){
        Map<String, Object> publicDataValues = publicData.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("users/public_data/" + user.getUid(), publicDataValues);

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

    public void retrieveRequestUserData(){

    }

    public ValueEventListener gatherAllContactData(Activity main){
        ValueEventListener eventListener = contactListener(main);
        reference.child("users").child("public_data").addValueEventListener(eventListener);
        return eventListener;
    }

    private ValueEventListener contactListener(final Activity main) {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> userData = dataSnapshot.getChildren();
                List<UserPublicData> publicData = new ArrayList<>();

                for (DataSnapshot data : userData) {
                    UserPublicData retrieveData = data.getValue(UserPublicData.class);
                    publicData.add(retrieveData);
                }

                if (main instanceof HomeActivity) {
                    HomeActivity temp = (HomeActivity) main;
                    temp.fillUsers(publicData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage() + "-contactListener");
            }
        };

        return listener;
    }

    public void retrieveUserPrivateData(final Activity main){
        reference.child("users").child("private_data").child(user.getUid()).child("contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserPrivateData privateData = new UserPrivateData();
                privateData.setContacts(new HashMap<String, String>());
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Map<String, String> temp = (Map<String, String>)data.getValue();
                    privateData.getContacts().putAll(temp);
                }


                if(main instanceof HomeActivity){
                    ((HomeActivity) main).setUserPrivateData(privateData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage() + "-contactListener");
            }
        });
    }

    public void createNewRoom(RoomMemberData roomData){
        reference.child("room_members").child(roomData.getRoomName()).setValue(roomData.getRoom())
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "-createNewRoom-" + e.getMessage());
            }
        });
    }

    public void publicDataRemoveListeners(ValueEventListener listener){
        reference.child("users").child("public_data").removeEventListener(listener);
    }
}
