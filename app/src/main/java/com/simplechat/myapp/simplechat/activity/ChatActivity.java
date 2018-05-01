package com.simplechat.myapp.simplechat.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.adapter.TextMessageAdapter;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.Base64Converter;
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.model.Chat;
import com.simplechat.myapp.simplechat.model.TextMessage;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText messageTextView;
    private ImageButton sendButton;
    private DatabaseReference databaseReference;
    private ListView chatMessageList;
    private ArrayList<TextMessage> messageArray;
    private ArrayAdapter<TextMessage> arrayAdapter;
    private ValueEventListener valueEventListenerMessage;

// recipient info
    private String recipientUserName;
    private String recipientUserId;

// sender info
    private String senderUserName;
    private String senderUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbarId);
        messageTextView = findViewById(R.id.messageTextViewId);
        sendButton = findViewById(R.id.sendButtonId);
        chatMessageList = findViewById(R.id.chatMessageListId);

// get logged in user data
        Preferences preferences = new Preferences(this);
        senderUserId = preferences.getCurrentUserIdentifier();
        senderUserName = preferences.getCurrentUserName();

        Bundle contactInformation = getIntent().getExtras();

        if (contactInformation != null){
            recipientUserName = contactInformation.getString("contactName");
            String recipientUserEmail = contactInformation.getString("contactEmail");
            recipientUserId = Base64Converter.base64Encode(recipientUserEmail);
        }
// set up toolbar
        toolbar.setTitle(recipientUserName);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        setSupportActionBar(toolbar);

// set up listview & adapter
        messageArray = new ArrayList<>();
        arrayAdapter = new TextMessageAdapter(ChatActivity.this, messageArray);
        chatMessageList.setAdapter(arrayAdapter);

// get messages on firebase
        databaseReference = FirebaseConfiguration.getFirebase()
                            .child("messages")
                            .child(senderUserId)
                            .child(recipientUserId);

// set up listener for message
        valueEventListenerMessage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageArray.clear();

                for (DataSnapshot data: dataSnapshot.getChildren()){
                    TextMessage textMessage = data.getValue( TextMessage.class);
                    messageArray.add( textMessage );
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener( valueEventListenerMessage);

// set up send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typedText = messageTextView.getText().toString();

                if (typedText.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Sending empty text is not allowed.", Toast.LENGTH_SHORT).show();
                } else {
                    TextMessage textMessage = new TextMessage();
                    textMessage.setUserId( senderUserId );
                    textMessage.setMessage( typedText );

// save message for both users
                    Boolean currentUserMessageTest = saveMessage(senderUserId, recipientUserId, textMessage);
                    if (!currentUserMessageTest) {
                        Toast.makeText(ChatActivity.this, "Message could not be saved.", Toast.LENGTH_LONG).show();
                    } else {
                        Boolean contactUserMessageTest = saveMessage(recipientUserId, senderUserId, textMessage);
                        if (!contactUserMessageTest){
                            Toast.makeText(ChatActivity.this, "Message could not be sent.", Toast.LENGTH_LONG).show();
                        }
                    }
// save chat for both users
                    Chat chatSender = new Chat();
                    chatSender.setUserId(recipientUserId);
                    chatSender.setUserName(recipientUserName);
                    chatSender.setMessage(typedText);
                    Boolean currentUserChatTest = saveChat(senderUserId, recipientUserId, chatSender);
                    if (!currentUserChatTest){
                        Toast.makeText(ChatActivity.this, "Chat could not be saved.", Toast.LENGTH_LONG).show();
                    } else {
                        Chat chatRecipient = new Chat();
                        chatRecipient.setUserId(senderUserId);
                        chatRecipient.setUserName(senderUserName);
                        chatRecipient.setMessage(typedText);
                        Boolean contactUserChatTest = saveChat(recipientUserId, senderUserId, chatRecipient);
                        if (!contactUserChatTest){
                            Toast.makeText(ChatActivity.this, "Chat could not be sent.", Toast.LENGTH_LONG).show();
                        }
                    }


                    messageTextView.setText("");

                }

            }
        });

    }

    private boolean saveMessage(String senderUserId, String recipientUserId, TextMessage message) {
        try{
            databaseReference = FirebaseConfiguration.getFirebase().child("messages");
            databaseReference.child( senderUserId )
                             .child( recipientUserId )
                             .push()
                             .setValue(message);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveChat(String senderUserId, String recipientUserId, Chat chat){
        try {
            databaseReference = FirebaseConfiguration.getFirebase().child("chats");
            databaseReference.child( senderUserId )
                             .child( recipientUserId )
                             .setValue(chat);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerMessage);
    }
}
