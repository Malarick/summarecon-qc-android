package com.summarecon.qcapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.TakePictureActivity;
import com.summarecon.qcapp.item.PenugasanChildItem;
import com.summarecon.qcapp.item.PenugasanGridItem;
import com.summarecon.qcapp.item.PenugasanParentItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanExpListAdapter extends BaseExpandableListAdapter {
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
        if(view == null){
            view = inflater.inflate(this.viewHolderChild, null);
        }

        PenugasanChildItem penugasanChildItem = getChild(parentPosition, childPosition);

        Log.e("EXPANDABLE", "Parent = " + parentPosition + "|||| Child = " + childPosition);

        //Load image (using different thread to reduce lag)
        ImageLoader imageLoader = new ImageLoader(view, penugasanChildItem);
        imageLoader.execute();


        return view;
    }

    //Open the Camera
    private void openCamera() {
        Intent openCameraIntent = new Intent(context, TakePictureActivity.class);
        context.startActivity(openCameraIntent);
    }

    private class ImageLoader extends AsyncTask<Void, Void, PenugasanGridAdapter>{

        ProgressBar progressBar;
        GridView gridView;
        View view;

        PenugasanChildItem penugasanChildItem;
        List<PenugasanGridItem> gridItems;

        private ImageLoader(View view, PenugasanChildItem penugasanChildItem) {
            this.gridView = gridView;
            this.penugasanChildItem = penugasanChildItem;
            this.view = view;
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
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(new GridItemClickListener());

            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected PenugasanGridAdapter doInBackground(Void... voids) {
            try{
                Thread.sleep(350);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }
            String path = penugasanChildItem.getPath();
            File file = new File(path);
            List<File> files = new ArrayList<File>();
            int reqImg = 10;

            //Insert all files inside the directory into a List
            Collections.addAll(files, file.listFiles());


            gridItems = new ArrayList<PenugasanGridItem>();
            for(File f : files){
                gridItems.add(new PenugasanGridItem(f));
            }

            while(gridItems.size() < reqImg){
                gridItems.add(new PenugasanGridItem(R.drawable.ic_default_photo));
            }

            PenugasanGridAdapter gridAdapter = new PenugasanGridAdapter(context, gridItems, R.layout.penugasan_grid_item);
            return gridAdapter;
        }

        private class GridItemClickListener implements AdapterView.OnItemClickListener{
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!gridItems.get(position).getIsFile()){
                    openCamera();
                }
            }
        }
    }
}
