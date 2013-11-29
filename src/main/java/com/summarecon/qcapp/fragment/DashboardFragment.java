package com.summarecon.qcapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.summarecon.qcapp.MainActivity;
import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.NotificationsAdapter;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_USER;
import com.summarecon.qcapp.item.NotificationsItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {

    private QCDBHelper db;
    private TextView txt_profile_name, txt_profile_nik, txt_profile_jabatan;
    private ListView mListView;
    private NotificationsAdapter mNotificationsAdapter;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Bundle fragmentArgs;
    private String nik;
    private String password;
    private CharSequence mTitle;
    private Bundle bundle = new Bundle();

    public DashboardFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Init the DB
        db = QCDBHelper.getInstance(getActivity());

        mTitle = getActivity().getTitle();
        mListView = (ListView) rootView.findViewById(R.id.list_notifications);
        populateNotifications(mListView);

        txt_profile_name = (TextView) rootView.findViewById(R.id.txt_profile_name);
        txt_profile_nik = (TextView) rootView.findViewById(R.id.txt_profile_nik);
        txt_profile_jabatan = (TextView) rootView.findViewById(R.id.txt_profile_position);

        bundle = getActivity().getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            nik = bundle.getString("nik");
            password = bundle.getString("password");
            DataUserProfile(nik);
        }

        return rootView;
    }

    private void DataUserProfile(String no_induk) {
        ArrayList<SQII_USER> user_profile;
        user_profile = (ArrayList<SQII_USER>) db.getUser(no_induk);
        //user_profile.get(0).getNAMA();

        if ((user_profile.get(0).getFLAG_PETUGAS_ADMIN()).equals("Y")) {
            txt_profile_jabatan.setText("ADMIN QC");
        } else if ((user_profile.get(0).getFLAG_SM()).equals("Y")) {
            txt_profile_jabatan.setText("SITE MANAGER");
        } else if ((user_profile.get(0).getFLAG_PENGAWAS()).equals("Y")) {
            txt_profile_jabatan.setText("PENGAWAS QC");
        } else if ((user_profile.get(0).getFLAG_PETUGAS_QC()).equals("Y")) {
            txt_profile_jabatan.setText("PETUGAS QC");
        }

        txt_profile_name.setText(user_profile.get(0).getNAMA());
        txt_profile_nik.setText(user_profile.get(0).getNO_INDUK());
    }

    public void populateNotifications(ListView listView) {
        List<String> labelList = new ArrayList<String>();
        List<String> iconList = new ArrayList<String>();
        List<NotificationsItem> itemList = new ArrayList<NotificationsItem>();

        Collections.addAll(labelList, getResources().getStringArray(R.array.arr_lbl_section_notifications));
        Collections.addAll(iconList, getResources().getStringArray(R.array.arr_icon_section_notifications));

        int c = 0;
        for (String s : labelList) {
            //Assign icon kecuali pada label yang tidak memiliki icon alias "null"
            if (iconList.get(c) != "null") {
                int id_icon = getResources().getIdentifier(iconList.get(c), "drawable", getActivity().getPackageName());
                if (s.equals(QCConfig.JENIS_PENUGASAN_SISA)) {
                    itemList.add(new NotificationsItem(s, db.getAllPelaksanaan("201005469", QCConfig.KD_PENUGASAN_SISA).size(), id_icon));
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_ULANG)) {
                    itemList.add(new NotificationsItem(s, db.getAllPelaksanaan("201005469", QCConfig.KD_PENUGASAN_ULANG).size(), id_icon));
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_BARU)) {
                    itemList.add(new NotificationsItem(s, db.getAllPelaksanaan("201005469", QCConfig.KD_PENUGASAN_BARU).size(), id_icon));
                } else {
                    itemList.add(new NotificationsItem(s, id_icon));
                }
            } else {
                itemList.add(new NotificationsItem(s));
            }
            c++;
        }

        mNotificationsAdapter = new NotificationsAdapter(getActivity(), R.layout.notifications_item, itemList);
        listView.setAdapter(mNotificationsAdapter);
        listView.setOnItemClickListener(new NotificationsItemClickListener());
    }

    public void selectItem(AdapterView adapterView, View view, int position) {
        CharSequence lblItem = ((TextView) view.findViewById(R.id.notifications_item_label)).getText();
        String jenisPenugasan = null;
        if(lblItem.equals(QCConfig.JENIS_PENUGASAN_SISA)){
            jenisPenugasan = QCConfig.KD_PENUGASAN_SISA;
        } else if(lblItem.equals(QCConfig.JENIS_PENUGASAN_ULANG)){
            jenisPenugasan = QCConfig.KD_PENUGASAN_ULANG;
        } else if(lblItem.equals(QCConfig.JENIS_PENUGASAN_BARU)){
            jenisPenugasan = QCConfig.KD_PENUGASAN_BARU;
        }
        fragmentCallPenugasan(new PenugasanFragment(), jenisPenugasan);
        mTitle = lblItem;
        getActivity().setTitle(mTitle);
    }

    public void fragmentCallPenugasan(Fragment fragment, String jenisPenugasan) {
        mFragment = fragment;
        MainActivity.mFragment = mFragment;
        fragmentArgs = new Bundle();

        fragmentArgs.putString(PenugasanFragment.ARGS_PENUGASAN, jenisPenugasan);
        mFragment.setArguments(fragmentArgs);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }

    private class NotificationsItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(adapterView, view, position);
        }
    }
}