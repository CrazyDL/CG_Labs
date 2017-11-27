package com.crazydl.cg_lab6;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class ControlFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_layout_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SeekBar sBAprHor = getActivity().findViewById(R.id.sBAprHor);
        SeekBar sBAprVer = getActivity().findViewById(R.id.sBAprVer);
        SeekBar sBUpRad = getActivity().findViewById(R.id.sBUpRad);
        SeekBar sBDownRad = getActivity().findViewById(R.id.sBDownRad);
        sBAprHor.setOnSeekBarChangeListener(this);
        sBAprVer.setOnSeekBarChangeListener(this);
        sBUpRad.setOnSeekBarChangeListener(this);
        sBDownRad.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sBAprVer:
                if (seekBar.getProgress() < 4) {
                    MyGLRenderer.vertAppr = 4;
                } else {
                    MyGLRenderer.vertAppr = seekBar.getProgress();
                }
                break;
            case R.id.sBAprHor:
                if (seekBar.getProgress() < 4) {
                    MyGLRenderer.horAppr = 4;
                } else {
                    MyGLRenderer.horAppr = seekBar.getProgress();
                }
                break;
            case R.id.sBUpRad:
                MyGLRenderer.aLen = seekBar.getProgress() / 1000f;
                break;
            case R.id.sBDownRad:
                MyGLRenderer.bLen = seekBar.getProgress() / 1000f;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
