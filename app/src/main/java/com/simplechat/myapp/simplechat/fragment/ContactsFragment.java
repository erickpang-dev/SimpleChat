package com.simplechat.myapp.simplechat.fragment;


import android.content.Intent;
import android.os.Bundle;
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
import com.simplechat.myapp.simplechat.activity.MainActivity;
import com.simplechat.myapp.simplechat.adapter.ContactAdapter;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.Base64Converter;
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.model.Contact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ListView contactsListView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Contact> contactArray;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContacts;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerContacts);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactArray = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactsListView = view.findViewById(R.id.contactsListViewId);

        arrayAdapter = new ContactAdapter(getActivity(),contactArray);

        contactsListView.setAdapter(arrayAdapter);
// get current user identifier
        Preferences preferences = new Preferences(getActivity());
        String currentUserIdentifier = preferences.getCurrentUserIdentifier();

// get contacts from firebase database
        databaseReference = FirebaseConfiguration.getFirebase();
        databaseReference = databaseReference.child("contacts").child(currentUserIdentifier);

// set listener for contacts on firebase
        valueEventListenerContacts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// clear list to prevent repeated items
                contactArray.clear();
// add contact name to listview
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Contact contact = data.getValue(Contact.class);
                    contactArray.add( contact );
                }
// notify adapter for list changes
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contactArray.get(position);

                Intent ContactToChatActivity = new Intent(getActivity(), ChatActivity.class);
                ContactToChatActivity.putExtra("contactName", contact.getContactName());
                ContactToChatActivity.putExtra("contactEmail", contact.getContactEmail());

                startActivity(ContactToChatActivity);
            }
        });

        return view;
    }

}
