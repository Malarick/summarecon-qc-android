package com.summarecon.qcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_USER;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginActivity extends Activity {
    //**Deklarasi variable Global
    String response;
    EditText edt_nik,edt_password;
    Button btn_login;
    ImageView img_logo,img_sync;
    String server_ip;
    LinearLayout layout_user_input;

    private static String FILE_DIR = "SummareconQC";
    private static String DATABASE_NAME = "summarecon.txt";

    ArrayList<SQII_USER> data_user;

    //init checkwifi
    CheckWifiConnection CheckWifi;

    //inisialisasi variable bertipe database
    QCDBHelper db;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Check ada setting/tidak ( kalau ada load dan pakai nilai setting tersebut ) */
        CheckSetting();

        /* inisialisasi  QCDHelper*/
        db = new QCDBHelper(this); //Buat database kosong klo belum ada/ pertama jalanin applikasi

        /*initialisasi check via wifi*/
        //checkloginwifi = new CheckLoginDataViaWifi();

        //**inisialisasi EditText, Button, ImageView, Layout yg mau di gerakin (Logo, username, password)
        edt_nik = (EditText) findViewById(R.id.edt_nik);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        img_logo =(ImageView) findViewById(R.id.img_logo);
        img_sync =(ImageView) findViewById(R.id.img_setting);
        layout_user_input = (LinearLayout) findViewById(R.id.user_input_layout);

        //**inisialisasi animasi yg akan digunakan
        final Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation translation = AnimationUtils.loadAnimation(this, R.anim.translation);

        //**jalankan animasi
        img_logo.startAnimation(translation);
        layout_user_input.startAnimation(fade_in);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* check isi dari database lokal*/
                CheckIsiDatabaseLocal();

                /*login bypass loncat ke main menu*/
                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
                //LoginActivity.this.finish();

            }
        });

        img_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!db.checkisidatabase()){
                    Toast.makeText(getApplicationContext(),"Syncronize Data...",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),edt_nik.getText().toString(),Toast.LENGTH_SHORT).show();
                    db.insertSQLBatch();
                }else
                    {
                        Toast.makeText(getApplicationContext(),"Data telah di Syncronize",Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    //**class berisi fungsi download database user dan penugasan dari server untuk diinsert ke db lokal
    class DownloadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loading = new ProgressDialog(LoginActivity.this);
            loading.setMessage("Checking login data. Please wait...");
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... Void) {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://"+server_ip+"/sqii/ext-lib/agung_qc/get-penugasan.php");
            try {
                // Add Multipart Post Data
                //MultipartEntity entity = new MultipartEntity();
                //entity.addPart("username", new StringBody(edt_username.getText().toString()));
                //entity.addPart("password", new StringBody(edt_password.getText().toString()));
                //request.setEntity(entity);

                // Get response from server
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                Log.e("MainActivity", "Response Text : " + response);


            } catch (IOException e) {
                e.printStackTrace();
                loading.dismiss(); //buang login kalau error
            } catch (Exception e) {
                e.printStackTrace();
                loading.dismiss(); //buang login kalau error
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void returnValue) {
            db.insertSQLBatchWifi(response);
            loading.dismiss();

             /* Check Login lwt database lokal */
            CheckUserLogin();
        }
    }
    //**Class dengan fungsi check koneksi internet
    class CheckWifiConnection extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL("http://"+server_ip+"/sqii/ext-lib/agung_qc/get-penugasan.php").openConnection();
                con.setRequestMethod("HEAD");

                con.setConnectTimeout(5000); //set timeout to 5 seconds
                Log.i("tes","step 1");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (java.net.SocketTimeoutException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == true){
                Toast.makeText(getApplicationContext(),"Koneksi ke server Berhasil!",Toast.LENGTH_SHORT).show();
                /* panggil fungsi downloaddata*/
                    DownloadData ddata = new DownloadData();
                    ddata.execute();
            }else if (result == false){
                Toast.makeText(getApplicationContext(),"Koneksi ke server Gagal!",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }


   /* Fungsi Check variabel Setting*/
   public void CheckSetting(){
       SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       if (mySharedPreferences.getString("edittext_preference", "") != ""){
           server_ip = mySharedPreferences.getString("edittext_preference", "");
       }else
       {
           server_ip="172.19.17.19";
       }
       //Toast.makeText(getApplicationContext(),"IP Server : "+server_ip,Toast.LENGTH_SHORT).show();
   }

    /* Fungsi Check database local*/
    public void CheckIsiDatabaseLocal(){
        /* Check isi database lokal (ada isi/engga)*/
        if (db.checkisidatabase()){
            /* Kalau ada ada datanya check login dari database lokal */
            CheckUserLogin();
        }
        else if (!CheckFileLokal())
         {
             /* Initialisasi CheckWifiConnection*/
             CheckWifi = new CheckWifiConnection();
            /* Jalankan Check koneksi internet untuk download data*/
             CheckWifi.execute();
         }else
            {
                db.insertSQLBatch();
                CheckUserLogin();
            }


    }

    /* Check apa ada file Summarecon.txt di scdard*/
    public boolean CheckFileLokal(){
        String file_path = Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator;

        File file = new File(file_path,"summarecon.txt");

        if(file.exists()){
            Toast.makeText(getApplicationContext(),"ada file summarecon.txt di sdcard",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Baca data dari sdcard",Toast.LENGTH_SHORT).show();

            return true;
        }else
            {
                Toast.makeText(getApplicationContext(),"tidak ada file summarecon.txt di sdcard",Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    public void CheckUserLogin(){
        if ((!String.valueOf(edt_nik.getText()).equals("")) && (!String.valueOf(edt_password.getText()).equals(""))){
            if (db.checkuserlogin(String.valueOf(edt_nik.getText()),md5(String.valueOf(edt_password.getText()))))
            {
                Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_SHORT).show();

                /* Init intent*/
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                /* init bundle dan bawa nilai username dan password ke aktivity lain*/
                Bundle bundle = new Bundle();
                bundle.putString("nik",edt_nik.getText().toString());
                bundle.putString("password",edt_password.getText().toString());
                intent.putExtra("bundle",bundle);

                startActivity(intent);

                LoginActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(),"Login Gagal",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Isi Nik dan Password",Toast.LENGTH_SHORT).show();
        }
    }

    /* Generate MD5 */
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}