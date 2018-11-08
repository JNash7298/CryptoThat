package com.chat.crypto.johnathannash.cryptothat.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.adapters.ContactFragmentListAdapter;
import com.chat.crypto.johnathannash.cryptothat.fragments.ContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.fragments.FindContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.RoomMemberData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPrivateData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.chat.crypto.johnathannash.cryptothat.models.UserRequestData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private FragmentTransaction transaction;
    private Fragment currentListFragment = null;
    private ValueEventListener publicDataListener, requestDataListener;
    private Map<String, UserPublicData> allUsers;
    private UserPrivateData privateData;
    private ContactFragmentListAdapter listAdapter;
    private FirebaseDBHandler dbHandler;
    private UserRequestData requestData;
    private boolean Initalizing = true;
    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbHandler = new FirebaseDBHandler();

        setupEvents();
        setup();
    }

    private void setupEvents(){
        LinearLayout navBar = findViewById(R.id.home_NavagationField);
        for(int item = 0; item < navBar.getChildCount(); item++){
            navBar.getChildAt(item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPressEvent(v);
                }
            });
        }

        ((TextView)findViewById(R.id.home_ProfileName)).setText(user.getDisplayName());
    }

    private void setup(){
        allUsers = new HashMap<>();
        requestDataListener = dbHandler.gatherRequestData(this);
    }

    public void updateRequestData(UserRequestData requestData){
        if(this.requestData == null){
            this.requestData = requestData;
        }
        else{
            for(String id : requestData.getMessage_request().keySet()){
                if(!privateData.getContacts().containsKey(id)){
                    this.requestData.getMessage_request().put(id, requestData.getMessage_request().get(id));
                }
            }
        }

        while(requestData.getMessage_request().size() != 0){
            String id = (String)requestData.getMessage_request().keySet().toArray()[0];
            dbHandler.pushUserPrivateData(requestData.getMessage_request().get(id), id);
            dbHandler.removeRequestData(id);
            requestData.getMessage_request().remove(id);
        }
    }

    public void setContentData(){
        if(Initalizing){
            publicDataListener = dbHandler.gatherAllContactData(this);
            Initalizing = false;
        }
    }

    public void fillUsers(List<UserPublicData> publicData){
        for(UserPublicData data : publicData){
            if (allUsers.containsKey(data.getUid())){
                if(!allUsers.get(data.getUid()).equals(data)){
                    updateUser(data);
                }
            }
            else{
                allUsers.put(data.getUid(), data);
                if(listAdapter != null){
                    listAdapter.AddContact(data);
                }
            }
        }
        dbHandler.retrieveUserPrivateData(this);
    }

    private void updateUser(UserPublicData publicData){
        if(listAdapter != null){
            listAdapter.updateContact(publicData);
        }
    }

    public void setUserPrivateData(UserPrivateData privateData){
        this.privateData = privateData;
        switchContactList(allUsers, privateData);
    }

    public void switchContactList(Map<String, UserPublicData> users, UserPrivateData privateData){
        transaction = getSupportFragmentManager().beginTransaction();

        if(currentListFragment == null){
            currentListFragment = new ContactsFragment();
        }
        else{
            transaction.remove(currentListFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            transaction = getSupportFragmentManager().beginTransaction();

            if(currentListFragment instanceof ContactsFragment){
                currentListFragment = new FindContactsFragment();
            }else if(currentListFragment instanceof FindContactsFragment){
                currentListFragment = new ContactsFragment();
            }
        }

        Bundle infoBundle = new Bundle();

        ArrayList<String> keys = new ArrayList<>(users.keySet());
        infoBundle.putStringArrayList("usersKey", keys);

        ArrayList<UserPublicData> values = new ArrayList<>(users.values());
        infoBundle.putParcelableArrayList("usersValues", values);

        infoBundle.putParcelable("privateData", privateData);

        currentListFragment.setArguments(infoBundle);

        if(listAdapter == null){
            if(currentListFragment instanceof ContactsFragment){
                listAdapter = new ContactFragmentListAdapter(this, contactsList());
            }
            else if(currentListFragment instanceof FindContactsFragment){
                listAdapter = new ContactFragmentListAdapter(this, usersList());
            }
        }

        transaction.add(R.id.home_ContactsFragmentFrame, currentListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public List<UserPublicData> contactsList(){
        List<UserPublicData> contacts = new ArrayList<>();
        for (String id : privateData.getContacts().keySet()) {
            contacts.add(allUsers.get(id));
        }

        return contacts;
    }

    public List<UserPublicData> usersList(){
        List<UserPublicData> users = new ArrayList<>(allUsers.values());

        users.remove(allUsers.get(user.getUid()));

        return users;
    }

    public RecyclerView.Adapter getListAdapter(){
        return listAdapter;
    }

    public void selectedUserToMessage(UserPublicData publicData){
        String room;
        if(currentListFragment instanceof ContactsFragment){
            room = privateData.getContacts().get(publicData.getUid());
            startMessage(allUsers.get(user.getUid()), publicData, room);
        }else if (currentListFragment instanceof FindContactsFragment){
            if(privateData.getContacts().containsKey(publicData.getUid())){
                room = privateData.getContacts().get(publicData.getUid());
                startMessage(allUsers.get(user.getUid()), publicData, room);
            }
            else{
                RoomMemberData newRoom = new RoomMemberData();

                Map<String, Boolean> roomMember = new HashMap<>();
                roomMember.put(user.getUid(), true);
                roomMember.put(publicData.getUid(), true);

                newRoom.setRoom(roomMember);
                newRoom.setRoomName(user.getUid() + ":" + publicData.getUid());

                dbHandler.createNewRoom(newRoom);

                UserRequestData request = new UserRequestData();

                Map<String, String> message_request = new HashMap<>();
                message_request.put(user.getUid(), newRoom.getRoomName());
                request.setMessage_request(message_request);

                dbHandler.pushUserRequestData(publicData.getUid(), newRoom.getRoomName());

                privateData.getContacts().put(publicData.getUid(), newRoom.getRoomName());
                dbHandler.pushUserPrivateData(newRoom.getRoomName(), publicData.getUid());
            }
        }
    }

    public void startMessage(UserPublicData user, UserPublicData contact, String room){
        Intent intent = new Intent(this, MessageActivity.class);

        intent.putExtra("user_data", user);
        intent.putExtra("contact_data", contact);
        intent.putExtra("room", room);

        startActivity(intent);
    }

    private void buttonPressEvent(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.home_CipherButton:
                break;
            case R.id.home_SettingButtton:
                break;
            case R.id.home_LogoutButton:
                auth.signOut();
                intent = new Intent(this, LoginActivity.class);
                break;
        }

        if(intent != null){
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dbHandler.publicDataRemoveListeners(publicDataListener);
        dbHandler.requestDataRemoveListeners(requestDataListener);
    }
}
