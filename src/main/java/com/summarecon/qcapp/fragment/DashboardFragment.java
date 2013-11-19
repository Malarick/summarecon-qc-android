package com.summarecon.qcapp.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.MainActivity;
import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.NotificationsAdapter;
import com.summarecon.qcapp.item.NotificationsItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {
    private ListView mListView;
    private NotificationsAdapter mNotificationsAdapter;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Bundle fragmentArgs;

    private CharSequence mTitle;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mTitle = getActivity().getTitle();
        mListView = (ListView) rootView.findViewById(R.id.list_notifications);
        populateNotifications(mListView);

        return rootView;
    }

    public void populateNotifications(ListView listView){
        List<String> labelList = new ArrayList<String>();
        List<String> iconList = new ArrayList<String>();
        List<NotificationsItem> itemList = new ArrayList<NotificationsItem>();

        Collections.addAll(labelList, getResources().getStringArray(R.array.arr_lbl_section_notifications));
        Collections.addAll(iconList, getResources().getStringArray(R.array.arr_icon_section_notifications));

        int c = 0;
        for(String s : labelList){
            //Assign icon kecuali pada label yang tidak memiliki icon alias "null"
            if(iconList.get(c) != "null"){
                int id_icon = getResources().getIdentifier(iconList.get(c), "drawable", getActivity().getPackageName());
                itemList.add(new NotificationsItem(s, id_icon));
            }else{
                itemList.add(new NotificationsItem(s));
            }
            c++;
        }

        mNotificationsAdapter = new NotificationsAdapter(getActivity(), R.layout.notifications_item, itemList);
        listView.setAdapter(mNotificationsAdapter);
        listView.setOnItemClickListener(new NotificationsItemClickListener());
    }

    public void selectItem(AdapterView adapterView, View view, int position){
        CharSequence lblItem = ((TextView) view.findViewById(R.id.notifications_item_label)).getText();
        fragmentPenugasan(new PenugasanFragment(), lblItem);
        mTitle = lblItem;
        getActivity().setTitle(mTitle);
    }

    public void fragmentPenugasan(Fragment fragment, CharSequence jenisPenugasan){
        mFragment = fragment;
        MainActivity.mFragment = mFragment;
        fragmentArgs = new Bundle();

        fragmentArgs.putCharSequence(PenugasanFragment.ARGS_PENUGASAN, jenisPenugasan);
        mFragment.setArguments(fragmentArgs);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }

    private class NotificationsItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(adapterView, view, position);
        }
    }
}