package com.summarecon.qcapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;
import com.summarecon.qcapp.utils.BitmapUtil;

import java.util.List;

public class PrevPicActivity extends Activity {

    public final static String PARENT_ITEM_SQII_PELAKSANAAN = "PARENT_ITEM_SQII_PELAKSANAAN";
    public final static String ITEM_SQII_PELAKSANAAN = "ITEM_SQII_PELAKSANAAN";
    public static final String PHOTO_BUNDLE = "PHOTO_BUNDLE";

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevpic);

        intent = getIntent();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prev_pic, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ImageView mPhoto_preview;
        private Bitmap photoBitmap;
        private SQII_PELAKSANAAN itemPelaksanaan;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_prevpic, container, false);

            mPhoto_preview = (ImageView) rootView.findViewById(R.id.img_prev_photo_preview);
            loadPhoto();

            return rootView;
        }

        public void loadPhoto(){
            String photoURL;

            Bundle bundle = new Bundle();
            bundle = getActivity().getIntent().getBundleExtra(PHOTO_BUNDLE);
            itemPelaksanaan = (SQII_PELAKSANAAN) bundle.getSerializable(ITEM_SQII_PELAKSANAAN);


            photoURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + "/" + itemPelaksanaan.getSRC_FOTO_DEFECT_LAMA();
            Log.e("MAIN", photoURL);
            photoBitmap = BitmapUtil.makeBitmapFromFile(photoURL, 1024 * 768);
            mPhoto_preview.setImageBitmap(photoBitmap);
        }

    }

}
