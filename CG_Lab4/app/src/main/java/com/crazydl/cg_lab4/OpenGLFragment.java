package com.crazydl.cg_lab4;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OpenGLFragment extends Fragment {

    private GLSurfaceView mGLView;

    public OpenGLFragment() {
        super();
    }

    public static OpenGLFragment getInstance() {
        OpenGLFragment fragment = new OpenGLFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGLView = new MyGLSurfaceView(getActivity().getApplicationContext());
        return mGLView;
    }


}
