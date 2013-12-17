package com.summarecon.qcapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.PenugasanExpListAdapter;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.item.PenugasanChildItem;
import com.summarecon.qcapp.item.PenugasanParentItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PenugasanFragment extends Fragment {

    public static final String ARGS_PENUGASAN = "args_penugasan";

    private ExpandableListView mExpListPenugasan;
    private GridView mGridViewPenugasan;
    private PenugasanExpListAdapter mAdapter;
    private String jenisPenugasan;
    private String kdJenisPenugasan;
    private int lastGroupPosition = 0;
    private QCDBHelper db;

    private Bundle bundleLogin;
    private String nik;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_penugasan, container, false);
        View childView = inflater.inflate(R.layout.penugasan_child_item, null);

        db = QCDBHelper.getInstance(getActivity());

        bundleLogin = getActivity().getIntent().getBundleExtra("bundleLogin");
        if(bundleLogin != null){
            nik = bundleLogin.getString("nik");
        }

        TextView textView = (TextView) rootView.findViewById(R.id.lbl_test);
        kdJenisPenugasan = getArguments().getString(ARGS_PENUGASAN, "PENUGASAN");
        if(kdJenisPenugasan.equals(QCConfig.KD_PENUGASAN_SISA)){
            jenisPenugasan = QCConfig.JENIS_PENUGASAN_SISA;
        }else if(kdJenisPenugasan.equals(QCConfig.KD_PENUGASAN_ULANG)){
            jenisPenugasan = QCConfig.JENIS_PENUGASAN_ULANG;
        }else{
            jenisPenugasan = QCConfig.JENIS_PENUGASAN_BARU;
        }
        textView.setText(jenisPenugasan);

        mExpListPenugasan = (ExpandableListView) rootView.findViewById(R.id.exp_list_penugasan);
        mGridViewPenugasan = (GridView) childView.findViewById(R.id.grid_penugasan_child);
        alignExpIndicatorToRight();

        return rootView;
    }

    @Override
    public void onResume() {
        populateExpListPenugasan();
        mExpListPenugasan.expandGroup(lastGroupPosition, true);
        super.onResume();
    }

    public void populateExpListPenugasan(){
        final List<SQII_PELAKSANAAN> parentList = db.getAllPelaksanaan(nik, kdJenisPenugasan);
        List<SQII_PELAKSANAAN> childList;

        List<PenugasanParentItem> parentItemsList = new ArrayList<PenugasanParentItem>();
        //List<String> parentLblList = new ArrayList<String>();
        //List<String> childLblList = new ArrayList<String>();

        //Collections.addAll(parentLblList, getResources().getStringArray(R.array.arr_lbl_parent_items));
        //Collections.addAll(childLblList, getResources().getStringArray(R.array.arr_lbl_child_items));

        int c = 0;
        int row_id = 1;
        for(SQII_PELAKSANAAN sParent : parentList){
            PenugasanParentItem parentItem = new PenugasanParentItem(
                    row_id + ". " + sParent.getNM_CLUSTER()
                    , sParent.getTGL_PENUGASAN()
                    , "Blok: " + sParent.getBLOK() + "/" + sParent.getNOMOR() + ", Lantai: " + sParent.getNM_LANTAI()
                    , sParent.getNM_ITEM_DEFECT() + ": " + String.format("%.0f", sParent.getJML_FOTO_REALISASI()) + "/" + String.format("%.0f", sParent.getJML_FOTO_PENUGASAN())
                    , sParent.getJML_FOTO_PENUGASAN()
            );

            childList = db.getAllPelaksanaan(
                    sParent.getNO_PENUGASAN()
                    , sParent.getKD_KAWASAN()
                    , sParent.getBLOK()
                    , sParent.getNOMOR()
                    , sParent.getKD_JENIS()
                    , sParent.getKD_TIPE()
                    , sParent.getKD_ITEM_DEFECT()
                    , sParent.getKD_LANTAI()
                    , sParent.getURUT_PELAKSANAAN()
            );

            //parentItem.getChildItemList().add(new PenugasanChildItem(sChild.getPATH_FOTO_DEFECT()));
            parentItem.getChildItemList().add(new PenugasanChildItem(sParent, childList));
            //parentItem.getChildItemList().add(new PenugasanChildItem(QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY));

            //if((c % 2) == 0){
            //    parentItem.getChildItemList().add(new PenugasanChildItem(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
            //            + File.separator + "Camera" + File.separator));
            //}else{
            //    parentItem.getChildItemList().add(new PenugasanChildItem(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()));
            //}
            parentItemsList.add(parentItem);
            c++;
            row_id++;
        }

        mAdapter = new PenugasanExpListAdapter(getActivity(), R.layout.penugasan_parent_item, R.layout.penugasan_child_item, parentItemsList);
        mExpListPenugasan.setAdapter(mAdapter);
        mExpListPenugasan.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != lastGroupPosition){
                    mExpListPenugasan.collapseGroup(lastGroupPosition);
                }

                ViewGroup.LayoutParams layoutParams = mGridViewPenugasan.getLayoutParams();
                Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());

                if(parentList.get(groupPosition).getJML_FOTO_PENUGASAN() < 7){
                    layoutParams.height = convertDpToPixels(80, getActivity());
                    Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                }else if(parentList.get(groupPosition).getJML_FOTO_PENUGASAN() < 14){
                    layoutParams.height = convertDpToPixels(160, getActivity());
                    Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                }else if(parentList.get(groupPosition).getJML_FOTO_PENUGASAN() < 21){
                    layoutParams.height = convertDpToPixels(240, getActivity());
                    Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                }else if(parentList.get(groupPosition).getJML_FOTO_PENUGASAN() < 28){
                    layoutParams.height = convertDpToPixels(320, getActivity());
                    Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                }else if(parentList.get(groupPosition).getJML_FOTO_PENUGASAN() < 35){
                    layoutParams.height = convertDpToPixels(400, getActivity());
                    Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                }

                Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
                mGridViewPenugasan.setLayoutParams(layoutParams);
                lastGroupPosition = groupPosition;
            }
        });
    }

    public void alignExpIndicatorToRight(){
        //get device resolution size in pixels
        //Get Display Size and contain it in displayMetrics
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

        //convert the dp(s) to pixels
        return (int) (scale * pixels + 0.5f);
    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}