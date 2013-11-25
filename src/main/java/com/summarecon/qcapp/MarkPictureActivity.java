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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.summarecon.qcapp.adapter.SpinnerListAdapter;
import com.summarecon.qcapp.customview.CustomScrollView;
import com.summarecon.qcapp.db.QCDBHelper;
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

public class MarkPictureActivity extends Activity {

    public static final String LOG_TAG = "MarkPictureActivity";
    public static final String PHOTO_URL = "PHOTO_URL";
    private Intent intent;

    private ImageView photoPreview;
    private Bitmap photoBitmap;
    private Bitmap oriPhotoBitmap;
    private String photoURL;
    private Canvas canvas;
    private SpinnerListAdapter adapter;

    private EditText mNote;
    private CustomScrollView rootScrollView;

    private QCDBHelper db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_picture);

        db = QCDBHelper.getInstance(this);

        rootScrollView = (CustomScrollView) findViewById(R.id.root_scroll_view);
        mNote = (EditText) findViewById(R.id.edt_note);

        //get Intent
        intent = getIntent();

        //show the photo's preview
        photoPreview = (ImageView) findViewById(R.id.img_photo_preview);
        loadPhoto();

        //On Touch Listener
        photoPreview.setOnTouchListener(new DrawingListener());

        populateSpinner(R.array.arr_lbl_status_defect, R.id.ddl_status_defect);
        populateSpinner(R.array.arr_lbl_status_pekerjaan, R.id.ddl_status_pekerjaan);
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
                if(savePhoto()){
                    Toast.makeText(this, "Save Successful", Toast.LENGTH_SHORT).show();
                    markFloorMap();
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

    private void loadPhoto() {
        photoURL = intent.getStringExtra("PHOTO_URL");
        photoBitmap = BitmapFactory.decodeFile(photoURL);
        oriPhotoBitmap = photoBitmap;
        photoPreview.setImageBitmap(photoBitmap);
    }

    private boolean savePhoto(){
        //Convert bitmap to Byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //File Output Stream
        //Proses write file
        byte[] final_data = byteArrayOutputStream.toByteArray();
        File pictureFile = new File(photoURL);
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream = new FileOutputStream(pictureFile);
            fileOutputStream.write(final_data);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        }catch (FileNotFoundException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }catch (IOException e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
    }

    private void markFloorMap(){
        Intent intent = new Intent(this, MarkFloorMapActivity.class);
        intent.putExtra(PHOTO_URL, photoURL);
        this.startActivity(intent);
    }

    private void clearDrawing(){
        photoBitmap = oriPhotoBitmap;
        photoPreview.setImageBitmap(photoBitmap);
    }

    private void populateSpinner(int res_spinner){
        List<SQII_PELAKSANAAN> listPelaksanaan = db.getAllPelaksanaan();
        List<SpinnerListItem> spinnerListItems = new ArrayList<SpinnerListItem>();

        for(SQII_PELAKSANAAN s : listPelaksanaan){
            spinnerListItems.add(new SpinnerListItem(s.getSTATUS_PEKERJAAN()));
        }

        adapter = new SpinnerListAdapter(this, R.layout.spinner_item, spinnerListItems);

        Spinner spinner = (Spinner) findViewById(res_spinner);
        spinner.setAdapter(adapter);
    }

    private void populateSpinner(int res_arr, int res_spinner){
        List<String> stringArrayList = new ArrayList<String>();
        List<SpinnerListItem> spinnerListItems = new ArrayList<SpinnerListItem>();

        Collections.addAll(stringArrayList, getResources().getStringArray(res_arr));
        for(String key : stringArrayList){
            String value = getString(getResources().getIdentifier(key, "string", this.getPackageName()));
            spinnerListItems.add(new SpinnerListItem(value));
        }

        adapter = new SpinnerListAdapter(this, R.layout.spinner_item, spinnerListItems);

        Spinner spinner = (Spinner) findViewById(res_spinner);
        spinner.setAdapter(adapter);
    }

    private class DrawingListener implements View.OnTouchListener{

        private float lineWidth = 10;
        private float startX, startY, endX, endY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    rootScrollView.setScrollAble(false);
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
                case MotionEvent.ACTION_UP:
                    rootScrollView.setScrollAble(true);
                    break;
            }

            return true;
        }

        public void drawLineOnCanvas(){
            //creating bitmap kosongan yg besarnya sama dengan photo yang di preview sebagai wadah coret2
            Bitmap b = Bitmap.createBitmap(photoBitmap.getWidth(), photoBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //assign bitmap kosongan ke canvas
            canvas = new Canvas(b);

            //draw bitmap dari preview photo di canvas
            canvas.drawBitmap(photoBitmap, 0, 0, null);

            //set line style
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(lineWidth);

            //Draw line and circle at end point
            float scaleX = (float) photoBitmap.getWidth() / (float) photoPreview.getWidth();
            float scaleY = (float) photoBitmap.getHeight() / (float) photoPreview.getHeight();
            float scaledStartX = startX * scaleX;
            float scaledStartY = startY * scaleY;
            float scaledEndX = endX * scaleX;
            float scaledEndY = endY * scaleY;
            float radius = lineWidth / 2;

            canvas.drawLine(scaledStartX, scaledStartY, scaledEndX, scaledEndY, paint);
            canvas.drawCircle(scaledEndX, scaledEndY, radius, paint);

            //Display on ImageView
            photoBitmap = b;
            photoPreview.setImageBitmap(photoBitmap);
        }
    }
}