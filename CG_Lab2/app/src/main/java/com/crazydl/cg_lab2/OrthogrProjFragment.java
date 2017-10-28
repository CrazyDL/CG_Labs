package com.crazydl.cg_lab2;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrthogrProjFragment extends Fragment {
    OrthogrProj orthogrProj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orthogr_proj_fragment, container, false);
        orthogrProj = view.findViewById(R.id.orthogr_pr_view);
        return view;
    }
}
