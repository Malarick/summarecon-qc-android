package com.summarecon.qcapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.utils.MD5Hash;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LoginActivity extends Activity {

    private static String LOG_TAG = "LoginActivity";
    private String response;
    private EditText edt_nik, edt_password;
    private Button btn_login;
    private ImageView img_logo;
    private String server_ip;
    private LinearLayout layout_user_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        server_ip = QCConfig.getSharedPreferences().getString("edittext_preference", "192.168.100.106");

        //**inisialisasi EditText, Button, ImageView, Layout yg mau di gerakin (Logo, username, password)
        edt_nik = (EditText) findViewById(R.id.edt_nik);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        img_logo = (ImageView) findViewById(R.id.img_logo);
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
                if (validasiFormLogin()) login();
            }
        });

    }

    private boolean validasiFormLogin() {
        String nik = edt_nik.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        if (nik.length() > 0 && password.length() > 0) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Isi Nik dan Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void login() {
        /* Check isi tabel user/penugasan */
        if (QCDBHelper.getInstance(this).checkTabelPenugasan()) {
            /* Kalau ada ada datanya check login dari database lokal */
            Log.e(LOG_TAG, "data penugasan ada di database...");
            checkUserLogin();
        } else if (!checkFileSQLPenugasan()) {
             /* Download Penugasan dari Server */
            Log.e(LOG_TAG, "download file penugasan...");
            new DownloadDataPenugasan().execute();
        } else {
            QCDBHelper.getInstance(this).executeSQLScriptFile();
            checkUserLogin();
        }
    }

    private boolean checkFileSQLPenugasan() {
        File file = new File(QCConfig.APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY);
        if (file.exists()) {
            Log.e(LOG_TAG, "ada file penugasan...");
            return true;
        } else {
            Log.e(LOG_TAG, "tidak ada file penugasan...");
            return false;
        }
    }

    private void checkUserLogin() {
        String nik = edt_nik.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        if (QCDBHelper.getInstance(this).checkLogin(nik, MD5Hash.getMD5(password))) {
            Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                /* Init intent*/
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                /* init bundle dan bawa nilai username dan password ke aktivity lain*/
            Bundle bundle = new Bundle();
            bundle.putString("nik", edt_nik.getText().toString());
            bundle.putString("password", MD5Hash.getMD5(edt_password.getText().toString()));
            intent.putExtra("bundle", bundle);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    class DownloadDataPenugasan extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loading = new ProgressDialog(LoginActivity.this);
            loading.setMessage("Checking login data. Please wait...");
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    DownloadDataPenugasan.this.cancel(true);
                }
            });
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... Void) {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://" + server_ip + "/sqii/ext-lib/agung_qc/get-penugasan.php");
            try {
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                // Write response to script file
                PrintStream out = null;
                try {
                    out = new PrintStream(new FileOutputStream(QCConfig.APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY));
                    out.print(response);
                } finally {
                    if (out != null) out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void returnValue) {
            QCDBHelper.getInstance(LoginActivity.this).executeSQLScriptFile();
            loading.dismiss();
            checkUserLogin();
        }
    }
}