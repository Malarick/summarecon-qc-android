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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.MainActivity;
import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.NotificationsAdapter;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.db.SQII_USER;
import com.summarecon.qcapp.item.NotificationsItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {

    private QCDBHelper db;
    private ImageView img_profile;
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
    private ArrayList<SQII_PELAKSANAAN> pelaksanaan;
    private ArrayList<String> update_pelaksanaan;
    private ArrayList<String> foto_pelaksanaan;
    private ArrayList<String> filepreupload;

    private Button btn_upload;
    private String year,month,day;
    private Calendar today;
    private String server_ip;
    private String client_ip;

    public DashboardFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Init the DB
        db = QCDBHelper.getInstance(getActivity());

        //Init Ip Server untuk proses upload
        server_ip = QCConfig.getSharedPreferences().getString("server_ip_preference", "172.19.17.19");

        //Init Ip Client
        client_ip = QCConfig.getSharedPreferences().getString("client_ip_preference", "192.168.42.49");

        mTitle = getActivity().getTitle();
        mListView = (ListView) rootView.findViewById(R.id.list_notifications);
        populateNotifications(mListView);

        img_profile = (ImageView) rootView.findViewById(R.id.img_header_profile);
        //img_profile.setImageResource(APP_EXTERNAL_IMAGES_DIRECTORY);
        txt_profile_name = (TextView) rootView.findViewById(R.id.txt_profile_name);
        txt_profile_nik = (TextView) rootView.findViewById(R.id.txt_profile_nik);
        txt_profile_jabatan = (TextView) rootView.findViewById(R.id.txt_profile_position);

        btn_upload = (Button) rootView.findViewById(R.id.btn_upload);
        pelaksanaan = new ArrayList<SQII_PELAKSANAAN>();
        update_pelaksanaan = new ArrayList<String>();
        foto_pelaksanaan = new ArrayList<String>();
        filepreupload = new ArrayList<String>();

        bundle = getActivity().getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            nik = bundle.getString("nik");
            password = bundle.getString("password");
            DataUserProfile(nik);
        }

        today = Calendar.getInstance();
        day = String.format("%02d", today.get(Calendar.DATE));
        month = String.format("%02d", today.get(Calendar.MONTH)+1);
        year = String.format("%02d", today.get(Calendar.YEAR));

                btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pelaksanaan = (ArrayList<SQII_PELAKSANAAN>) db.getAllPelaksanaan();
                        for (int i = 0; i < pelaksanaan.size(); i++) {
                            update_pelaksanaan.add("UPDATE SQII_PELAKSANAAN SET TGL_PELAKSANAAN = '" + pelaksanaan.get(i).getTGL_PELAKSANAAN().toString() + "', STATUS_DEFECT = '" + pelaksanaan.get(i).getSTATUS_DEFECT().toString() + "', CATATAN = '" + pelaksanaan.get(i).getCATATAN().toString() + "', SRC_FOTO_DENAH = '" + pelaksanaan.get(i).getSRC_FOTO_DENAH().toString() + "', SRC_FOTO_DEFECT = '" + pelaksanaan.get(i).getSRC_FOTO_DEFECT().toString() + "' " +
                                                   "WHERE NO_PENUGASAN ='"+pelaksanaan.get(i).getTGL_PELAKSANAAN()+"'  AND " +
                                                          "KD_KAWASAN ='"+pelaksanaan.get(i).getKD_KAWASAN()+"' AND " +
                                                          "BLOK ='"+pelaksanaan.get(i).getBLOK()+"' AND " +
                                                          "NOMOR ='"+pelaksanaan.get(i).getNOMOR()+"' AND " +
                                                          "KD_JENIS ='"+pelaksanaan.get(i).getKD_JENIS()+"' AND " +
                                                          "KD_TIPE ='"+pelaksanaan.get(i).getKD_TIPE()+"' AND " +
                                                          "KD_ITEM_DEFECT ='"+pelaksanaan.get(i).getKD_ITEM_DEFECT()+"' AND " +
                                                          "KD_LANTAI ='"+pelaksanaan.get(i).getKD_LANTAI()+"' AND " +
                                                          "URUT_PELAKSANAAN = '"+pelaksanaan.get(i).getURUT_PELAKSANAAN()+"'");

                        }


                        GenerateScriptOnSD("UPLOAD_"+ nik + year + month + day +".txt", update_pelaksanaan);

                        /* Generate file Bat*/
                        for (int i = 0; i < pelaksanaan.size(); i++) {
                            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT()));
                            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH()));
                        }
                        GenerateBat("BAT_UPLOAD_"+ nik + year + month + day +".bat", foto_pelaksanaan);


                        /* Tambahkan dan generate*/
                        for (int i = 0; i < pelaksanaan.size(); i++) {
                            filepreupload.add("JENIS_PENUGASAN = '" + pelaksanaan.get(i).getJENIS_PENUGASAN() + "'|TGL_PElAKSANAAN = '" + year + "-" + month + "-" + day + "'|STATUS_DEFECT = '" + pelaksanaan.get(i).getSTATUS_DEFECT() + "'| STATUS_PEKERJAAN = '" + pelaksanaan.get(i).getSTATUS_PEKERJAAN() + "'|CATATAN = '" + pelaksanaan.get(i).getCATATAN() + "'|SRC_FOTO_DENAH = '" + pelaksanaan.get(i).getSRC_FOTO_DENAH() + "'|SRC_FOTO_DEFECT = '" + pelaksanaan.get(i).getSRC_FOTO_DEFECT() + "'#NO_PENUGASAN = '" + pelaksanaan.get(i).getNO_PENUGASAN() + "'|KD_KAWASAN = '" + pelaksanaan.get(i).getKD_KAWASAN() + "'|BLOK = '" + pelaksanaan.get(i).getBLOK() + "'|NOMOR = '" + pelaksanaan.get(i).getNOMOR() + "'|KD_JENIS = '" + pelaksanaan.get(i).getKD_JENIS() + "'|KD_TIPE = '" + pelaksanaan.get(i).getKD_TIPE() + "'|KD_ITEM_DEFECT = '" + pelaksanaan.get(i).getKD_ITEM_DEFECT() + "'|KD_LANTAI = '" + pelaksanaan.get(i).getKD_LANTAI() + "'|URUT_PELAKSANAAN = '" + pelaksanaan.get(i).getURUT_PELAKSANAAN() + "'|URUT_FOTO = '" + pelaksanaan.get(i).getURUT_FOTO() + "'");
                        }
                      GeneratePreUpload("PRE_UPLOAD_" + nik + year + month + day + ".txt", filepreupload);

                    }
                });

        return rootView;
    }

    private void DataUserProfile(String no_induk) {
        ArrayList<SQII_USER> user_profile;
        user_profile = (ArrayList<SQII_USER>) db.getUser(no_induk);

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
                    itemList.add(new NotificationsItem(s, db.getPelaksanaanJumlahFoto("201005469", QCConfig.KD_PENUGASAN_SISA), id_icon));
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_ULANG)) {
                    itemList.add(new NotificationsItem(s, db.getPelaksanaanJumlahFoto("201005469", QCConfig.KD_PENUGASAN_ULANG), id_icon));
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_BARU)) {
                    itemList.add(new NotificationsItem(s, db.getPelaksanaanJumlahFoto("201005469", QCConfig.KD_PENUGASAN_BARU), id_icon));
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

    public void GenerateScriptOnSD(String sFileName, ArrayList<String> sBody){
        int jum_data=sBody.size();
        try
        {
            File root = new File(QCConfig.APP_EXTERNAL_TEMP_DIRECTORY);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);

            for (int i=0;i<jum_data;i++){
                writer.append(sBody.get(i));
                    writer.append("\r\n");

            }
            writer.flush();
            writer.close();
            Toast.makeText(getActivity().getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }

    public void GenerateBat(String sFileName, ArrayList<String> sBody){
        int jum_data=sBody.size();
        try
        {
            File root = new File(QCConfig.APP_EXTERNAL_TEMP_DIRECTORY);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("@echo off");
            writer.append("\r\n");

            for (int i=0;i<jum_data;i++){
                writer.append(sBody.get(i));
                writer.append("\r\n");
            }

            writer.append("echo 'Proses Download Selesai'");
            writer.append("\r\n");
            writer.append("taskkill /f /im adb.exe");
            writer.flush();
            writer.close();
            Toast.makeText(getActivity().getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }

    public void GeneratePreUpload(String sFileName, ArrayList<String> sBody){
        int jum_data=sBody.size();
        try
        {
            File root = new File(QCConfig.APP_EXTERNAL_TEMP_DIRECTORY);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);

            for (int i=0;i<jum_data;i++){
                writer.append(sBody.get(i));
                if (i<jum_data-1){
                    writer.append("\r\n");
                }
            }

            writer.flush();
            writer.close();
            Toast.makeText(getActivity().getApplicationContext(), "Data berhasil dibuat", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }


    private static String uploadpenugasan(String stringUrl) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            inputStream = connection.getInputStream();
            Log.e("upload : ", inputStream.toString());
            return inputStream.toString();
        } catch (Exception e) {
            Log.w("x", "Error while uploading data from " + stringUrl, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}