package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.item.NavDrawerItem;
import com.summarecon.qcapp.item.NotificationsItem;

import java.util.List;

/**
 * Created by arnold on 7/11/13.
 */
public class NotificationsAdapter extends BaseAdapter {
    private Context context;
    private int viewHolder;
    private List<NotificationsItem> items;
    private LayoutInflater inflater;
    private TextView textView_lbl;
    private ImageView imgView_icon;

    public NotificationsAdapter(Context context, int viewHolder, List<NotificationsItem> items) {
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
        NotificationsItem item = items.get(i);

        if(view == null){
            view = inflater.inflate(this.viewHolder, null);
        }

        textView_lbl = (TextView) view.findViewById(R.id.notifications_item_label);
        textView_lbl.setText(item.getItemLabel());

        imgView_icon = (ImageView) view.findViewById(R.id.notifications_item_icon);
        imgView_icon.setImageResource(item.getItemIcon());


        return view;
    }
}
