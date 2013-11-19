package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.summarecon.qcapp.adapter.SpinnerListAdapter;
import com.summarecon.qcapp.item.SpinnerListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarkFloorMapActivity extends Activity {

    private Intent intent;

    private ImageView mapPreview;
    private Bitmap mapBitmap;
    private Bitmap oriMapBitmap;
    private String mapURL;
    private Canvas canvas;

    private SpinnerListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_floor_map);

        //load the floor map and populate the spinner
        intent = getIntent();
        mapPreview = (ImageView) findViewById(R.id.img_map_preview);
        loadMap();
        populateSpinner(R.id.ddl_lantai);

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
        mapURL = intent.getStringExtra("PHOTO_URL");
        mapBitmap = BitmapFactory.decodeFile(mapURL);
        oriMapBitmap = mapBitmap;
        mapPreview.setImageBitmap(mapBitmap);
    }

    private void clearDrawing(){
        mapBitmap = oriMapBitmap;
        mapPreview.setImageBitmap(mapBitmap);
    }

    private boolean saveMap(){
        return false;
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