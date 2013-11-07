package com.summarecon.qcapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summarecon.qcapp.R;

public class PenugasanFragment extends Fragment {

    public static final String ARGS_PENUGASAN = "args_penugasan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_penugasan, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.lbl_test);
        textView.setText(getArguments().getCharSequence(ARGS_PENUGASAN, "PENUGASAN"));

        return rootView;
    }
}