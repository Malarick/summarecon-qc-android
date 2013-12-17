package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.summarecon.qcapp.MarkPictureActivity;
import com.summarecon.qcapp.R;
import com.summarecon.qcapp.TakePictureActivity;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.item.PenugasanChildItem;
import com.summarecon.qcapp.item.PenugasanGridItem;
import com.summarecon.qcapp.item.PenugasanParentItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanExpListAdapter extends BaseExpandableListAdapter {
    public static final String ACTIVITY = "GRID";

    private Context context;
    private List<PenugasanParentItem> parentItemList;
    private LayoutInflater inflater;
    private int viewHolderParent;
    private int viewHolderChild;

    private TextView txtItemLbl;
    private TextView txtItemTglLbl;
    private TextView txtItemBlokLbl;
    private TextView txtItemTotalImagesLbl;

    public PenugasanExpListAdapter(Context context, int viewHolderParent, int viewHolderChild, List<PenugasanParentItem> parentItemList) {
        this.context = context;
        this.parentItemList = parentItemList;
        this.viewHolderParent = viewHolderParent;
        this.viewHolderChild = viewHolderChild;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return parentItemList.size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        PenugasanParentItem penugasanParentItem = parentItemList.get(parentPosition);
        return penugasanParentItem.getChildItemList().size();
    }

    @Override
    public PenugasanParentItem getGroup(int parentPosition) {
        return parentItemList.get(parentPosition);
    }

    @Override
    public PenugasanChildItem getChild(int parentPosition, int childPosition) {
        PenugasanParentItem penugasanParentItem = parentItemList.get(parentPosition);
        return penugasanParentItem.getChildItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public View getGroupView(int parentPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        PenugasanParentItem penugasanParentItem = getGroup(parentPosition);

        if(view == null){
            view = inflater.inflate(this.viewHolderParent, null);
        }

        txtItemLbl = (TextView) view.findViewById(R.id.txt_penugasan_parent);
        txtItemLbl.setText(penugasanParentItem.getParentItemLbl());

        txtItemTglLbl = (TextView) view.findViewById(R.id.txt_penugasan_parent_tgl);
        txtItemTglLbl.setText(penugasanParentItem.getParentTglLbl());

        txtItemBlokLbl = (TextView) view.findViewById(R.id.txt_penugasan_parent_blok);
        txtItemBlokLbl.setText(penugasanParentItem.getParentBlokLbl());

        txtItemTotalImagesLbl = (TextView) view.findViewById(R.id.txt_penugasan_parent_total_images);
        txtItemTotalImagesLbl.setText(penugasanParentItem.getParentTotalImagesLbl());

        return view;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        view = inflater.inflate(this.viewHolderChild, null);

        PenugasanParentItem penugasanParentItem = getGroup(parentPosition);
        PenugasanChildItem penugasanChildItem = getChild(parentPosition, childPosition);

        Log.e("EXPANDABLE", "Parent = " + parentPosition + "|||| Child = " + childPosition);
        Log.e("CHILD_COUNT", String.valueOf(getChildrenCount(parentPosition)));

        //Load image (using different thread to reduce lag)
        ImageLoader imageLoader = new ImageLoader(context, view, penugasanChildItem, penugasanParentItem.getReqImages());
        imageLoader.execute();


        return view;
    }

    //Open the Camera
    private void openCamera(SQII_PELAKSANAAN parent, SQII_PELAKSANAAN item, int position) {
        Intent openCameraIntent = new Intent(context, TakePictureActivity.class);
        Bundle bundle = new Bundle();

        bundle.putFloat(TakePictureActivity.URUT_FOTO, item.getURUT_FOTO());
        bundle.putBoolean(TakePictureActivity.ACTION_REPLACE_DEFECT, false);
        bundle.putBoolean(TakePictureActivity.ACTION_REPLACE_DENAH, false);
        bundle.putSerializable(TakePictureActivity.PARENT_ITEM_SQII_PELAKSANAAN, parent);
        bundle.putSerializable(TakePictureActivity.ITEM_SQII_PELAKSANAAN, item);

        openCameraIntent.putExtra(TakePictureActivity.GRID_BUNDLE, bundle);
        context.startActivity(openCameraIntent);
    }

    private void editMarkPhoto(SQII_PELAKSANAAN parent, SQII_PELAKSANAAN item, int position){
        Intent editMarkPhotoIntent = new Intent(context, MarkPictureActivity.class);
        Bundle bundle = new Bundle();

        Log.e("EXTRA_", QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + item.getSRC_FOTO_DEFECT());
        bundle.putString(MarkPictureActivity.PHOTO_URL, QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + item.getSRC_FOTO_DEFECT());
        bundle.putString(MarkPictureActivity.PHOTO_DIR, QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY);
        bundle.putString(MarkPictureActivity.PHOTO_NAME, item.getSRC_FOTO_DEFECT());
        bundle.putFloat(MarkPictureActivity.URUT_FOTO, item.getURUT_FOTO());
        bundle.putBoolean(MarkPictureActivity.ACTION_REPLACE_DEFECT, true);
        bundle.putBoolean(MarkPictureActivity.ACTION_REPLACE_DENAH, false);
        bundle.putSerializable(MarkPictureActivity.PARENT_ITEM_SQII_PELAKSANAAN, parent);
        bundle.putSerializable(MarkPictureActivity.ITEM_SQII_PELAKSANAAN, item);
        bundle.putString(MarkPictureActivity.CALLING_ACTIVITY, ACTIVITY);

        editMarkPhotoIntent.putExtra(MarkPictureActivity.PHOTO_BUNDLE, bundle);
        context.startActivity(editMarkPhotoIntent);
    }

    private void retakePhoto(SQII_PELAKSANAAN parent, SQII_PELAKSANAAN item, int position){
        Intent retakePhotoIntent = new Intent(context, TakePictureActivity.class);
        Bundle bundle = new Bundle();

        Log.e("EXTRA_", QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + item.getSRC_FOTO_DEFECT());
        bundle.putFloat(TakePictureActivity.URUT_FOTO, item.getURUT_FOTO());
        bundle.putBoolean(TakePictureActivity.ACTION_REPLACE_DEFECT, true);
        bundle.putBoolean(TakePictureActivity.ACTION_REPLACE_DENAH, true);
        bundle.putSerializable(TakePictureActivity.PARENT_ITEM_SQII_PELAKSANAAN, parent);
        bundle.putSerializable(TakePictureActivity.ITEM_SQII_PELAKSANAAN, item);

        retakePhotoIntent.putExtra(TakePictureActivity.GRID_BUNDLE, bundle);
        context.startActivity(retakePhotoIntent);
    }

    private class ImageLoader extends AsyncTask<Void, Void, PenugasanGridAdapter>{

        Context context;
        ProgressBar progressBar;
        GridView gridView;
        View view;

        Integer reqImg;

        PenugasanChildItem penugasanChildItem;
        List<SQII_PELAKSANAAN> childList;
        SQII_PELAKSANAAN parent;
        List<PenugasanGridItem> gridItems;

        private ImageLoader(Context context, View view, PenugasanChildItem penugasanChildItem, Integer reqImg) {
            this.view = view;
            this.penugasanChildItem = penugasanChildItem;
            this.reqImg = reqImg;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gridView = (GridView) view.findViewById(R.id.grid_penugasan_child);
            gridView.setAdapter(null);

            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(PenugasanGridAdapter gridAdapter) {
            super.onPostExecute(gridAdapter);

            /* Responsive GridView heigh */
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            if(parent.getJML_FOTO_PENUGASAN() < 7){
                layoutParams.height = convertDpToPixels(80);
                //Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
            }else if(parent.getJML_FOTO_PENUGASAN() < 14){
                layoutParams.height = convertDpToPixels(160);
                //Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
            }else if(parent.getJML_FOTO_PENUGASAN() < 21){
                layoutParams.height = convertDpToPixels(240);
                //Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
            }else if(parent.getJML_FOTO_PENUGASAN() < 28){
                layoutParams.height = convertDpToPixels(320);
                //Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
            }else if(parent.getJML_FOTO_PENUGASAN() < 35){
                layoutParams.height = convertDpToPixels(400);
                //Log.e("EXTRA_1", layoutParams.height + " || " + parentList.get(groupPosition).getJML_FOTO_PENUGASAN());
            }
            gridView.setLayoutParams(layoutParams);


            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(new GridItemClickListener());
            gridView.setOnItemLongClickListener(new GridItemLongClickListener());

            progressBar.setVisibility(View.INVISIBLE);
        }

        public int convertDpToPixels(float dp){
            Resources resources = this.context.getResources();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    resources.getDisplayMetrics()
            );
        }

        @Override
        protected PenugasanGridAdapter doInBackground(Void... voids) {
            try{
                Thread.sleep(350);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }

            gridItems = new ArrayList<PenugasanGridItem>();

            parent = penugasanChildItem.getParent();
            childList = penugasanChildItem.getListChild();
            for(SQII_PELAKSANAAN pelaksanaan : childList){
                Log.e("EXTRA_", QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + " || " + pelaksanaan.getSRC_FOTO_DEFECT());
                if(!pelaksanaan.getSRC_FOTO_DEFECT().equals("")){
                    File file = new File(QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + pelaksanaan.getSRC_FOTO_DEFECT());
                    if(file.exists()){
                        gridItems.add(new PenugasanGridItem(file));
                    }else{
                        gridItems.add(new PenugasanGridItem(R.drawable.ic_default_photo));
                    }
                }else{
                    gridItems.add(new PenugasanGridItem(R.drawable.ic_default_photo));
                }
            }

//            String path = penugasanChildItem.getPath();
//            File file = new File(path);
//            List<File> files = new ArrayList<File>();
//
//            //Insert all files inside the directory into a List
//            Collections.addAll(files, file.listFiles());
//
//            for(File f : files){
//                gridItems.add(new PenugasanGridItem(f));
//            }

//            while(gridItems.size() < reqImg){
//                gridItems.add(new PenugasanGridItem(R.drawable.ic_default_photo));
//            }

            PenugasanGridAdapter gridAdapter = new PenugasanGridAdapter(context, gridItems, R.layout.penugasan_grid_item);
            return gridAdapter;
        }

        private class GridItemClickListener implements AdapterView.OnItemClickListener{
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!gridItems.get(position).getIsFile()){
                    openCamera(parent, childList.get(position), position);
                }else{
                    editMarkPhoto(parent, childList.get(position), position);
                }
            }
        }

        private class GridItemLongClickListener implements AdapterView.OnItemLongClickListener{
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(gridItems.get(position).getIsFile()){
                    retakePhoto(parent, childList.get(position), position);
                    return true;
                }

                return false;
            }
        }
    }
}
