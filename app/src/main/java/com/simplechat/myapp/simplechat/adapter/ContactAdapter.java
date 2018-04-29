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
import com.simplechat.myapp.simplechat.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contactsArray;
    private Context context;

    public ContactAdapter(@NonNull Context c, @NonNull ArrayList<Contact> objects) {
        super(c, 0, objects);
        this.contactsArray = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (contactsArray != null){

// set object to assemble a view
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

// assemble view from a xml
            view = layoutInflater.inflate(R.layout.contact_list, parent, false);

            TextView contactName = view.findViewById(R.id.contactNameId);
            TextView contactEmail = view.findViewById(R.id.contactEmailId);

            Contact contact = contactsArray.get( position );
            contactName.setText(contact.getContactName());
            contactEmail.setText(contact.getContactEmail());



        }

        return view;
    }
}
