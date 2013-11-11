package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.item.PenugasanGridItem;

import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<PenugasanGridItem> items;
    private int viewHolder;

    private ImageView imgItem;

    public PenugasanGridAdapter(Context context, List<PenugasanGridItem> items, int viewHolder) {
        this.mContext = context;
        this.items = items;
        this.viewHolder = viewHolder;
        inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PenugasanGridItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        PenugasanGridItem item = getItem(position);

        if(view == null){
            view = inflater.inflate(viewHolder, null);
        }

        imgItem = (ImageView) view.findViewById(R.id.img_grid_item);

        if(item.getIsFile()){
            //create Thumbnail
            Bitmap photoThumbnail = createThumbnail(item, 8);
            imgItem.setImageBitmap(photoThumbnail);
        }else{
            imgItem.setBackgroundColor(item.getRes());
        }

        return view;
    }

    public Bitmap createThumbnail(PenugasanGridItem item, int resizeRate){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = resizeRate;
        Bitmap photoThumbnail = BitmapFactory.decodeFile(item.getFile().getAbsolutePath(), opts);

        return photoThumbnail;
    }
}
