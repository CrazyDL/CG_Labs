package com.crazydl.cg_lab3;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawFragment extends Fragment {

    DrawView drawView;

    public DrawFragment() {}

    public static DrawFragment getInstance() {
        DrawFragment fragment = new DrawFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.draw_layout_fragment, container, false);
        drawView = _view.findViewById(R.id.drawView);
        return _view;
    }
}
