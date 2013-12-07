package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.item.NavDrawerItem;
import com.summarecon.qcapp.item.NotificationsItem;

import java.util.List;

/**
 * Created by arnold on 2/11/13.
 */
public class NavDrawerAdapter extends BaseAdapter {
    private Context context;
    private int viewHolder;
    private List<NavDrawerItem> items;
    private LayoutInflater inflater;
    private TextView textView_lbl;
    private ImageView imgView_icon;
    private TextView textView_counter;

    public NavDrawerAdapter(Context context, int viewHolder, List<NavDrawerItem> items) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
//        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        NavDrawerItem item = items.get(0);
        String label = item.getItemLabel();

        if(label.equals(QCConfig.JENIS_PENUGASAN_SISA) || label.equals(QCConfig.JENIS_PENUGASAN_ULANG) || label.equals(QCConfig.JENIS_PENUGASAN_BARU)){
            switch(position){
                case 0:
                    if(label.equals(QCConfig.JENIS_PENUGASAN_SISA) && item.getItemCounter() > 0){
                        return true;
                    }else{
                        return false;
                    }
                case 1:
                    if(isEnabled(position - 1)){
                        return false;
                    }else{
                        return true;
                    }
                case 2:
                    if(isEnabled(position - 2)){
                        return false;
                    }else if(isEnabled(position - 1)){
                        return false;
                    }else{
                        return true;
                    }
                default:
                    return true;
            }
        }else{
            return true;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NavDrawerItem item = items.get(i);

        view = inflater.inflate(this.viewHolder, null);

        ImageView imgView_icon = (ImageView) view.findViewById(R.id.drawer_item_icon);
        imgView_icon.setImageResource(item.getItemIcon());

        TextView textView_lbl = (TextView) view.findViewById(R.id.drawer_item_label);
        textView_lbl.setText(item.getItemLabel());

        if(item.counterExist && item.getItemCounter() > 0){
            TextView textView_counter = (TextView) view.findViewById(R.id.drawer_item_counter);
            textView_counter.setText(String.valueOf(item.getItemCounter()));
            textView_counter.setBackgroundResource(R.drawable.rectangle);
        }

        if(!isEnabled(i)){
            textView_lbl.setTextColor(Color.GRAY);
        }

        Log.e("COUNTER",  item.getItemLabel() +" = "+ item.counterExist.toString());

        return view;
    }
}
