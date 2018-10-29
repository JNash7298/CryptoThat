package com.chat.crypto.johnathannash.cryptothat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.adapters.ContactFragmentListAdapter;
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.UserPrivateData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends Fragment {

    private View view;
    private RecyclerView list;
    private FirebaseDBHandler dbHandler;
    private Map<String, UserPublicData> allUsers;
    private UserPrivateData privateData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        allUsers = new HashMap<>();

        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        dbHandler = new FirebaseDBHandler();

        list = view.findViewById(R.id.contactFragment_ContactList);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHandler.retrieveAllUsers(this);

        return view;
    }

    public void newUserAvaliable(UserPublicData publicData){
        if(allUsers.containsKey(publicData.getUid())){
            updateUserData(publicData);
        }
        else{
            allUsers.put(publicData.getUid(), publicData);
        }
    }

    public void newUserAvaliable(List<UserPublicData> publicData){
        for(UserPublicData data : publicData){
            allUsers.put(data.getUid(), data);
        }
    }

    public void userPrivateDate(UserPrivateData privateData){
        this.privateData = privateData;

        List<UserPublicData> listData = new ArrayList<>();
        for(String id : privateData.getContacts().values()){
            listData.add(allUsers.get(id));
        }
        list.setAdapter(new ContactFragmentListAdapter(listData));
    }

    private void updateUserData(UserPublicData publicData){
        allUsers.get(publicData.getUid()).setName(publicData.getName());
        allUsers.get(publicData.getUid()).setAvatar(publicData.getAvatar());
    }
}
