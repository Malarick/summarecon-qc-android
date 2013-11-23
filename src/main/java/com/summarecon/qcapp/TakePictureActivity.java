package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakePictureActivity extends Activity {

    final static String LOG_TAG = "TakePictureActivity";
    final static String PHOTO_URL = "PHOTO_URL";
    final static int ZOOM_IN_INCREMENT = 2;
    final static int ZOOM_OUT_INCREMENT = -2;
    private Camera camera;
    private CameraPreview cameraPreview;
    private Camera.Parameters parameters;
    private ZoomControls zoomControls;
    private Button btnTakePicture;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        //initialize the camera
        cameraPreview = new CameraPreview(this);
        camera = cameraPreview.getCamera();
        parameters = camera.getParameters();

        //Tampilkan camera pada FrameLayout
        FrameLayout cameraLayout = (FrameLayout) findViewById(R.id.camera_layout);
        cameraLayout.addView(cameraPreview);

        //Handle listener untuk zoomControl
        setZoomControlListener();

        btnTakePicture = (Button) findViewById(R.id.btn_take_picture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_flash:
                if(setFlash() == 0){
                    item.setIcon(R.drawable.ic_action_flash);
                }else{
                    item.setIcon(R.drawable.ic_action_flash_off);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setZoomControlListener(){
        //Button ZoomControls
        //ZoomIn
        zoomControls = (ZoomControls) findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZoom(ZOOM_IN_INCREMENT);
            }
        });

        //ZoomOut
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZoom(ZOOM_OUT_INCREMENT);
            }
        });
    }

    public int setFlash(){
        int status = 0;

        //get Current Parameters
        parameters = camera.getParameters();

        //Check whether the flash is already on or off and do the opposite
        if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)){
            status = 1;
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }else if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)){
            status = 0;
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }

        camera.setParameters(parameters);
        return status;
    }

    public void setZoom(int zoomValue){
        //get Current Parameters
        parameters = camera.getParameters();

        int value = parameters.getZoom() + zoomValue;
        if(value < 0){
            value = 0;
        }else if(value > parameters.getMaxZoom()){
            value = parameters.getMaxZoom();
        }

        if(parameters.isSmoothZoomSupported()){
            Toast.makeText(this, "SmoothZoom Supported", 100).show();
        }else{
            Toast.makeText(this, "SmoothZoom Not Supported", 100).show();
        }
        parameters.setZoom(value);
        camera.setParameters(parameters);
    }

    public void takePicture(){
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //Play shutter sound
                MediaActionSound mediaActionSound = new MediaActionSound();
                mediaActionSound.play(MediaActionSound.SHUTTER_CLICK);

                //Setting path dan nama file
                File pictureFileDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                String pictureFileName = System.currentTimeMillis() + ".jpg";
                File pictureFile = new File(pictureFileDir, pictureFileName);

                //create directory if not exist
                if(!pictureFileDir.exists()){
                    pictureFileDir.mkdirs();
                }

                Bitmap raw_img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Creating the matrix to rotate the image 90 degree
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                //Creating the bitmap Bitmap
                raw_img = Bitmap.createBitmap(raw_img, 0, 0, raw_img.getWidth(), raw_img.getHeight(), matrix, true);

                //Convert bitmap to Byte
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                raw_img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                //File Output Stream
                //Proses write file
                byte[] final_data = byteArrayOutputStream.toByteArray();
                FileOutputStream fileOutputStream;
                try{
                    fileOutputStream = new FileOutputStream(pictureFile);
                    fileOutputStream.write(final_data);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }catch (FileNotFoundException e){
                    Log.e(LOG_TAG, e.getMessage());
                }catch (IOException e){
                    Log.e(LOG_TAG, e.getMessage());
                }

                //preview the photo by calling the markFloorActivity
                previewPhoto(pictureFile.getAbsolutePath());
            }
        };

        camera.takePicture(null, null, pictureCallback);
    }

    public void previewPhoto(String filePath){
        Intent intent = new Intent(this, MarkPictureActivity.class);
        intent.putExtra(PHOTO_URL, filePath);
        this.startActivity(intent);
    }
}