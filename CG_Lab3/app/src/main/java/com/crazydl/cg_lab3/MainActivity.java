package com.crazydl.cg_lab3;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.drawFragment, DrawFragment.getInstance());
        fragmentTransaction.commit();
    }
}
