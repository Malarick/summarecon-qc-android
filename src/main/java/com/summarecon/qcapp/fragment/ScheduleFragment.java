package com.summarecon.qcapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import com.summarecon.qcapp.R;


public class ScheduleFragment extends Fragment {
    CalendarView jadwal;
    EditText edt_keterangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        jadwal = (CalendarView) rootView.findViewById(R.id.cal_jadwal);
        edt_keterangan = (EditText) rootView.findViewById(R.id.edt_keterangan);

        jadwal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                edt_keterangan.append(String.valueOf(dayOfMonth)+"-"+String.valueOf(month)+"-"+String.valueOf(year));
            }
        });

        return rootView;
    }
}