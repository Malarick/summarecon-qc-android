package com.summarecon.qcapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.adapter.PenugasanExpListAdapter;
import com.summarecon.qcapp.adapter.SpinnerListAdapter;
import com.summarecon.qcapp.customview.CustomScrollView;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_CATATAN;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.item.SpinnerListItem;
import com.summarecon.qcapp.utils.BitmapUtil;

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
    public static final String CALLING_ACTIVITY = "CALLING_ACTIVITY";
    public static final String PHOTO_BUNDLE = "PHOTO_BUNDLE";
    public static final String PHOTO_URL = "PHOTO_URL";
    public static final String PHOTO_DIR = "PHOTO_DIR";
    public static final String PHOTO_NAME = "PHOTO_NAME";
    public static final String URUT_FOTO = "URUT_FOTO";
    public final static String ACTION_REPLACE_DEFECT = "ACTION_REPLACE_DEFECT";
    public static final String ACTION_REPLACE_DENAH = "ACTION_REPLACE_DENAH";
    public final static String PARENT_ITEM_SQII_PELAKSANAAN = "PARENT_ITEM_SQII_PELAKSANAAN";
    public final static String ITEM_SQII_PELAKSANAAN = "ITEM_SQII_PELAKSANAAN";

    private Intent intent;

    private ImageView photoPreview;
    private Bitmap photoBitmap;
    private Bitmap oriPhotoBitmap;
    private String photoURL;
    private String photoDir;
    private String photoName;
    private Float urutFoto;
    private Float jumlahFotoRealisasi;
    private Canvas canvas;
    private SpinnerListAdapter adapter;

    private EditText mNote;
    private TextView mSelectedViewStatusDefect;
    private TextView mSelectedViewStatusPekerjaan;
    private TextView mSelectedViewNote;
    private CustomScrollView rootScrollView;

    private Spinner mSpnStatusDefect;
    private Spinner mSpnStatusPekerjaan;
    private Spinner mSpnNote;

    private QCDBHelper db;

    private SQII_PELAKSANAAN parent;
    private SQII_PELAKSANAAN item;
    private String callingActivity;
    private Boolean isReplaceDefect;
    private Boolean isReplaceDenah;
    private Boolean actionListenerChecker;

    private String lastItemSelected;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_picture);

        db = QCDBHelper.getInstance(this);

        rootScrollView = (CustomScrollView) findViewById(R.id.root_scroll_view);
        mNote = (EditText) findViewById(R.id.edt_note);
        mSpnStatusDefect = (Spinner) findViewById(R.id.ddl_status_defect);
        mSpnStatusPekerjaan = (Spinner) findViewById(R.id.ddl_status_pekerjaan);
        mSpnNote = (Spinner) findViewById(R.id.ddl_note);
        actionListenerChecker = false;
        lastItemSelected = "";

        //get Intent
        intent = getIntent();

        //SHOW THE PHOTO'S PREVIEW
        photoPreview = (ImageView) findViewById(R.id.img_photo_preview);
        loadPhoto();

        //ON TOUCH LISTENER
        photoPreview.setOnTouchListener(new DrawingListener());

        //SPINNERS
        //POPULATE AWAL SPINNER
        List<SQII_CATATAN> notes = db.getAllCatatan(item.getKD_ITEM_DEFECT());
        populateSpinner(R.array.arr_lbl_status_defect, mSpnStatusDefect);
        if(item.getSTATUS_DEFECT().equals("B") && !isReplaceDenah){
            populateSpinner(R.array.arr_lbl_status_pekerjaan_berat, mSpnStatusPekerjaan);
        } else{
            populateSpinner(R.array.arr_lbl_status_pekerjaan, mSpnStatusPekerjaan);
        }
        populateSpinnerNote(notes, mSpnNote);

        initSpinners();
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
        Bundle bundle = intent.getBundleExtra(PHOTO_BUNDLE);
        photoURL = bundle.getString(PHOTO_URL);
        photoDir = bundle.getString(PHOTO_DIR);
        photoName = bundle.getString(PHOTO_NAME);
        urutFoto = bundle.getFloat(URUT_FOTO, 1);

        //photoBitmap = BitmapFactory.decodeFile(photoURL);
        photoBitmap = BitmapUtil.makeBitmapFromFile(photoURL, 1024 * 768);
        oriPhotoBitmap = photoBitmap;
        photoPreview.setImageBitmap(photoBitmap);

        parent = (SQII_PELAKSANAAN) bundle.getSerializable(PARENT_ITEM_SQII_PELAKSANAAN);
        item = (SQII_PELAKSANAAN) bundle.getSerializable(ITEM_SQII_PELAKSANAAN);

        jumlahFotoRealisasi = parent.getJML_FOTO_REALISASI();
        callingActivity = bundle.getString(CALLING_ACTIVITY);
        isReplaceDefect = bundle.getBoolean(ACTION_REPLACE_DEFECT);
        isReplaceDenah = bundle.getBoolean(ACTION_REPLACE_DENAH);
    }

    private void initSpinners(){
        //CEK SIAPA YANG MEMANGGIL INTENT
        //KALAU DARI GRID DIA BERSIFAT EDIT, ARTINYA SPINNER-NYA + NOTE-NYA DI SET
        //KALAU DARI PHOTO DIA BERSIFAT BARU, ARTINYA SEMUA DALAM KEADAAN DEFAULT
        if(callingActivity.equals(TakePictureActivity.ACTIVITY)){
            if(!isReplaceDefect){
                jumlahFotoRealisasi++;
            }
        } else if(callingActivity.equals(PenugasanExpListAdapter.ACTIVITY)){
            List<String> arr_status_defect = new ArrayList<String>();
            List<String> arr_status_pekerjaan = new ArrayList<String>();
            List<SQII_CATATAN> notes = db.getAllCatatan(item.getKD_ITEM_DEFECT());
            Collections.addAll(arr_status_defect, getResources().getStringArray(R.array.arr_lbl_status_defect));
            Collections.addAll(arr_status_pekerjaan, getResources().getStringArray(R.array.arr_lbl_status_pekerjaan));

            //SET FIELD-FIELD AGAR SESUAI DENGAN DB SEBELUM DI EDIT
            mSpnStatusDefect.setSelection(arr_status_defect.indexOf(item.getSTATUS_DEFECT()));
            mSpnStatusPekerjaan.setSelection(arr_status_pekerjaan.indexOf(item.getSTATUS_PEKERJAAN()));
            mSpnNote.setSelection(notes.indexOf(item.getCATATAN()));
            Log.e("EXTRA_", item.getSTATUS_DEFECT() + " || " + mSpnStatusDefect.getSelectedItemPosition() + " || " + arr_status_defect.indexOf(item.getSTATUS_DEFECT()));
            Log.e("EXTRA_", item.getSTATUS_PEKERJAAN() + " || " + mSpnStatusPekerjaan.getSelectedItemPosition() + " || " + arr_status_pekerjaan.indexOf(item.getSTATUS_PEKERJAAN()));
            Log.e("EXTRA_", item.getCATATAN() + " || " + mSpnNote.getSelectedItemPosition() + " || " + notes.indexOf(item.getCATATAN()));
            //mNote.setText(item.getCATATAN());
        }

        mSpnStatusDefect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = (TextView) view.findViewById(R.id.spinner_item_key);
                if(actionListenerChecker){
                    if(txt.getText().equals("B") && !lastItemSelected.equals("B")){
                        populateSpinner(R.array.arr_lbl_status_pekerjaan_berat, mSpnStatusPekerjaan);
                    }else{
                        if(lastItemSelected.equals("B")){
                            populateSpinner(R.array.arr_lbl_status_pekerjaan, mSpnStatusPekerjaan);
                        }
                    }
                }
                lastItemSelected = txt.getText().toString();
                actionListenerChecker = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean savePhoto(){
        //GET SELECTED ITEM FROM SPINNER/DROPDOWN
        mSelectedViewStatusDefect = (TextView) mSpnStatusDefect.getSelectedView().findViewById(R.id.spinner_item_key);
        mSelectedViewStatusPekerjaan = (TextView) mSpnStatusPekerjaan.getSelectedView().findViewById(R.id.spinner_item_key);
        mSelectedViewNote = (TextView) mSpnNote.getSelectedView().findViewById(R.id.spinner_item_key);

        //Convert bitmap to Byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        //File Output Stream
        //Proses write file
        //byte[] final_data = byteArrayOutputStream.toByteArray();
        //photoBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        photoBitmap.recycle();
        //Bitmap compressed = BitmapUtil.makeBitmap(byteArrayOutputStream.toByteArray(), 1024*768);
        //photoBitmap.recycle();
        //ByteArrayOutputStream byteArrayOutputStreamCompressed = new ByteArrayOutputStream();
        //compressed.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStreamCompressed);

        byte[] final_data = byteArrayOutputStream.toByteArray();

        File pictureFile = new File(photoURL);
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream = new FileOutputStream(pictureFile);
            fileOutputStream.write(final_data);
            fileOutputStream.flush();
            fileOutputStream.close();

            //UPDATE DB
            item.setPATH_FOTO_DEFECT(photoDir);
            item.setSRC_FOTO_DEFECT(photoName);
            item.setSTATUS_DEFECT(mSelectedViewStatusDefect.getText().toString());
            item.setSTATUS_PEKERJAAN(mSelectedViewStatusPekerjaan.getText().toString());
            item.setCATATAN(mSelectedViewNote.getText().toString());
            //item.setCATATAN(mNote.getText().toString());
            //parent.setJML_FOTO_REALISASI(jumlahFotoRealisasi);
            //Log.e("EXTRA_", item.getPATH_FOTO_DEFECT() + " || " + item.getSRC_FOTO_DEFECT() + " || " + item.getURUT_FOTO());
            //Log.e("EXTRA_", mSelectedViewStatusDefect.getText().toString() + " || " + mSelectedViewStatusPekerjaan.getText().toString());
            //db.updatePelaksanaan(item);
            //db.updateItemDefectPenugasan(parent);
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
        Bundle bundle = new Bundle();

        bundle.putBoolean(MarkFloorMapActivity.ACTION_REPLACE_DEFECT, isReplaceDefect);
        bundle.putBoolean(MarkFloorMapActivity.ACTION_REPLACE_DENAH, isReplaceDenah);
        bundle.putSerializable(MarkFloorMapActivity.PARENT_ITEM_SQII_PELAKSANAAN, parent);
        bundle.putSerializable(MarkFloorMapActivity.ITEM_SQII_PELAKSANAAN, item);

        intent.putExtra(PHOTO_BUNDLE, bundle);
        this.startActivity(intent);
        this.finish();
    }

    private void clearDrawing(){
        photoBitmap = oriPhotoBitmap;
        photoPreview.setImageBitmap(photoBitmap);
    }

    private void populateSpinnerNote(List<SQII_CATATAN> notes, Spinner spinner){
        List<SpinnerListItem> spinnerListItems = new ArrayList<SpinnerListItem>();

        if(notes.size() > 0){
            for(SQII_CATATAN value : notes){
                spinnerListItems.add(new SpinnerListItem(value.getDESKRIPSI()));
            }
        }else{
            spinnerListItems.add(new SpinnerListItem("None"));
        }

        adapter = new SpinnerListAdapter(this, R.layout.spinner_item, spinnerListItems);

        spinner.setAdapter(adapter);
    }

    private void populateSpinner(int res_arr, Spinner spinner){
        List<String> stringArrayList = new ArrayList<String>();
        List<SpinnerListItem> spinnerListItems = new ArrayList<SpinnerListItem>();

        Collections.addAll(stringArrayList, getResources().getStringArray(res_arr));
        for(String key : stringArrayList){
            String value = getString(getResources().getIdentifier(key, "string", this.getPackageName()));
            spinnerListItems.add(new SpinnerListItem(key, value));
        }

        adapter = new SpinnerListAdapter(this, R.layout.spinner_item, spinnerListItems);

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