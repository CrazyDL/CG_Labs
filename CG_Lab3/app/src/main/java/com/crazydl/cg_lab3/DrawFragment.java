package com.crazydl.cg_lab3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawFragment extends Fragment {
    DrawView drawView;

    public DrawFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.draw_layout_fragment, container, false);
        drawView = _view.findViewById(R.id.drawView);
        return _view;
    }
}
