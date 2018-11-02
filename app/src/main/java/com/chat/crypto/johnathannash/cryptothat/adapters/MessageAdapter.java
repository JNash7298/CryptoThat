package com.chat.crypto.johnathannash.cryptothat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.activities.MessageActivity;
import com.chat.crypto.johnathannash.cryptothat.models.MessageData;
import com.chat.crypto.johnathannash.cryptothat.models.UserPublicData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private List<MessageData> messages = new ArrayList<>();
    private MessageActivity messageActivity;
    private UserPublicData user, contact;

    public MessageAdapter(MessageActivity messageActivity,
                          UserPublicData userData,
                          UserPublicData contactData) {
        this.user = userData;
        this.contact = contactData;
        this.messageActivity = messageActivity;
        messages = new ArrayList<>();
    }

    public MessageAdapter(MessageActivity messageActivity,
                          List<MessageData> messages,
                          UserPublicData userData,
                          UserPublicData contactData){
        this.user = userData;
        this.contact = contactData;
        this.messageActivity = messageActivity;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position){
        int type = -1;

        if(messages.get(position).getSender() == user.getUid()){
            type = 0;
        }
        else {
            type = 1;
        }

        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType){
            case 0:
                view = inflater.inflate(R.layout.my_message_row, parent, false);
                return new MyMessageViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.other_message_row, parent, false);
                return new OtherMessageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyMessageViewHolder){
            MyMessageViewHolder viewHolder = (MyMessageViewHolder)holder;
            viewHolder.message.setText(messages.get(position).getCipher_text());
            viewHolder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageActivity.messageSelected(messages.get(position));
                }
            });
        }else if (holder instanceof OtherMessageViewHolder){
            OtherMessageViewHolder viewHolder = (OtherMessageViewHolder)holder;
            viewHolder.avatar.setBackground(new ColorDrawable(Color.parseColor(contact.getAvatar())));
            viewHolder.name.setText(contact.getName());
            viewHolder.message.setText(messages.get(position).getCipher_text());
            viewHolder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageActivity.messageSelected(messages.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class OtherMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name, message;
        ConstraintLayout row;

        public OtherMessageViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.message_Avatar);
            name = itemView.findViewById(R.id.message_ProfileName);
            message = itemView.findViewById(R.id.message_MessageText);
            row = itemView.findViewById(R.id.message_Row);
        }
    }

    class MyMessageViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        ConstraintLayout row;

        public MyMessageViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.message_MessageText);
            row = itemView.findViewById(R.id.message_Row);
        }
    }
}
