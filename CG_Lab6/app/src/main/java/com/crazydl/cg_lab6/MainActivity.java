package com.crazydl.cg_lab6;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main_layout);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backCount == 0) {
                    finish();
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.drawFragment, OpenGLFragment.getInstance()).addToBackStack(null).commit();
        }
    }

    private boolean supportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
