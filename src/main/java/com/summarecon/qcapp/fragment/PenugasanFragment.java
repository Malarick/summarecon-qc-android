package com.summarecon.qcapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.PenugasanExpListAdapter;
import com.summarecon.qcapp.item.PenugasanChildItem;
import com.summarecon.qcapp.item.PenugasanParentItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PenugasanFragment extends Fragment {

    public static final String ARGS_PENUGASAN = "args_penugasan";

    private ExpandableListView mExpListPenugasan;
    private PenugasanExpListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_penugasan, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.lbl_test);
        textView.setText(getArguments().getCharSequence(ARGS_PENUGASAN, "PENUGASAN"));

        mExpListPenugasan = (ExpandableListView) rootView.findViewById(R.id.exp_list_penugasan);
        alignExpIndicatorToRight();
        populateExpListPenugasan();

        return rootView;
    }

    public void populateExpListPenugasan(){
        List<String> parentLblList = new ArrayList<String>();
        //List<String> childLblList = new ArrayList<String>();
        List<PenugasanParentItem> parentItemsList = new ArrayList<PenugasanParentItem>();

        Collections.addAll(parentLblList, getResources().getStringArray(R.array.arr_lbl_parent_items));
        //Collections.addAll(childLblList, getResources().getStringArray(R.array.arr_lbl_child_items));

        int c = 0;
        for(String sParent : parentLblList){
            PenugasanParentItem parentItem = new PenugasanParentItem(sParent);
            if(c == 1){
                parentItem.getChildItemList().add(new PenugasanChildItem(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                        + File.separator + "Camera" + File.separator));
            }else{
                parentItem.getChildItemList().add(new PenugasanChildItem(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()));
            }
            parentItemsList.add(parentItem);
            c++;
        }

        mAdapter = new PenugasanExpListAdapter(getActivity(), R.layout.penugasan_parent_item, R.layout.penugasan_child_item, parentItemsList);
        mExpListPenugasan.setAdapter(mAdapter);
    }

    public void alignExpIndicatorToRight(){
        //get device resolution size in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        mExpListPenugasan.setIndicatorBounds(width - getPixelsFromDips(66), width - getPixelsFromDips(26));
    }

    public int getPixelsFromDips(float pixels){
        //get the screen density scale
        float scale = getResources().getDisplayMetrics().density;

        //conver the dp(s) to pixels
        return (int) (scale * pixels + 0.5f);
    }
}