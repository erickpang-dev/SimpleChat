package com.simplechat.myapp.simplechat.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simplechat.myapp.simplechat.fragment.ChatsFragment;
import com.simplechat.myapp.simplechat.fragment.ContactsFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitle = {
            "CHATS",
            "CONTACTS"
    };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ChatsFragment();
                break;
            case 1:
                fragment = new ContactsFragment();
                break;
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
