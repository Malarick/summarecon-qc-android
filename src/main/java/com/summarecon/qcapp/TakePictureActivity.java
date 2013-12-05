package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakePictureActivity extends Activity {

    final static String LOG_TAG = "TakePictureActivity";

    final static int ZOOM_IN_INCREMENT = 2;
    final static int ZOOM_OUT_INCREMENT = -2;

    public final static String ACTIVITY = "PHOTO";
    public final static String PARENT_ITEM_SQII_PELAKSANAAN = "PARENT_ITEM_SQII_PELAKSANAAN";
    public final static String ITEM_SQII_PELAKSANAAN = "ITEM_SQII_PELAKSANAAN";
    public final static String URUT_FOTO = "URUT_FOTO";
    public final static String GRID_BUNDLE = "GRID_BUNDLE";
    public final static String ACTION_REPLACE_DEFECT = "ACTION_REPLACE_DEFECT";
    public static final String ACTION_REPLACE_DENAH = "ACTION_REPLACE_DENAH";

    private FrameLayout cameraLayout;
    private Camera camera;
    private CameraPreview cameraPreview;
    private Camera.Parameters parameters;
    private ZoomControls zoomControls;
    private Button btnTakePicture;

    private QCDBHelper db;
    private SQII_PELAKSANAAN parent;
    private SQII_PELAKSANAAN item;
    private Float urutFoto;
    private Boolean isReplaceDefect;
    private Boolean isReplaceDenah;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        db = QCDBHelper.getInstance(this);
        cameraLayout = (FrameLayout) findViewById(R.id.camera_layout);

        //GET ITEM FROM GRIDVIEW
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra(GRID_BUNDLE);
        parent = (SQII_PELAKSANAAN) bundle.getSerializable(PARENT_ITEM_SQII_PELAKSANAAN);
        item = (SQII_PELAKSANAAN) bundle.getSerializable(ITEM_SQII_PELAKSANAAN);
        urutFoto = bundle.getFloat(URUT_FOTO, 1);
        isReplaceDefect = bundle.getBoolean(ACTION_REPLACE_DEFECT);
        isReplaceDenah = bundle.getBoolean(ACTION_REPLACE_DENAH);

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
    protected void onResume() {
        //initialize the camera
        cameraPreview = new CameraPreview(this);
        camera = cameraPreview.getCamera();
        Log.e(LOG_TAG, camera.toString());
        parameters = camera.getParameters();

        //Tampilkan camera pada FrameLayout
        cameraLayout.addView(cameraPreview);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraLayout.removeAllViews();
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
            Toast.makeText(this, "SmoothZoom Supported", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "SmoothZoom Not Supported", Toast.LENGTH_SHORT).show();
        }
        parameters.setZoom(value);
        camera.setParameters(parameters);
    }

    public void takePicture(){
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //Play shutter sound
                //MediaActionSound mediaActionSound = new MediaActionSound();
                //mediaActionSound.play(MediaActionSound.SHUTTER_CLICK);

                //Setting path dan nama file
                File pictureFileDir = new File(QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY);
                String pictureFileName = QCConfig.PREFIX_FILE_DEFECT + parent.getNM_CLUSTER() + "_" + System.currentTimeMillis() + ".jpg";
                if(isReplaceDefect){
                    pictureFileName = item.getSRC_FOTO_DEFECT();
                }

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
                previewPhoto(pictureFile.getAbsolutePath(), pictureFileDir.getAbsolutePath(), pictureFileName);
            }
        };

        camera.takePicture(null, null, pictureCallback);
    }

    public void previewPhoto(String filePath, String fileDir, String fileName){
        Intent intent = new Intent(this, MarkPictureActivity.class);

        //MASUKKAN SEMUA PARAMETER YANG DIPERLUKAN KE DALAM BUNDLE
        Bundle bundle = new Bundle();
        bundle.putString(MarkPictureActivity.PHOTO_URL, filePath);
        bundle.putString(MarkPictureActivity.PHOTO_DIR, fileDir);
        bundle.putString(MarkPictureActivity.PHOTO_NAME, fileName);
        bundle.putFloat(MarkPictureActivity.URUT_FOTO, urutFoto);
        bundle.putBoolean(MarkPictureActivity.ACTION_REPLACE_DEFECT, isReplaceDefect);
        bundle.putBoolean(MarkPictureActivity.ACTION_REPLACE_DENAH, isReplaceDenah);
        bundle.putSerializable(MarkPictureActivity.PARENT_ITEM_SQII_PELAKSANAAN, parent);
        bundle.putSerializable(MarkPictureActivity.ITEM_SQII_PELAKSANAAN, item);
        bundle.putString(MarkPictureActivity.CALLING_ACTIVITY, ACTIVITY);

        intent.putExtra(MarkPictureActivity.PHOTO_BUNDLE, bundle);
        this.startActivity(intent);
        this.finish();
    }
}