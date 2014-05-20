package com.summarecon.qcapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
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
import com.summarecon.qcapp.utils.BitmapUtil;

public class PrevPicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevpic);

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
            photoURL = QCConfig.APP_EXTERNAL_IMAGES_DIRECTORY + "/DFC_VERNONIA_RESIDENCE_1400583562915.jpg";
            photoBitmap = BitmapUtil.makeBitmapFromFile(photoURL, 1024 * 768);
            mPhoto_preview.setImageBitmap(photoBitmap);
        }

    }

}
