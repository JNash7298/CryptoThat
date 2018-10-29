package com.chat.crypto.johnathannash.cryptothat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;

import java.util.ArrayList;
import java.util.List;

public class ContactFragmentListAdapter extends RecyclerView.Adapter{

    private List<UserPublicData> contacts;

    public ContactFragmentListAdapter(List<UserPublicData> contacts){
        this.contacts = contacts;
    }

    public ContactFragmentListAdapter(){
        contacts = new ArrayList<>();
    }

    public void AddContact(UserPublicData publicData){
        contacts.add(publicData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contactView = inflater.inflate(R.layout.contact_data_layout, parent, false);
        return new ContactViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder viewHolder = (ContactViewHolder)holder;
        viewHolder.avatar.setBackground(
                new ColorDrawable(Color.parseColor(contacts.get(position).getAvatar())));

        viewHolder.name.setText(contacts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name;

        public ContactViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.contactDisplay_avatar);
            name = itemView.findViewById(R.id.contactDisplay_name);
        }
    }
}
