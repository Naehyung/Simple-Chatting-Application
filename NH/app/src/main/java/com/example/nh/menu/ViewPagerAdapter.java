package com.example.nh.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private String username;

    public ViewPagerAdapter(@NonNull FragmentManager fm, String username)
    {
        super(fm);

        this.username = username;

        fragment1 = Fragment1.newInstance(username, username);
        fragment2 = Fragment2.newInstance(username, username);
        fragment3 = new Fragment3();

        arrayList.add(fragment1);
        arrayList.add(fragment2);
        arrayList.add(fragment3);

        name.add("Friends");
        name.add("Chatting");
        name.add("Setting");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return name.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

       // Log.d("test", username);


        switch (position) {

            case 0:
                return fragment1;
            case 1:
                return fragment2;
            case 2:
                return fragment3;
            default:
                return null;


        }

    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
