package com.example.nh.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.nh.R;
import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {

    public static final String TAG = "MenuActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String username;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        Log.d(TAG, username);

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),username);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);




    }

    public void reloadFragment() {
        adapter.notifyDataSetChanged();
    }




}