package com.summarecon.qcapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private ArrayList<String> filedimas;

    private Button btn_upload;
    private String year,month,day;
    private Calendar today;
    private String server_ip;

    public DashboardFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Init the DB
        db = QCDBHelper.getInstance(getActivity());

        //Init Ip Server untuk proses upload
        server_ip = QCConfig.getSharedPreferences().getString("edittext_preference", "172.19.17.19");

        mTitle = getActivity().getTitle();
        mListView = (ListView) rootView.findViewById(R.id.list_notifications);
        populateNotifications(mListView);

        img_profile = (ImageView) rootView.findViewById(R.id.img_header_profile);
        txt_profile_name = (TextView) rootView.findViewById(R.id.txt_profile_name);
        txt_profile_nik = (TextView) rootView.findViewById(R.id.txt_profile_nik);
        txt_profile_jabatan = (TextView) rootView.findViewById(R.id.txt_profile_position);

        btn_upload = (Button) rootView.findViewById(R.id.btn_upload);
        pelaksanaan = new ArrayList<SQII_PELAKSANAAN>();
        update_pelaksanaan = new ArrayList<String>();
        foto_pelaksanaan = new ArrayList<String>();
        filedimas = new ArrayList<String>();

        //panggiluploadphp = new PanggilUploadFilePhp();

        bundle = getActivity().getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            nik = bundle.getString("nik");
            password = bundle.getString("password");
            DataUserProfile(nik);
        }

        today = Calendar.getInstance();
        day = String.format("%02d", today.get(Calendar.DATE));
        month = String.format("%02d", today.get(Calendar.MONTH+1));
        year = String.format("%02d", today.get(Calendar.YEAR));

        //Log.e("Date Today : ", year + "-" + month + "-" + day);

                btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pelaksanaan = (ArrayList<SQII_PELAKSANAAN>) db.getAllPelaksanaan();
                        for (int i = 0; i < pelaksanaan.size(); i++) {
                            update_pelaksanaan.add("--Update data pelaksanaan, data ke : " + String.valueOf(i + 1));
                            update_pelaksanaan.add("UPDATE SQII_PELAKSANAAN SET STATUS_DEFECT = '" + pelaksanaan.get(i).getSTATUS_DEFECT().toString() + "', STATUS_PEKERJAAN = '" + pelaksanaan.get(i).getSTATUS_PEKERJAAN().toString() + "', CATATAN = '" + pelaksanaan.get(i).getCATATAN().toString() + "', PATH_FOTO_DENAH = '" + pelaksanaan.get(i).getPATH_FOTO_DENAH().toString() + "', SRC_FOTO_DENAH = '" + pelaksanaan.get(i).getSRC_FOTO_DENAH().toString() + "', PATH_FOTO_DEFECT = '" + pelaksanaan.get(i).getPATH_FOTO_DEFECT().toString() + "', SRC_FOTO_DEFECT = '" + pelaksanaan.get(i).getSRC_FOTO_DEFECT().toString() + "' WHERE NO_PENUGASAN IS NOT NULL AND KD_KAWASAN IS NOT NULL AND BLOK IS NOT NULL AND NOMOR IS NOT NULL AND KD_JENIS IS NOT NULL AND KD_TIPE IS NOT NULL AND KD_ITEM_DEFECT IS NOT NULL AND KD_LANTAI IS NOT NULL AND URUT_PELAKSANAAN IS NOT NULL AND URUT_FOTO IS NOT NULL");
                            update_pelaksanaan.add(" ");
                        }


                        GenerateScriptOnSD("UPLOAD_"+ nik + year + month + day +".txt", update_pelaksanaan);

                        /* Bat Ok*/

                        for (int i = 0; i < pelaksanaan.size(); i++) {
                                                               //"c://xampp/htdocs/sqii_api/ext-download/sqii/bat_file/20100546920131130/adb" pull "/sdcard/Android/data/com.summarecon.qcapp/files/images/A.JPEG" "c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/20100546920131130/A.JPEG"
                            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext_upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT()));
                            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext_upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH()));
                        }
                        GenerateBat("BAT_UPLOAD_"+ nik + year + month + day +".txt", foto_pelaksanaan);

                        //Toast.makeText(getActivity().getApplicationContext(),"\"Hello\"",Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < pelaksanaan.size(); i++) {
                            filedimas.add("TGL_PElAKSANAAN='"+ year +"-"+ month +"-"+ day +"'|STATUS_DEFECT='"+pelaksanaan.get(i).getKD_ITEM_DEFECT()+"'|CATATAN='"+pelaksanaan.get(i).getCATATAN()+"'|SRC_FOTO_DENAH='"+pelaksanaan.get(i).getSRC_FOTO_DENAH()+"'|SRC_FOTO_DEFECT='"+pelaksanaan.get(i).getSRC_FOTO_DEFECT()+"'#NO_PENUGASAN='"+pelaksanaan.get(i).getNO_PENUGASAN()+"'|KD_KAWASAN='"+pelaksanaan.get(i).getKD_KAWASAN()+"'|BLOK='"+pelaksanaan.get(i).getBLOK()+"'|NOMOR='"+pelaksanaan.get(i).getNOMOR()+"'|KD_JENIS='"+pelaksanaan.get(i).getKD_JENIS()+"'|KD_TIPE='"+pelaksanaan.get(i).getKD_TIPE()+"'|KD_ITEM_DEFECT='"+pelaksanaan.get(i).getKD_ITEM_DEFECT()+"'|KD_LANTAI='"+pelaksanaan.get(i).getKD_LANTAI()+"'|URUT_PELAKSANAAN='"+pelaksanaan.get(i).getURUT_PELAKSANAAN()+"'|URUT_FOTO='"+pelaksanaan.get(i).getURUT_FOTO()+"'");
                            //filedimas.add("TGL_PElAKSANAAN='"+ year +"-"+ month +"-"+ day +"'|STATUS_DEFECT='S'|CATATAN='YYY'|SRC_FOTO_DENAH='B.JPEG'|SRC_FOTO_DEFECT='B.JPEG'#NO_PENUGASAN='F001'|KD_KAWASAN='BTK'|BLOK='BB'|NOMOR='001'|KD_JENIS='RMS'|KD_TIPE='RM467'|KD_ITEM_DEFECT='7'|KD_LANTAI='1'|URUT_PELAKSANAAN='1'|URUT_FOTO='2'");
                        }
                        GenerateDimas("pre_upload.txt",filedimas);

                        new PanggilUploadFilePhp().execute(); // Panggil Php untuk jalanin bat (permintaan dimas)
                        Toast.makeText(getActivity().getApplicationContext(),password,Toast.LENGTH_SHORT).show();
                    }
                });

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
                writer.append("\n");
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
            writer.append("\n");

            for (int i=0;i<jum_data;i++){
                writer.append(sBody.get(i));
                writer.append("\n");
            }

            writer.append("echo 'Proses Download Selesai'");
            writer.append("\n");
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

    public void GenerateDimas(String sFileName, ArrayList<String> sBody){
        int jum_data=sBody.size();
        try
        {
            File root = new File(QCConfig.APP_EXTERNAL_TEMP_DIRECTORY);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            //writer.append("@echo off");
            //writer.append("\n");

            for (int i=0;i<jum_data;i++){
                writer.append(sBody.get(i));
                writer.append("\n");
            }

            writer.append("echo 'Proses Download Selesai'");
            writer.append("\n");
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

    class PanggilUploadFilePhp extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;
        String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loading = new ProgressDialog(getActivity());
            loading.setMessage("Uploading data penugasan Please wait...");
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    PanggilUploadFilePhp.this.cancel(true);
                }
            });
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... Void) {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://192.168.42.49/sqii_api/index.php/sqii/c_das_api/upload/"+nik+"/"+password);
            //HttpPost request = new HttpPost("http://" + server_ip + "/sqii/ext-lib/agung_qc/get-penugasan.php");

            try {
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                // Write response to script file
                /*
                PrintStream out = null;
                try {
                    out = new PrintStream(new FileOutputStream(QCConfig.APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY));
                    out.print(response);
                } finally {
                    if (out != null) out.close();
                }
                */

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void returnValue) {
            //QCDBHelper.getInstance(getActivity()).executeSQLScriptFile();
            loading.dismiss();
            Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            //checkUserLogin();
        }
    }

}