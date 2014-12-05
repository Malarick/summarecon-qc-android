package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by arnold on 14/11/13.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public static final String LOG_TAG = "CameraPreview";

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private DisplayMetrics displayMetrics;
    private Camera.Parameters parameters;
    private Camera.Size size;
    private List supportedSizes;
    //private MediaActionSound mediaActionSound;

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
        //mediaActionSound = new MediaActionSound();
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
            if(parameters.getSupportedFocusModes().indexOf(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) != -1){
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);       //auto-focus
            }

            supportedSizes = parameters.getSupportedPictureSizes();
            if(supportedSizes == null){
                Log.e("CameraPreview", "Supported PictureSize not found");
                return;
            }else{
                int first = ((Camera.Size)supportedSizes.get(0)).width;
                int last = ((Camera.Size)supportedSizes.get(supportedSizes.size() - 1)).width;
                if(last > first){
                    //CHECK FOR SUPPORTED PICTURE SIZE ASCENDING
                    for(int i = 0; i < supportedSizes.size(); i++){
                        size = (Camera.Size)supportedSizes.get(i);
                        //ASPECT RATIO 1.7 (16:9) OR 1.3 (4:3)
                        if(size.width >= 1024 && size.height >= 720){
                            parameters.setPictureSize(size.width, size.height);
                            break;
                        }
                    }
                }else{
                    //CHECK FOR SUPPORTED PICTURE SIZE DESCENDING
                    for(int i = supportedSizes.size() - 1; i >= 0; i--){
                        size = (Camera.Size)supportedSizes.get(i);
                        //ASPECT RATIO 1.7 (16:9) OR 1.3 (4:3)
                        if(size.width >= 1024 && size.height >= 720){
                            parameters.setPictureSize(size.width, size.height);
                            break;
                        }
                    }
                }
                Log.e("CameraPreview", parameters.getPictureSize().width + "x" + parameters.getPictureSize().height);
            }
            parameters.setPictureFormat(ImageFormat.JPEG);

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
