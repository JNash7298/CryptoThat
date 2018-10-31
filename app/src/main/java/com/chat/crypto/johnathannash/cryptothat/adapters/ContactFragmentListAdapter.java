package com.chat.crypto.johnathannash.cryptothat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.activities.HomeActivity;
import com.chat.crypto.johnathannash.cryptothat.fragments.ContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.fragments.FindContactsFragment;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;

import java.util.ArrayList;
import java.util.List;

public class ContactFragmentListAdapter extends RecyclerView.Adapter{

    private List<UserPublicData> contacts;
    private HomeActivity home;

    public ContactFragmentListAdapter(HomeActivity home, List<UserPublicData> contacts){
        this.home = home;
        this.contacts = contacts;
    }

    public ContactFragmentListAdapter(HomeActivity home){
        this.home = home;
        contacts = new ArrayList<>();
    }

    public void updateContact(UserPublicData publicData){
        int updated = -1;
        for(int position = 0; position < contacts.size() && updated == -1; position++){
            if(contacts.get(position).getUid().equals(publicData.getUid())){
                updated = position;
                contacts.get(position).setName(publicData.getName());
                contacts.get(position).setAvatar(publicData.getAvatar());
            }
        }

        notifyItemChanged(updated);
    }

    public void AddContact(UserPublicData publicData){
        contacts.add(publicData);
        notifyItemInserted(contacts.size()-1);
    }

    public void DisplayOnly(List<UserPublicData> publicData){
        contacts.clear();
        notifyDataSetChanged();

        contacts.addAll(publicData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contactView = inflater.inflate(R.layout.contact_data_row, parent, false);
        return new ContactViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ContactViewHolder viewHolder = (ContactViewHolder)holder;
        viewHolder.avatar.setBackground(
                new ColorDrawable(Color.parseColor(contacts.get(position).getAvatar())));

        viewHolder.name.setText(contacts.get(position).getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.selectedUserToMessage(contacts.get(position));
            }
        };
        viewHolder.row.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name;
        ConstraintLayout row;

        public ContactViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.contactDisplay_avatar);
            name = itemView.findViewById(R.id.contactDisplay_name);
            row = itemView.findViewById(R.id.contactDisplay_Row);
        }
    }
}
