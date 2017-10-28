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

public class IsomProjFragment extends Fragment {
    IsomProj isomProj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.isom_proj_fragment, container, false);
        isomProj = view.findViewById(R.id.isom_pr_view);
        return view;
    }
}
