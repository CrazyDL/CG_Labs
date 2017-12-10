package com.crazydl.cg_cp;

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
        SeekBar sBApr = getActivity().findViewById(R.id.sBApr);
        sBApr.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sBApr:
                if (seekBar.getProgress() < 4) {
                    DrawView.step = 1f / 4f;
                } else {
                    DrawView.step = 1f / seekBar.getProgress();
                }
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
