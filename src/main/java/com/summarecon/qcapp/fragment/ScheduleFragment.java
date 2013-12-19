package com.summarecon.qcapp.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.summarecon.qcapp.R;
import com.summarecon.qcapp.adapter.CalendarAdapter;
import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;
import com.summarecon.qcapp.utils.CalendarUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;


public class ScheduleFragment extends Fragment {
    public GregorianCalendar month, itemmonth;// fragment_schedule instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot marker.
    public ArrayList<String> items; // container to store fragment_schedule items which needs showing the event marker

    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    View rootView;
    GridView gridview;
    TextView title;
    RelativeLayout previous,next;
    Bundle bundleLogin;
    String nik,password;
    public String[] day = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

    QCDBHelper db;
    public int jum_penugasan_baru,jum_penugasan_ulang,jum_penugasan_sisa;
    CalendarUtility calut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        /* Init the DB */
        db = QCDBHelper.getInstance(getActivity());
        calut= new CalendarUtility();

        bundleLogin = getActivity().getIntent().getBundleExtra("bundleLogin");
        if (bundleLogin != null) {
            nik = bundleLogin.getString("nik");
            password = bundleLogin.getString("password");
        }

        rLayout = (LinearLayout) rootView.findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(getActivity(), month);

        gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
/*
        Toast.makeText(getActivity().getApplicationContext(),String.valueOf(gridview.getItemAtPosition(0)),Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity().getApplicationContext(),"Bulan : "+String.valueOf(android.text.format.DateFormat.format("MM", month)),Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(),"Tahun : "+String.valueOf(android.text.format.DateFormat.format("yyyy", month)),Toast.LENGTH_LONG).show();

        Toast.makeText(getActivity().getApplicationContext(),"Penugasan Baru : "+jum_penugasan_baru,Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(),"Penugasan Ulang : "+jum_penugasan_ulang,Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(),"Penugasan Sisa : "+jum_penugasan_sisa,Toast.LENGTH_LONG).show();
*/

        /* Saat pertama kali schedule dijalankan dilakukan pengecekan tanggal yg ada penugasan nya. */
        for (int i=0;i<31;i++){

            jum_penugasan_baru=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_BARU).size();
            jum_penugasan_ulang=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_ULANG).size();
            jum_penugasan_sisa=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_SISA).size();

            if ((jum_penugasan_baru>0) || (jum_penugasan_ulang>0) || (jum_penugasan_sisa>0))
            {
                calut.setEvent(String.valueOf(android.text.format.DateFormat.format("yyyy", month)+"-"+android.text.format.DateFormat.format("MM", month)+"-"+day[i]+"\n"+"Penugasan Sisa: "+jum_penugasan_sisa+"\n"+"Penugasan Ulang: "+jum_penugasan_ulang+"\n"+"Pengasan Baru: "+jum_penugasan_baru));
                calut.setYear(String.valueOf(android.text.format.DateFormat.format("yyyy", month)));
                calut.setMon(String.valueOf(android.text.format.DateFormat.format("MM", month)));
                calut.setDay(day[i]);
                //descriptions.add("xxx");
                //Log.e("tanggal penugasan : ",String.valueOf(year+"-"+mon+"-"+day[i]));
            }

        }

        handler = new Handler();
        handler.post(calendarUpdater);

        title = (TextView) rootView.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        previous = (RelativeLayout) rootView.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
                /* Saat tekan next month check ulang dalam bulan tersebut apakah ada penugasan */
                for (int i=0;i<31;i++){

                    jum_penugasan_baru=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_BARU).size();
                    jum_penugasan_ulang=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_ULANG).size();
                    jum_penugasan_sisa=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_SISA).size();

                    if ((jum_penugasan_baru>0) || (jum_penugasan_ulang>0) || (jum_penugasan_sisa>0))
                    {
                        calut.setEvent(String.valueOf(android.text.format.DateFormat.format("yyyy", month)+"-"+android.text.format.DateFormat.format("MM", month)+"-"+day[i]+"\n"+"Penugasan Sisa: "+jum_penugasan_sisa+"\n"+"Penugasan Ulang: "+jum_penugasan_ulang+"\n"+"Pengasan Baru: "+jum_penugasan_baru));
                        calut.setYear(String.valueOf(android.text.format.DateFormat.format("yyyy", month)));
                        calut.setMon(String.valueOf(android.text.format.DateFormat.format("MM", month)));
                        calut.setDay(day[i]);
                        //descriptions.add("xxx");
                        //Log.e("tanggal penugasan : ",String.valueOf(year+"-"+mon+"-"+day[i]));
                    }

                }

            }
        });

        next = (RelativeLayout) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
                /* Saat tekan next month check ulang dalam bulan tersebut apakah ada penugasan */
                for (int i=0;i<31;i++){

                    jum_penugasan_baru=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_BARU).size();
                    jum_penugasan_ulang=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_ULANG).size();
                    jum_penugasan_sisa=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(String.valueOf(android.text.format.DateFormat.format("yyyy", month))+"-"+String.valueOf(android.text.format.DateFormat.format("MM", month))+"-"+String.valueOf(day[i])),nik, QCConfig.KD_PENUGASAN_SISA).size();

                    if ((jum_penugasan_baru>0) || (jum_penugasan_ulang>0) || (jum_penugasan_sisa>0))
                    {
                        calut.setEvent(String.valueOf(android.text.format.DateFormat.format("yyyy", month)+"-"+android.text.format.DateFormat.format("MM", month)+"-"+day[i]+"\n"+"Penugasan Sisa: "+jum_penugasan_sisa+"\n"+"Penugasan Ulang: "+jum_penugasan_ulang+"\n"+"Pengasan Baru: "+jum_penugasan_baru));
                        calut.setYear(String.valueOf(android.text.format.DateFormat.format("yyyy", month)));
                        calut.setMon(String.valueOf(android.text.format.DateFormat.format("MM", month)));
                        calut.setDay(day[i]);
                        //descriptions.add("xxx");
                        //Log.e("tanggal penugasan : ",String.valueOf(year+"-"+mon+"-"+day[i]));
                    }

                }

            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /* removing the previous view if added */
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                /* navigate to next or previous month on clicking offdays.*/
                //Toast.makeText(getActivity().getApplicationContext(), selectedGridDate, Toast.LENGTH_SHORT).show();
                jum_penugasan_baru = db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(selectedGridDate),nik, QCConfig.KD_PENUGASAN_BARU).size();
                jum_penugasan_ulang = db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(selectedGridDate),nik, QCConfig.KD_PENUGASAN_ULANG).size();
                jum_penugasan_sisa = db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(selectedGridDate),nik, QCConfig.KD_PENUGASAN_SISA).size();

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                    if (CalendarUtility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(CalendarUtility.nameOfEvent.get(i));
                    }
                }
/*
                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(getActivity().getApplicationContext());

                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }
*/
                /* Coba nampilin penugasan*/
                TextView rowTextView = new TextView(getActivity().getApplicationContext());

                // set some properties of rowTextView or something
                rowTextView.setText("Event : "+selectedGridDate+"\n Penugasan Baru : "+ String.valueOf(jum_penugasan_baru)+" \n Penugasan Ulang : "+String.valueOf(jum_penugasan_ulang)+"\n Penugasan Sisa : "+String.valueOf(jum_penugasan_sisa));
                rowTextView.setTextColor(Color.BLACK);

                // add the textview to the linearlayout
                rLayout.addView(rowTextView);

                desc = null;

            }

        });

        return rootView;
    }

    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    protected void showToast(String string) {
        Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
        TextView title = (TextView) rootView.findViewById(R.id.title);

        adapter.refreshDays();

        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some fragment_schedule items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = CalendarUtility.readCalendarEvent(getActivity().getApplicationContext());
            Log.d("=====Event====", event.toString());
            Log.d("=====Date ARRAY====", CalendarUtility.startDates.toString());

            for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(CalendarUtility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

}