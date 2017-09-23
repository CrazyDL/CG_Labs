package com.crazydl.cg_lab1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataFragment extends Fragment {
    Button btnBuildGraph;
    EditText inpA, inpK, inpB;
    View graphView;
    InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnBuildGraph = getActivity().findViewById(R.id.btnBuildGraph);
        inpA = getActivity().findViewById(R.id.inpA);
        inpK = getActivity().findViewById(R.id.inpK);
        inpB = getActivity().findViewById(R.id.inpB);
        graphView = getActivity().findViewById(R.id.graphView);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        btnBuildGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float a = Float.valueOf(inpA.getText().toString());
                    float k = Float.valueOf(inpK.getText().toString());
                    float B = Float.valueOf(inpB.getText().toString());
                    GraphView.setConstants(a, k, B);
                    graphView.invalidate();
                    inpA.clearFocus();
                    inpK.clearFocus();
                    inpB.clearFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (NumberFormatException e){
                    Toast.makeText(getContext(), "Wrong parameters!", Toast.LENGTH_LONG).show();
                }

            }
        });

        graphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inpA.clearFocus();
                inpK.clearFocus();
                inpB.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
}
