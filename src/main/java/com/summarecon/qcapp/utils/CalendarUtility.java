package com.summarecon.qcapp.utils;

import android.content.Context;
import android.util.Log;

import com.summarecon.qcapp.core.QCConfig;
import com.summarecon.qcapp.db.QCDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtility {
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static int jum_penugasan_baru,jum_penugasan_ulang,jum_penugasan_sisa;
    public static String[] day = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    public static String mon;
    public static String year;
    public static String nik;
    public static String eventname = "penugasan";

    public void setMon(String m) {
        this.mon = m;
    }

    public void setYear(String y) {
        this.year = y;
    }

    public void setNik(String n) {
        this.nik = n;
    }

    public String getMon() {
        return mon;
    }

    public String getYear() {
        return year;
    }
    public String getNik() {
        return nik;
    }

    public static ArrayList<String> readCalendarEvent(Context context) {


        //Init the DB
        QCDBHelper db = QCDBHelper.getInstance(context);
        //if (db.getAllPelaksanaan() = null)
        GregorianCalendar month = (GregorianCalendar) GregorianCalendar.getInstance();


        /*
        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://com.android.fragment_schedule/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];
        */
        // fetching calendars id
        //nameOfEvent.clear();
        //startDates.clear();
        //endDates.clear();
        //descriptions.clear();

        clearvalue();
/*
		for (int i = 0; i < CNames.length; i++) {

			nameOfEvent.add(cursor.getString(1));
			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
			endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			descriptions.add(cursor.getString(2));
			CNames[i] = cursor.getString(1);
			cursor.moveToNext();
		}
*/
        //nameOfEvent.add("2013-12-11"+"\n"+"Penugasan Sisa: 5"+"\n"+"Penugasan Ulang: 3"+"\n"+"Pengasan Baru: 10");
        //startDates.add("2013-12-11");
        //endDates.add("2013-12-11");
        //descriptions.add("penugasan 10 Desember 2013");


        for (int i=0;i<31;i++){

            jum_penugasan_baru=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(year+"-"+mon+"-"+day[i]),nik, QCConfig.KD_PENUGASAN_BARU).size();
            jum_penugasan_ulang=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(year+"-"+mon+"-"+day[i]),nik, QCConfig.KD_PENUGASAN_ULANG).size();
            jum_penugasan_sisa=db.getAllPelaksanaanPenugasan(java.sql.Date.valueOf(year+"-"+mon+"-"+day[i]),nik, QCConfig.KD_PENUGASAN_SISA).size();

            if ((jum_penugasan_baru>0) || (jum_penugasan_ulang>0) || (jum_penugasan_sisa>0))
            {
                nameOfEvent.add(String.valueOf(year+"-"+mon+"-"+day[i]+"\n"+"Penugasan Sisa: "+jum_penugasan_sisa+"\n"+"Penugasan Ulang: "+jum_penugasan_ulang+"\n"+"Pengasan Baru: "+jum_penugasan_baru));
                startDates.add(String.valueOf(year+"-"+mon+"-"+day[i]));
                endDates.add(String.valueOf(year+"-"+mon+"-"+day[i]));
                descriptions.add("xxx");
                Log.e("tanggal penugasan : ",String.valueOf(year+"-"+mon+"-"+day[i]));
            }

        }

        if (nameOfEvent != null){
            Log.e("nama event : ",nameOfEvent+" , start_date:"+startDates+" , end_Date:"+endDates+" , deskripsi:"+descriptions);
        }

        return nameOfEvent;
    }

    public static void clearvalue(){
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
    }
/*
    public static void setmark(String namaevent,String startdate, String enddate, String deskripsi){
        nameOfEvent.add(namaevent);
        startDates.add(startdate);
        endDates.add(enddate);
        descriptions.add(deskripsi);
    }
*/
    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
