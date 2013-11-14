package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ListView;
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
    private GridView gridView;
    private PenugasanGridAdapter gridAdapter;

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

        String path = penugasanChildItem.getPath();
        File file = new File(path);
        List<File> files = new ArrayList<File>();
        int reqImg = 10;

        //Insert all files inside the directory into a List
        Collections.addAll(files, file.listFiles());


        List<PenugasanGridItem> gridItems = new ArrayList<PenugasanGridItem>();
        for(File f : files){
            gridItems.add(new PenugasanGridItem(f));
        }

        while(gridItems.size() < reqImg){
            gridItems.add(new PenugasanGridItem(Color.LTGRAY));
        }

        gridView = (GridView) view.findViewById(R.id.grid_penugasan_child);
        gridAdapter = new PenugasanGridAdapter(context, gridItems, R.layout.penugasan_grid_item);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new GridItemClickListener());

        return view;
    }

    //Open the Camera
    private void openCamera() {
        Intent openCameraIntent = new Intent(context, TakePictureActivity.class);
        context.startActivity(openCameraIntent);
    }

    private class GridItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            openCamera();
        }
    }
}
