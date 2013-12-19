package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.summarecon.qcapp.adapter.SpinnerListAdapter;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_LANTAI_TIPE_RUMAH;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.item.SpinnerListItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MarkFloorMapActivity extends Activity {

    private static final String LOG_TAG = "MarkFloorMapActivity";

    public static final String PARENT_ITEM_SQII_PELAKSANAAN = "PARENT_ITEM_SQII_PELAKSANAAN";
    public static final String ITEM_SQII_PELAKSANAAN = "ITEM_SQII_PELAKSANAAN";
    public static final String PHOTO_BUNDLE = "PHOTO_BUNDLE";
    public static final String ACTION_REPLACE_DEFECT = "ACTION_REPLACE_DEFECT";
    public static final String ACTION_REPLACE_DENAH = "ACTION_REPLACE_DENAH";

    private Intent intent;

    private ImageView mapPreview;
    private Bitmap mapBitmap;
    private Bitmap oriMapBitmap;
    private String oriMapURL;
    private String mapURL;
    private String mapDir;
    private String mapName;
    private Boolean isReplaceDefect;
    private Boolean isReplaceDenah;
    private Canvas canvas;

    private QCDBHelper db;
    private SQII_PELAKSANAAN parent;
    private SQII_PELAKSANAAN item;

    private SpinnerListAdapter adapter;

    /* variable generate script dan bat*/
    private ArrayList<SQII_PELAKSANAAN> pelaksanaan;
    private ArrayList<String> update_pelaksanaan;
    private ArrayList<String> foto_pelaksanaan;
    private ArrayList<String> filepreupload;
    private String year,month,day;
    private String nik;
    private Calendar today;
    private Bundle bundleLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_floor_map);

        //SETUP DB
        db = QCDBHelper.getInstance(this);

        //load the floor map and populate the spinner
        intent = getIntent();
        mapPreview = (ImageView) findViewById(R.id.img_map_preview);
        loadMap();
        //populateSpinner(R.id.ddl_lantai);

        mapPreview.setOnTouchListener(new DrawingListener());

        /*init variable generate script dan bat*/
        pelaksanaan = new ArrayList<SQII_PELAKSANAAN>();
        update_pelaksanaan = new ArrayList<String>();
        foto_pelaksanaan = new ArrayList<String>();
        filepreupload = new ArrayList<String>();

        nik = QCConfig.TMP_NIK;
        today = Calendar.getInstance();
        day = String.format("%02d", today.get(Calendar.DATE));
        month = String.format("%02d", today.get(Calendar.MONTH)+1);
        year = String.format("%02d", today.get(Calendar.YEAR));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                if(saveMap()){
                    Toast.makeText(this, "Save Successful", Toast.LENGTH_SHORT).show();
                    this.finish();
                }else{
                    Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_clear_drawing:
                clearDrawing();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateSpinner(int res_spinner){
        List<String> stringArrayList = new ArrayList<String>();
        List<SpinnerListItem> spinnerListItems = new ArrayList<SpinnerListItem>();

        Collections.addAll(stringArrayList, getResources().getStringArray(R.array.arr_lbl_lantai));
        for(String s:stringArrayList){
            spinnerListItems.add(new SpinnerListItem(s));
        }

        adapter = new SpinnerListAdapter(this, R.layout.spinner_item, spinnerListItems);

        Spinner spinner = (Spinner) findViewById(res_spinner);
        spinner.setAdapter(adapter);
    }

    private void loadMap() {
        Bundle bundle = intent.getBundleExtra(PHOTO_BUNDLE);
        parent = (SQII_PELAKSANAAN) bundle.getSerializable(PARENT_ITEM_SQII_PELAKSANAAN);
        item = (SQII_PELAKSANAAN) bundle.getSerializable(ITEM_SQII_PELAKSANAAN);
        isReplaceDefect = bundle.getBoolean(ACTION_REPLACE_DEFECT);
        isReplaceDenah = bundle.getBoolean(ACTION_REPLACE_DENAH);

        //Log.e("EXTRA_", "xxx" + parent.getTIPE_DENAH() + "xxx");
        SQII_LANTAI_TIPE_RUMAH denah = db.getLantaiTipeRumah(item.getKD_LANTAI(), item.getKD_JENIS(), item.getKD_TIPE(), item.getKD_KAWASAN(), parent.getTIPE_DENAH());

        oriMapURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + denah.getSRC_FOTO_DENAH();
        if(item.getSRC_FOTO_DENAH().equals("") || isReplaceDenah){
            mapURL = oriMapURL;
        }else{
            mapURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + item.getSRC_FOTO_DENAH();
        }

        mapDir = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY;
        //Log.e("EXTRA_", item.getSRC_FOTO_DENAH());
        //Log.e("EXTRA_", denah.getSRC_FOTO_DENAH());
        if(isReplaceDenah){
            mapName = item.getSRC_FOTO_DENAH();
        }else{
            mapName = item.getSRC_FOTO_DEFECT().replace(QCConfig.PREFIX_FILE_DEFECT, QCConfig.PREFIX_FILE_DENAH);
        }

        oriMapBitmap = BitmapFactory.decodeFile(oriMapURL);
        mapBitmap = BitmapFactory.decodeFile(mapURL);
        if(mapBitmap != null){
            mapPreview.setImageBitmap(mapBitmap);
        }
    }

    private void clearDrawing(){
        mapBitmap = oriMapBitmap;
        mapPreview.setImageBitmap(mapBitmap);
    }

    private boolean saveMap(){
        if(mapBitmap == null){
            return false;
        }

        //Convert bitmap to Byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mapBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //File Output Stream
        //Proses write file
        byte[] final_data = byteArrayOutputStream.toByteArray();
        File pictureFile = new File(mapDir, mapName);
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream = new FileOutputStream(pictureFile);
            fileOutputStream.write(final_data);
            fileOutputStream.flush();
            fileOutputStream.close();

            //UPDATE DB
            item.setPATH_FOTO_DENAH(mapDir);
            item.setSRC_FOTO_DENAH(mapName);
            db.updatePelaksanaan(item);
            generateFile();
            //db.updateItemDefectPenugasan(parent);
            //Log.e("EXTRA_", item.getSRC_FOTO_DENAH());
            return true;
        }catch (FileNotFoundException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }catch (IOException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
    }

    private class DrawingListener implements View.OnTouchListener{

        private float lineWidth = 10;
        private float startX, startY, endX, endY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    endX = motionEvent.getX();
                    endY = motionEvent.getY();
                    drawLineOnCanvas();
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
            }

            return true;
        }

        public void drawLineOnCanvas(){
            if(mapBitmap == null){
               return;
            }

            //creating bitmap kosongan yg besarnya sama dengan photo yang di preview sebagai wadah coret2
            Bitmap b = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //assign bitmap kosongan ke canvas
            canvas = new Canvas(b);

            //draw bitmap dari preview photo di canvas
            canvas.drawBitmap(mapBitmap, 0, 0, null);

            //set line style
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(lineWidth);

            //Draw line and circle at end point
            float scaleX = (float) mapBitmap.getWidth() / (float) mapPreview.getWidth();
            float scaleY = (float) mapBitmap.getHeight() / (float) mapPreview.getHeight();
            float scaledStartX = startX * scaleX;
            float scaledStartY = startY * scaleY;
            float scaledEndX = endX * scaleX;
            float scaledEndY = endY * scaleY;
            float radius = lineWidth / 2;

            canvas.drawLine(scaledStartX, scaledStartY, scaledEndX, scaledEndY, paint);
            canvas.drawCircle(scaledEndX, scaledEndY, radius, paint);

            //Display on ImageView
            mapBitmap = b;
            mapPreview.setImageBitmap(mapBitmap);
        }
    }

    /* Untuk Panggil Semua Generate*/
    public void generateFile(){
        int ctr_script=1, ctr_bat=1, ctr_pre=1;
        int ctr_script_name=1, ctr_bat_name=1, ctr_pre_name=1;
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
        generateScriptOnSD("UPLOAD_" + nik + year + month + day + ".txt", update_pelaksanaan);

                        /* Generate file Bat*/
        for (int i = 0; i < pelaksanaan.size(); i++) {
            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DEFECT()));
            foto_pelaksanaan.add(String.format("\"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/adb\" pull \"/sdcard/Android/data/com.summarecon.qcapp/files/images/%s\" \"c://xampp/htdocs/sqii_api/ext-upload/sqii/bat_file/%s%s%s%s/%s\"",nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH(),nik,year,month,day,pelaksanaan.get(i).getSRC_FOTO_DENAH()));
        }
        generateBat("BAT_UPLOAD_" + nik + year + month + day + ".bat", foto_pelaksanaan);


                        /* Tambahkan dan generate*/
        for (int i = 0; i < pelaksanaan.size(); i++) {
            filepreupload.add("JENIS_PENUGASAN = '" + pelaksanaan.get(i).getJENIS_PENUGASAN() + "'|TGL_PElAKSANAAN = '" + year + "-" + month + "-" + day + "'|STATUS_DEFECT = '" + pelaksanaan.get(i).getSTATUS_DEFECT() + "'| STATUS_PEKERJAAN = '" + pelaksanaan.get(i).getSTATUS_PEKERJAAN() + "'|CATATAN = '" + pelaksanaan.get(i).getCATATAN() + "'|SRC_FOTO_DENAH = '" + pelaksanaan.get(i).getSRC_FOTO_DENAH() + "'|SRC_FOTO_DEFECT = '" + pelaksanaan.get(i).getSRC_FOTO_DEFECT() + "'#NO_PENUGASAN = '" + pelaksanaan.get(i).getNO_PENUGASAN() + "'|KD_KAWASAN = '" + pelaksanaan.get(i).getKD_KAWASAN() + "'|BLOK = '" + pelaksanaan.get(i).getBLOK() + "'|NOMOR = '" + pelaksanaan.get(i).getNOMOR() + "'|KD_JENIS = '" + pelaksanaan.get(i).getKD_JENIS() + "'|KD_TIPE = '" + pelaksanaan.get(i).getKD_TIPE() + "'|KD_ITEM_DEFECT = '" + pelaksanaan.get(i).getKD_ITEM_DEFECT() + "'|KD_LANTAI = '" + pelaksanaan.get(i).getKD_LANTAI() + "'|URUT_PELAKSANAAN = '" + pelaksanaan.get(i).getURUT_PELAKSANAAN() + "'|URUT_FOTO = '" + pelaksanaan.get(i).getURUT_FOTO() + "'");
        }
        generatePreUpload("PRE_UPLOAD_" + nik + year + month + day + ".txt", filepreupload);
    }

    public void generateScriptOnSD(String sFileName, ArrayList<String> sBody){
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
            //Toast.makeText(this.getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }

    public void generateBat(String sFileName, ArrayList<String> sBody){
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
            //Toast.makeText(this.getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }

    public void generatePreUpload(String sFileName, ArrayList<String> sBody){
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
            Toast.makeText(this.getApplicationContext(), "Data berhasil dibuat", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
    }
}