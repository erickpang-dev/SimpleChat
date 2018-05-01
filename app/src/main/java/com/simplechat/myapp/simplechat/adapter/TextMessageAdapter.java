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
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.model.TextMessage;

import java.util.ArrayList;

public class TextMessageAdapter extends ArrayAdapter<TextMessage> {

    private Context context;
    private ArrayList<TextMessage> textMessageArrayList;

    public TextMessageAdapter(@NonNull Context c, @NonNull ArrayList<TextMessage> objects) {
        super(c, 0, objects);
        this.context = c;
        this.textMessageArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (textMessageArrayList != null){

            Preferences preferences = new Preferences(context);
            String senderUserId = preferences.getCurrentUserIdentifier();

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            TextMessage textMessage = textMessageArrayList.get( position );

            if (senderUserId.equals(textMessage.getUserId())){
                view = layoutInflater.inflate(R.layout.sent_messages, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.received_messages, parent, false);
            }

            TextView textViewMessage = view.findViewById(R.id.textViewMessageId);
            textViewMessage.setText( textMessage.getMessage() );


        } else {

        }

        return view;
    }
}
