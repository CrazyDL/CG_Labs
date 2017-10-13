package com.crazydl.cg_lab2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(myFragmentPagerAdapter);

        TabLayout tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(viewPager);
    }
}
