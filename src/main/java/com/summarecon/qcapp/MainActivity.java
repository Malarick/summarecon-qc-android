package com.summarecon.qcapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(getApplicationContext(),"Tes Agung",Toast.LENGTH_SHORT);
        //Toast.makeText(getApplicationContext(),"T",Toast.LENGTH_SHORT);
    }
}
