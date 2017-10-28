package com.crazydl.cg_lab2;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context cnt) {
        super(fm);
        context = cnt;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrthogrProjFragment();
            case 1:
                return new IsomProjFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.orthographic_projection);
            case 1:
                return context.getString(R.string.isometric_projection);
            default:
                return null;
        }
    }
}
