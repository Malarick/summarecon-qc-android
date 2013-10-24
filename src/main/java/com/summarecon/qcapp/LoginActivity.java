package com.summarecon.qcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {
    CheckLoginData checklogin;
    String response;
    EditText edt_username,edt_password;
    Button btn_login;
    ImageView img_logo;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //** Check koneksi internet
        CheckWifiConnection check = new CheckWifiConnection();
        check.execute();
        //Log.i("tes","step 3");

        //**Deklarasi
        final float toX, toY;
        final LinearLayout layout_logo;

        //**initialisasi
        checklogin = new CheckLoginData();
        toX = 0;
        toY = -300;
        edt_username = (EditText) findViewById(R.id.edttxt_username);
        edt_password = (EditText) findViewById(R.id.edtTxt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        img_logo =(ImageView) findViewById(R.id.img_logo);

        //**animasi
        final Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final TranslateAnimation anim = new TranslateAnimation(0, toX, 0, toY);
        anim.setDuration(1500);

        //**inisialisasi Layout yg mau di gerakin (Logo, username, password)
        layout_logo = (LinearLayout) findViewById(R.id.logo);

        //**buat animasi listener untuk layout Logo(logo summarecon,username,password, button login)
        anim.setAnimationListener(new TranslateAnimation.AnimationListener()
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_logo.getLayoutParams();
            @Override
            public void onAnimationStart(Animation animation) {
                params.bottomMargin += toY*(-1);
                params.leftMargin += toX;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                //Toast.makeText(getApplicationContext(), "Animation Stopped",Toast.LENGTH_SHORT).show();
                animation.cancel();
                layout_logo.setLayoutParams(params);
                edt_username.startAnimation(fade_in);
                edt_password.startAnimation(fade_in);
                btn_login.startAnimation(fade_in);
            }
        }
        );

        //**set animasi listener fade-in untuk isian username dan password
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Toast.makeText(getApplicationContext(), "Animation FadeIn Stopped",Toast.LENGTH_SHORT).show();
                //** SetVisibility agar setelah animasi fadeIn tetap nampak di layar
                edt_username.setVisibility(View.VISIBLE);
                edt_password.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //**jalankan animasi layout logo bergerak ke atas
        layout_logo.startAnimation(anim);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //**Upload data login user untuk dicheck di server (VALID/INVALID)
                checklogin.execute();
            }
        });

    }


    //**class berisi fungsi upload data login user ke server untuk dicheck di server
    class CheckLoginData extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loading = new ProgressDialog(LoginActivity.this);
            loading.setMessage("Checking login data. Please wait...");
            loading.show();
            //btn_login.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... Void) {
            //Log.e("");
            //Contact c = contacts[0];
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://192.168.100.119/login/list.php");
            try {
                // Add Multipart Post Data
                MultipartEntity entity = new MultipartEntity();
                entity.addPart("username", new StringBody(edt_username.getText().toString()));
                entity.addPart("password", new StringBody(edt_password.getText().toString()));
                request.setEntity(entity);

                // Get response from server
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                Log.e("MainActivity", "Response Text : " + response);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void returnValue) {
            loading.dismiss();

            if (response.equals("VALID")){
                Toast.makeText(getApplicationContext(), "Login Sukses", Toast.LENGTH_SHORT).show();
                //panggil ulang task (buat refresh pengecekan biar bisa dipanggil ulang)
                checklogin = new CheckLoginData();
            }
            if (response.equals("INVALID")){
                Toast.makeText(getApplicationContext(),"Login Gagal",Toast.LENGTH_SHORT).show();
                //panggil ulang task (buat refresh pengecekan biar bisa dipanggil ulang)
                checklogin = new CheckLoginData();
            }

        }
    }

    //**Class dengan fungsi check koneksi internet
    class CheckWifiConnection extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;

        //GetDataFromServer data_server = new GetDataFromServer();
        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL("http://192.168.100.119/login/list.php").openConnection();
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
            //progressDialog.dismiss();
            Log.i("tes","step 2");
            super.onPostExecute(result);
            if (result == true){
                Toast.makeText(getApplicationContext(),"Koneksi ke server Berhasil!",Toast.LENGTH_SHORT).show();
                //data_server.execute();
            }else {
                Toast.makeText(getApplicationContext(),"Koneksi ke server Gagal!",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //progressDialog = ProgressDialog.show(HomeActivity.this, "","Loading...");
        }
    }

    //private class Animasi extends TranslateAnimation{
    //    private Animasi(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
    //        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
    //    }
    //}
}