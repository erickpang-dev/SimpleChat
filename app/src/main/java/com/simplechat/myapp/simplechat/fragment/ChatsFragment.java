package com.simplechat.myapp.simplechat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.activity.ChatActivity;
import com.simplechat.myapp.simplechat.adapter.ChatAdapter;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.Base64Converter;
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.model.Chat;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private ListView chatsListView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Chat> chatArray;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerChats;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerChats);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerChats);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatArray = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatsListView = view.findViewById(R.id.chatListViewId);

        arrayAdapter = new ChatAdapter(getActivity(), chatArray);

        chatsListView.setAdapter(arrayAdapter);

        Preferences preferences = new Preferences(getActivity());
        String currentUserIdentifier = preferences.getCurrentUserIdentifier();

        databaseReference = FirebaseConfiguration.getFirebase();
        databaseReference = databaseReference.child("chats").child(currentUserIdentifier);

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatArray.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Chat chat = data.getValue( Chat.class );
                    chatArray.add(chat);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Chat chat = chatArray.get(position);

                Intent ChatToChatActivity = new Intent(getActivity(), ChatActivity.class);
                ChatToChatActivity.putExtra("contactName", chat.getUserName());
                String contactEmail = Base64Converter.base64Decode(chat.getUserId());
                ChatToChatActivity.putExtra("contactEmail", contactEmail);
                startActivity(ChatToChatActivity);

            }
        });

        return view;
    }

}
