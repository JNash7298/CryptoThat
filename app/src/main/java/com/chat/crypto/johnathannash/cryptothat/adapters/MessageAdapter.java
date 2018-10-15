package com.chat.crypto.johnathannash.cryptothat.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<MessageModel> messageModels = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(MessageModel messageModel) {
        this.messageModels.add(messageModel);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder = new MessageViewHolder();
        MessageModel messageModel = messageModels.get(position);

        if(convertView == null){
            LayoutInflater messageInflater =
                    (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(messageModel.isBelongsToCurrentUser()){
                convertView = messageInflater
                        .inflate(R.layout.my_message_layout, parent, false);

                holder.messageBody = convertView.findViewById(R.id.message_MyMessageText);
            }else{
                convertView = messageInflater
                        .inflate(R.layout.other_message_layout, parent, false);
                holder.avatar = convertView.findViewById(R.id.message_Avatar);
                holder.profileName = convertView.findViewById(R.id.message_ProfileName);
                holder.messageBody = convertView.findViewById(R.id.message_OtherMessageText);
            }
            convertView.setTag(holder);
        }else{
            holder = (MessageViewHolder)convertView.getTag();
        }

        if(messageModel.isBelongsToCurrentUser()) {
            holder.messageBody.setText(messageModel.getText());
        }else{
            holder.profileName.setText(messageModel.getData().getName());
            holder.messageBody.setText(messageModel.getText());

            GradientDrawable drawable = (GradientDrawable)holder.avatar.getBackground();
            drawable.setColor(Color.parseColor(messageModel.getData().getColor()));
            holder.avatar.setBackground(drawable);
        }

        return convertView;
    }

    class MessageViewHolder {
        View avatar;
        TextView profileName;
        TextView messageBody;
    }
}
