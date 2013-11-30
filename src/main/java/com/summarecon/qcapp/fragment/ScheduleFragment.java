package com.summarecon.qcapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.db.SQII_PELAKSANAAN;

import java.sql.Date;
import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    CalendarView jadwal;
    EditText edt_keterangan;

    QCDBHelper db;
    private ArrayList<SQII_PELAKSANAAN> pelaksanaan;
    private int jum_penugasan_baru,jum_penugasan_ulang,jum_penugasan_sisa;
    private String tanggal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        //Init the DB
        db = QCDBHelper.getInstance(getActivity());


        jadwal = (CalendarView) rootView.findViewById(R.id.cal_jadwal);
        edt_keterangan = (EditText) rootView.findViewById(R.id.edt_keterangan);



        jadwal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                jum_penugasan_baru = db.getAllPelaksanaanPenugasan(Date.valueOf(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth)),"201005469", QCConfig.KD_PENUGASAN_BARU).size();
                jum_penugasan_ulang = db.getAllPelaksanaanPenugasan(Date.valueOf(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth)),"201005469", QCConfig.KD_PENUGASAN_ULANG).size();
                jum_penugasan_sisa = db.getAllPelaksanaanPenugasan(Date.valueOf(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth)),"201005469", QCConfig.KD_PENUGASAN_SISA).size();

                edt_keterangan.setText("");
                edt_keterangan.append("parameter: " + String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth) + "\n");
                edt_keterangan.append("Date saat ini: "+String.valueOf(dayOfMonth) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year)+"\n");
                edt_keterangan.append("Jumlah Penugasan Baru : "+jum_penugasan_baru+"\n");
                edt_keterangan.append("Jumlah Penugasan Ulang : "+jum_penugasan_ulang+"\n");
                edt_keterangan.append("Jumlah Penugasan Sisa : "+jum_penugasan_sisa);
                //edt_keterangan.setText(String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year));
            }
        });

        return rootView;
    }
}