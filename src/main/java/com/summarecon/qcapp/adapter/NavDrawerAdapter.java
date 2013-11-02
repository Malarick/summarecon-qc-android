package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.item.NavDrawerItem;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        NavDrawerItem item = items.get(i);

        if(view == null){
            view = inflater.inflate(this.viewHolder, null);
        }

        textView_lbl = (TextView) view.findViewById(R.id.drawer_item_label);
        textView_lbl.setText(item.getTxt_item_label());

        return view;
    }
}
