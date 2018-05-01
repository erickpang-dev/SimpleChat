package com.simplechat.myapp.simplechat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {

    private ArrayList<Chat> chatArray;
    private Context context;

    public ChatAdapter(@NonNull Context c, @NonNull ArrayList<Chat> objects) {
        super(c, 0, objects);
        this.chatArray = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (chatArray != null){


            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


            view = layoutInflater.inflate(R.layout.chat_list, parent, false);

            TextView contactName = view.findViewById(R.id.contactNameId);
            TextView latestMessage = view.findViewById(R.id.latestMessageId);

            Chat chat = chatArray.get(position);
            contactName.setText(chat.getUserName());
            latestMessage.setText(chat.getMessage());
        }

        return view;
    }
}
