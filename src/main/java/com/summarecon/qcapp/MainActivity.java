package com.summarecon.qcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.summarecon.qcapp.adapter.NavDrawerAdapter;
import com.summarecon.qcapp.fragment.AboutFragment;
import com.summarecon.qcapp.fragment.DashboardFragment;
import com.summarecon.qcapp.fragment.PenugasanFragment;
import com.summarecon.qcapp.fragment.ScheduleFragment;
import com.summarecon.qcapp.fragment.SettingFragment;
import com.summarecon.qcapp.item.NavDrawerItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private String username;
    private String password;
    private String response;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    //DrawerLayout
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerMain;
    private CustomActionBarDrawerToggle mActionBarDrawerToggle;
    private NavDrawerAdapter mNavDrawerAdapter;
    private ListView mListSectionDashboard;
    private ListView mListSectionPenugasan;
    private ListView mListSectionEtc;

    //Fragment
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Bundle fragmentArgs;

    private TextView lbl_user;
    private Bundle bundle = new Bundle();
    private GetDataFromServer getData = new GetDataFromServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SETUP THE ACTION BARS
        setupActionBar();
        mTitle = mDrawerTitle = getTitle();

        bundle = getIntent().getBundleExtra("bundle");
        if(bundle != null){
            username = bundle.getString("username");
            password = bundle.getString("password");
        }

        //INITIALIZING NAVIGATION DRAWER LAYOUT
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mDrawerMain = (LinearLayout) findViewById(R.id.drawer_main);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mActionBarDrawerToggle = new CustomActionBarDrawerToggle(
                this
                , mDrawerLayout
                , R.drawable.ic_drawer
                , R.string.desc_drawer_open
                , R.string.desc_drawer_close
        );
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //POPULATE MASING-MASING LISTVIEW
        mListSectionDashboard = (ListView) findViewById(R.id.list_section_dashboard);
        mListSectionPenugasan = (ListView) findViewById(R.id.list_section_penugasan);
        mListSectionEtc = (ListView) findViewById(R.id.list_section_etc);
        populateNavDrawerSection(R.array.arr_icon_section_dashboard, R.array.arr_lbl_section_dashboard, R.layout.drawer_item, mListSectionDashboard, null);
        populateNavDrawerSection(R.array.arr_icon_section_penugasan, R.array.arr_lbl_section_penugasan, R.layout.drawer_item, mListSectionPenugasan, getString(R.string.header_section_penugasan));
        populateNavDrawerSection(R.array.arr_icon_section_etc, R.array.arr_lbl_section_etc, R.layout.drawer_item, mListSectionEtc, getString(R.string.header_section_etc));

        //Default Main Menu Fragment (Dashboard)
        fragmentCall(new DashboardFragment());

        lbl_user = (TextView) findViewById(R.id.lbl_test);
        //DI COMMENT SEMENTARA
        //getData.execute();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //@Override
    //public void onBackPressed() {
    //    Toast.makeText(this, mFragment.toString(), Toast.LENGTH_LONG).show();
    //    if(mFragment != new DashboardFragment()){
    //        fragmentCall(new DashboardFragment());
    //    }else{
    //        this.finish();
    //    }
    //}

    public void populateNavDrawerSection(int arr_icon_res, int arr_lbl_res, int layout_res, ListView listView, String header) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> labelList = new ArrayList<String>();
        List<String> iconList = new ArrayList<String>();
        List<NavDrawerItem> itemList = new ArrayList<NavDrawerItem>();

        //Adding the header
        if(header != null){
            View header_view = inflater.inflate(R.layout.drawer_header, null);
            TextView lbl_header = (TextView) header_view.findViewById(R.id.lbl_header);
            lbl_header.setText(header);
            listView.addHeaderView(header_view, null, false);
        }

        Collections.addAll(iconList, getResources().getStringArray(arr_icon_res));
        Collections.addAll(labelList, getResources().getStringArray(arr_lbl_res));

        int c = 0;
        for(String s:labelList){
            //Assign icon kecuali pada label yang tidak memiliki icon alias "null"
            if(iconList.get(c) != "null"){
                int id_icon = getResources().getIdentifier(iconList.get(c), "drawable", this.getPackageName());
                itemList.add(new NavDrawerItem(id_icon, s));
            }else{
                itemList.add(new NavDrawerItem(s));
            }
            c++;
        }
        mNavDrawerAdapter = new NavDrawerAdapter(this, layout_res, itemList);

        listView.setAdapter(mNavDrawerAdapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
    }

    public void selectItem(AdapterView adapterView, View view, int position){
        CharSequence lblItem = ((TextView) view.findViewById(R.id.drawer_item_label)).getText();

        //Handle item selected
        if(adapterView.getId() == R.id.list_section_dashboard){
            mListSectionPenugasan.setItemChecked(-1, true);
            mListSectionEtc.setItemChecked(-1, true);
            fragmentCall(new DashboardFragment());

            setTitle(mDrawerTitle);
        }else{
            switch(adapterView.getId()){
                case R.id.list_section_penugasan:
                    mListSectionDashboard.setItemChecked(-1, true);
                    mListSectionEtc.setItemChecked(-1, true);
                    if(lblItem.equals("Schedule")){
                        fragmentCall(new ScheduleFragment());
                    }else{
                        fragmentCallPenugasan(new PenugasanFragment(), lblItem);
                    }
                    break;
                case R.id.list_section_etc:
                    mListSectionDashboard.setItemChecked(-1, true);
                    mListSectionPenugasan.setItemChecked(-1, true);
                    if(lblItem.equals("Settings")){
                        fragmentCall(new SettingFragment());
                    }else if(lblItem.equals("About")){
                        fragmentCall(new AboutFragment());
                    }else if(lblItem.equals("Sign Out")){
                        this.finish();
                    }else{
                        fragmentCall(new DashboardFragment());
                    }
                    break;
            }

            setTitle(lblItem);
        }
        mDrawerLayout.closeDrawer(mDrawerMain);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setTitle(CharSequence title){
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void fragmentCall(Fragment fragment){
        mFragment = fragment;
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void fragmentCallPenugasan(Fragment fragment, CharSequence jenisPenugasan){
        mFragment = fragment;
        fragmentArgs = new Bundle();

        fragmentArgs.putCharSequence(PenugasanFragment.ARGS_PENUGASAN, jenisPenugasan);
        mFragment.setArguments(fragmentArgs);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }

    class GetDataFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Request ke Server
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://192.168.100.127/login/list.php");
            //HttpResponse response = null;


            try {
                //response = client.execute(request);
                //text = EntityUtils.toString(response.getEntity());

                // Add Multipart Post Data
                MultipartEntity entity = new MultipartEntity();
                entity.addPart("username", new StringBody(username));
                entity.addPart("password", new StringBody(password));
                request.setEntity(entity);

                // Get response from server
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                // Parsing JSON
                //contact = new ArrayList<Contact>();
                JSONArray array = new JSONArray(response);
                Log.e("jason", "text: " + response);
                // for (int i=0; i<=array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(0);
                username = obj.getString("username");
                password = obj.getString("password");
/*
                        Contact c = new Contact();
                        c.setId(obj.getInt("id"));
                        c.setName(obj.getString("name"));
                        c.setDob(obj.getString("dob"));
                        c.setPhoneNumber(obj.getString("phone_number"));
                        c.setPhotoURL("http://192.168.100.143/contactapp-webapi/" + obj.getString("photo_url"));


                        URI uri = new URI(c.getPhotoURL().replace(" ", "%20"));
                        Bitmap photoBitmap = downloadBitmap(uri.toString());

                        // Convert Bitmap to Byte
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] final_data = stream.toByteArray();

                        // Save Image Byte to File
                        File photo_directory = new File(Environment.getExternalStorageDirectory() + "/DCIM/CameraAPI");

                        // Create Folder if not exist
                        if (!photo_directory.exists()) {
                            photo_directory.mkdirs();
                        }

                        String filename = System.currentTimeMillis() + ".jpg";
                        File picture_file = new File(photo_directory, filename);
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(picture_file);
                            fos.write(final_data);
                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        c.setPhotoURL(picture_file.getAbsolutePath());

                        // Debug untuk mendapatkan url photo
                            //String url = "http://192.168.100.113/contactapp-webapi/" + obj.getString("photo_url");
                            //Log.d("test_photo","photo_url : " + url);
                        contact.add(c);
*/
                // }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //catch (URISyntaxException e)
            //{
            //   e.printStackTrace();
            //}

            return null;
        }

/*
            private Bitmap downloadBitmap(String url) {
                Bitmap image = null;
                final DefaultHttpClient client = new DefaultHttpClient();
                final HttpGet getRequest = new HttpGet(url);
                try {
                    HttpResponse response = client.execute(getRequest);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                        return null;
                    }

                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = entity.getContent();
                            image = BitmapFactory.decodeStream(inputStream);
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        }
                    }
                } catch (Exception e) {
                    getRequest.abort();
                    Log.e("ImageDownloader", "Error : " + url + e.toString());
                }
                return image;
            }

            @Override
            public void onPostExecute(Void return_value){
                adapter = new  ContactListAdapter(MainActivity.this, R.layout.contact_list_item, contact);
                output.setAdapter(adapter);
            }
            */

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(getApplicationContext(), "username: " + username + " " + "Password: " + password, Toast.LENGTH_SHORT).show();
            lbl_user.setText("Welcome, "+username);
        }
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            getActionBar().setTitle(mDrawerTitle);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getActionBar().setTitle(mTitle);
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(adapterView, view, position);
        }
    }
}
