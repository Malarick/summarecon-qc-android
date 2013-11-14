package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by arnold on 14/11/13.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public static final String LOG_TAG = "CameraPreview";

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private DisplayMetrics displayMetrics;
    private Camera.Parameters parameters;
    private MediaActionSound mediaActionSound;

    public CameraPreview(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Get Display Size and contain it in displayMetrics
        displayMetrics = new DisplayMetrics();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        //Release the camera before opening if any other applications have been using it
        //releaseCamera();

        try{
            camera = Camera.open();
            //Parameters for camera function
            parameters = camera.getParameters();
        }catch (Exception e){
            e.printStackTrace();
        }

        //MediaActionSound
        mediaActionSound = new MediaActionSound();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if(camera == null){
                camera = Camera.open();
                parameters = camera.getParameters();
            }
            camera.setDisplayOrientation(90);       //set agar portrait
            camera.setPreviewDisplay(surfaceHolder);

            //setup camera parameters
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);                      //Flashlight
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);       //auto-focus
            parameters.setPictureSize(1024, 768);

            //apply camera parameters
            camera.setParameters(parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        try{
            camera.startPreview();
        }catch (Exception e){
            Log.d(LOG_TAG, "Error starting camera preview :" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    public Camera getCamera() {
        return camera;
    }

    public void releaseCamera(){
        if(camera != null){
            camera.cancelAutoFocus();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }
}
