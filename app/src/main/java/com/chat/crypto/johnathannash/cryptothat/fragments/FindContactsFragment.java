package com.chat.crypto.johnathannash.cryptothat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.activities.HomeActivity;
import com.chat.crypto.johnathannash.cryptothat.adapters.ContactFragmentListAdapter;
import com.chat.crypto.johnathannash.cryptothat.helpers.FirebaseDBHandler;
import com.chat.crypto.johnathannash.cryptothat.models.UserPrivateData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindContactsFragment extends Fragment {

    private FirebaseDBHandler dbHandler;
    private RecyclerView list;
    private TextView searchBar;
    private View view;
    private HomeActivity home;
    private Map<String, UserPublicData> allUsers;
    private UserPrivateData privateData;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private ContactFragmentListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_contacts, container, false);

        if(getActivity() instanceof HomeActivity){
            home = (HomeActivity)getActivity();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        allUsers = new HashMap<>();
        listAdapter = (ContactFragmentListAdapter) home.getListAdapter();

        List<String> keys = getArguments().getStringArrayList("usersKey");
        List<UserPublicData> users = getArguments().getParcelableArrayList("usersValues");

        for(int position = 0; position < keys.size(); position++){
            allUsers.put(keys.get(position), users.get(position));
        }

        privateData = getArguments().getParcelable("privateData");

        list = view.findViewById(R.id.contactFragment_UserList);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(listAdapter);
        listAdapter.DisplayOnly(home.usersList());

        setupEvents();

        return view;
    }

    private void setupEvents(){
        searchBar = view.findViewById(R.id.contactsFragment_SearchBar);

        view.findViewById(R.id.contactsFragment_ContactSwitchButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEvent(v);
                    }
                });

        view.findViewById(R.id.contactFragment_SearchButton)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEvent(v);
                    }
                });
    }

    private void onClickEvent(View view){
        switch (view.getId()){
            case R.id.contactsFragment_ContactSwitchButton:
                home.switchContactList(allUsers, privateData);
                break;
            case R.id.contactFragment_SearchButton:
                List<UserPublicData> listData = new ArrayList<>();

                if(searchBar.getText() == null || searchBar.getText().toString().isEmpty()){
                    listData.addAll(allUsers.values());
                    listData.remove(allUsers.get(user.getUid()));
                }
                else{
                    for(String id : allUsers.keySet()){
                        if(!id.equals(user.getUid())){
                            UserPublicData tempData = allUsers.get(id);
                            if(tempData.getName().toLowerCase().contains(searchBar.getText().toString())){
                                listData.add(tempData);
                            }
                        }
                    }
                }

                listAdapter.DisplayOnly(listData);
                break;
        }
    }
}
