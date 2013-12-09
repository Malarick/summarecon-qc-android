package com.summarecon.qcapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
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
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.fragment.AboutFragment;
import com.summarecon.qcapp.fragment.DashboardFragment;
import com.summarecon.qcapp.fragment.PenugasanFragment;
import com.summarecon.qcapp.fragment.ScheduleFragment;
import com.summarecon.qcapp.fragment.SettingFragment;
import com.summarecon.qcapp.item.NavDrawerItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";
    //Fragment
    public static Fragment mFragment;
    private String nik;
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
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Bundle fragmentArgs;
    private TextView lbl_user;
    private Bundle bundleLogin = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SETUP THE ACTION BARS
        setupActionBar();
        mTitle = mDrawerTitle = getTitle();

        bundleLogin = getIntent().getBundleExtra("bundleLogin");
        if (bundleLogin != null) {
            nik = bundleLogin.getString("nik");
            password = bundleLogin.getString("password");
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

        try {
            Field mDragger = mDrawerLayout.getClass().getDeclaredField("mLeftDragger"); //mRightDragger for right dragger
            mDragger.setAccessible(true);
            ViewDragHelper dragHelper = (ViewDragHelper) mDragger.get(mDrawerLayout);
            Field mEdgeSize = dragHelper.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(dragHelper);
            mEdgeSize.setInt(dragHelper, edge * 2); //may set any constant in DP
        } catch (NoSuchFieldException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

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
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mFragment.getClass() != DashboardFragment.class) {
            fragmentCall(new DashboardFragment());
        } else {
            this.finish();
        }
    }

    public void populateNavDrawerSection(int arr_icon_res, int arr_lbl_res, int layout_res, ListView listView, String header) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<String> labelList = new ArrayList<String>();
        List<String> iconList = new ArrayList<String>();
        List<NavDrawerItem> itemList = new ArrayList<NavDrawerItem>();

        //Adding the header
        if (header != null) {
            View header_view = inflater.inflate(R.layout.drawer_header, null);
            TextView lbl_header = (TextView) header_view.findViewById(R.id.lbl_header);
            lbl_header.setText(header);
            listView.addHeaderView(header_view, null, false);
        }

        Collections.addAll(iconList, getResources().getStringArray(arr_icon_res));
        Collections.addAll(labelList, getResources().getStringArray(arr_lbl_res));


        int c = 0;
        for (String s : labelList) {
            //Assign icon kecuali pada label yang tidak memiliki icon alias "null"
            if (iconList.get(c) != "null") {
                int id_icon = getResources().getIdentifier(iconList.get(c), "drawable", this.getPackageName());
                if (s.equals(QCConfig.JENIS_PENUGASAN_SISA)) {
                    itemList.add(new NavDrawerItem(id_icon, s, QCDBHelper.getInstance(this).getPelaksanaanJumlahFoto(nik, QCConfig.KD_PENUGASAN_SISA)));
                    Log.e("LUAR", s + "= " + itemList.get(c).counterExist.toString());
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_ULANG)) {
                    itemList.add(new NavDrawerItem(id_icon, s, QCDBHelper.getInstance(this).getPelaksanaanJumlahFoto(nik, QCConfig.KD_PENUGASAN_ULANG)));
                    Log.e("LUAR", s + "= " + itemList.get(c).counterExist.toString());
                } else if (s.equals(QCConfig.JENIS_PENUGASAN_BARU)) {
                    itemList.add(new NavDrawerItem(id_icon, s, QCDBHelper.getInstance(this).getPelaksanaanJumlahFoto(nik, QCConfig.KD_PENUGASAN_BARU)));
                    Log.e("LUAR", s + "= " + itemList.get(c).counterExist.toString());
                } else {
                    itemList.add(new NavDrawerItem(id_icon, s));
                    Log.e("LUAR", s + "= " + itemList.get(c).counterExist.toString());
                }
            } else {
                itemList.add(new NavDrawerItem(s));
                Log.e("LUAR", s + "= " + itemList.get(c).counterExist.toString());
            }
            c++;
        }
        mNavDrawerAdapter = new NavDrawerAdapter(this, layout_res, itemList);

        listView.setAdapter(mNavDrawerAdapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
    }

    public void selectItem(AdapterView adapterView, View view, int position) {
        CharSequence lblItem = ((TextView) view.findViewById(R.id.drawer_item_label)).getText();

        //Handle item selected
        if (adapterView.getId() == R.id.list_section_dashboard) {
            mListSectionPenugasan.setItemChecked(-1, true);
            mListSectionEtc.setItemChecked(-1, true);
            fragmentCall(new DashboardFragment());

            setTitle(mDrawerTitle);
        } else {
            switch (adapterView.getId()) {
                case R.id.list_section_penugasan:
                    mListSectionDashboard.setItemChecked(-1, true);
                    mListSectionEtc.setItemChecked(-1, true);
                    if (lblItem.equals("Schedule")) {
                        fragmentCall(new ScheduleFragment());
                    } else {
                        String jenisPenugasan = null;
                        if(lblItem.equals(QCConfig.JENIS_PENUGASAN_SISA)){
                            jenisPenugasan = QCConfig.KD_PENUGASAN_SISA;
                        } else if(lblItem.equals(QCConfig.JENIS_PENUGASAN_ULANG)){
                            jenisPenugasan = QCConfig.KD_PENUGASAN_ULANG;
                        } else if(lblItem.equals(QCConfig.JENIS_PENUGASAN_BARU)){
                            jenisPenugasan = QCConfig.KD_PENUGASAN_BARU;
                        }
                        fragmentCallPenugasan(new PenugasanFragment(), jenisPenugasan);
                    }
                    break;
                case R.id.list_section_etc:
                    mListSectionDashboard.setItemChecked(-1, true);
                    mListSectionPenugasan.setItemChecked(-1, true);
                    if (lblItem.equals("Settings")) {
                        fragmentCall(new SettingFragment());
                    } else if (lblItem.equals("About")) {
                        fragmentCall(new AboutFragment());
                    } else if (lblItem.equals("Sign Out")) {
                        this.finish();
                    } else {
                        fragmentCall(new DashboardFragment());
                    }
                    break;
            }

            setTitle(lblItem);
        }
        mDrawerLayout.closeDrawer(mDrawerMain);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    public void fragmentCall(Fragment fragment) {
        mFragment = fragment;
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }


    public void fragmentCallPenugasan(Fragment fragment, String jenisPenugasan) {
        mFragment = fragment;
        fragmentArgs = new Bundle();

        fragmentArgs.putString(PenugasanFragment.ARGS_PENUGASAN, jenisPenugasan);
        mFragment.setArguments(fragmentArgs);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().replace(R.id.content_main, mFragment);
        mFragmentTransaction.commit();
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {
        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            populateNavDrawerSection(R.array.arr_icon_section_penugasan, R.array.arr_lbl_section_penugasan, R.layout.drawer_item, mListSectionPenugasan, null);
            super.onDrawerOpened(drawerView);
            getActionBar().setTitle(mDrawerTitle);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getActionBar().setTitle(mTitle);
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(adapterView, view, position);
        }
    }
}
