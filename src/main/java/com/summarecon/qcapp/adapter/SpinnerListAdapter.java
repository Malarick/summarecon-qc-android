package com.summarecon.qcapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.item.SpinnerListItem;

import java.util.List;

/**
 * Created by arnold on 31/10/13.
 */
public class SpinnerListAdapter extends BaseAdapter {
    private Context context;
    private int viewHolder;
    private List<SpinnerListItem> spinnerItem;
    private LayoutInflater inflater;

    public SpinnerListAdapter(Context context, int viewHolder, List<SpinnerListItem> spinnerItem) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.spinnerItem = spinnerItem;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return spinnerItem.size();
    }

    @Override
    public SpinnerListItem getItem(int i) {
        return spinnerItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SpinnerListItem item = spinnerItem.get(i);

        if(view == null){
            view = inflater.inflate(viewHolder, null);
        }

        TextView spinnerTextView = (TextView) view.findViewById(R.id.spinner_item);
        spinnerTextView.setText(item.getLbl_item());

        return view;
    }
}
