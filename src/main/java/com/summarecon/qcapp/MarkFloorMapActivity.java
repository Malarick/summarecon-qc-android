package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarkFloorMapActivity extends Activity {

    private static final String LOG_TAG = "MarkFloorMapActivity";

    public static final String PARENT_ITEM_SQII_PELAKSANAAN = "PARENT_ITEM_SQII_PELAKSANAAN";
    public static final String ITEM_SQII_PELAKSANAAN = "ITEM_SQII_PELAKSANAAN";
    public static final String PHOTO_BUNDLE = "PHOTO_BUNDLE";
    public static final String ACTION_REPLACE = "ACTION_REPLACE";

    private Intent intent;

    private ImageView mapPreview;
    private Bitmap mapBitmap;
    private Bitmap oriMapBitmap;
    private String oriMapURL;
    private String mapURL;
    private String mapDir;
    private String mapName;
    private Boolean isReplace;
    private Canvas canvas;

    private QCDBHelper db;
    private SQII_PELAKSANAAN parent;
    private SQII_PELAKSANAAN item;

    private SpinnerListAdapter adapter;

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
        isReplace = bundle.getBoolean(ACTION_REPLACE);

        SQII_LANTAI_TIPE_RUMAH denah = db.getLantaiTipeRumah(item.getKD_LANTAI(), item.getKD_JENIS(), item.getKD_TIPE(), item.getKD_KAWASAN());

        oriMapURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + denah.getSRC_FOTO_DENAH();
        if(item.getSRC_FOTO_DENAH().equals("")){
            mapURL = oriMapURL;
        }else{
            mapURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + File.separator + item.getSRC_FOTO_DENAH();
        }

        mapDir = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY;
        Log.e("EXTRA_", item.getSRC_FOTO_DENAH());
        Log.e("EXTRA_", denah.getSRC_FOTO_DENAH());
        if(isReplace){
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
            db.updateItemDefectPenugasan(parent);
            Log.e("EXTRA_", item.getSRC_FOTO_DENAH());
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
}