package com.summarecon.qcapp.db;

/*Created By Wahyu Wibisana on 10/19/2013*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 10/19/13.
 */
public class QCDBHelper extends SQLiteOpenHelper {

    private static String FILE_DIR = "SummareconQC";
    private static String DATABASE_NAME = "penugasan.db";
    private static int DB_VERSION = 1;

    public QCDBHelper(final Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + DATABASE_NAME, null, DB_VERSION);
        Log.e("Test", Environment.getExternalStorageDirectory().toString());

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);

        onCreate(db);
        /*Log.e("Test","Masuk Constructor");*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Table SQII_PENUGASAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_PENUGASAN (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "TGL_PENUGASAN DATETIME, " +
                "KD_KAWASAN CHAR(3), " +
                "PETUGAS_QC VARCHAR(50), " +
                "DESKRIPSI VARCHAR(255), " +
                "FLAG_AKTIF CHAR(1), " +
                "FLAG_BATAL CHAR(1), " +
                "USER_BATAL VARCHAR(50), " +
                "TGL_BATAL DATETIME, " +
                "ALASAN_BATAL VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_PENUGASAN))"
        );
        db.close();

        /*Table SQII_PENUGASAN_DETIL*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_PENUGASAN_DETIL (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "PENGAWAS VARCHAR(50), " +
                "SM VARCHAR(50), " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR))"
        );
        db.close();

        /*Table SQII_ITEM_DEFECT_PENUGASAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_ITEM_DEFECT_PENUGASAN (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "KD_LANTAI DECIMAL(18,0), " +
                "JML_FOTO_PENUGASAN DECIMAL(18,0), " +
                "FLAG_AKTIF CHAR(1), " +
                "FLAG_BATAL CHAR(1), " +
                "USER_BATAL VARCHAR(50), " +
                "TGL_BATAL DATETIME, " +
                "ALASAN_BATAL VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI))"
        );
        db.close();

        /*Table SQII_PELAKSANAAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_PELAKSANAAN (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "KD_LANTAI DECIMAL(18,0), " +
                "URUT_PELAKSANAAN DECIMAL(18,0), " +
                "TGL_PELAKSANAAN DATETIME, " +
                "PETUGAS_QC VARCHARF(50), " +
                "PENGAWAS VARCHAR(50), " +
                "SM VARCHAR(50), " +
                "STATUS_DEFECT CHAR(1), " +
                "STATUS_PEKERJAAN CHAR(1), " +
                "CATATAN VARCHAR(255), " +
                "FLAG_UPLOAD CHAR(1), " +
                "TGL_UPLOAD DATETIME, " +
                "SRC_FOTO_DENAH VARCHAR(255), " +
                "SRC_FOTO_DEFECT VARCHAR(255), " +
                "LAMA_PERBAIKAN DECIMAL(18,0), " +
                "TGL_ENTRY_LAMA_PERBAIKAN DATETIME, " +
                "TGL_JATUH_TEMPO_PERBAIKAN DATETIME, " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_AKTIF VARCHAR(50), " +
                "TGL_AKTIF DATETIME, " +
                "ALASAN_AKTIF VARCHAR(255), " +
                "FLAG_BATAL CHAR(1), " +
                "USER_BATAL VARCHAR(50), " +
                "TGL_BATAL DATETIME, " +
                "ALASAN_BATAL VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "ROWID DECIMAL(18,0), " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI, URUT_PELAKSANAAN))"
        );
        db.close();

        /*Table SQII_HISTORY_UPLOAD*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_HISTORY_UPLOAD (" +
                "ROWID DECIMAL(18,0), " +
                "NO_PENUGASAN VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "KD_LANTAI DECIMAL(18,0), " +
                "URUT_PELAKSANAAN DECIMAL(18,0), " +
                "TGL_UPLOAD DATETIME, " +
                "CATATAN VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (ROWID))"
        );
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /*LIST FUNCTION TABLE SQII_PENUGASAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_PENUGASAN> getAllPenugasan() {
        List<SQII_PENUGASAN> listData = new ArrayList<SQII_PENUGASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PENUGASAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PENUGASAN item = new SQII_PENUGASAN();
                item.setNO_PENUGASAN(cursor.getString(0));
                item.setTGL_PENUGASAN(cursor.getString(1));
                item.setKD_KAWASAN(cursor.getString(2));
                item.setPETUGAS_QC(cursor.getString(3));
                item.setDESKRIPSI(cursor.getString(4));
                item.setFLAG_AKTIF(cursor.getString(5));
                item.setFLAG_BATAL(cursor.getString(6));
                item.setUSER_BATAL(cursor.getString(7));
                item.setTGL_BATAL(cursor.getString(8));
                item.setALASAN_BATAL(cursor.getString(9));
                item.setUSER_ENTRY(cursor.getString(10));
                item.setTGL_ENTRY(cursor.getString(11));
                item.setUSER_UPDATE(cursor.getString(12));
                item.setTGL_UPDATE(cursor.getString(13));
                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertPenugasan(SQII_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("TGL_PENUGASAN", item.getTGL_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        long id = db.insert("SQII_PENUGASAN", null, values);
        db.close();

        return id;
    }

    public boolean updatePenugasan(SQII_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("TGL_PENUGASAN", item.getTGL_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN();

        int affected_rows = db.update("SQII_PENUGASAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deletePenugasan(SQII_PENUGASAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN();

        int affected_rows = db.delete("SQII_PENUGASAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_PENUGASAN_DETIL*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_PENUGASAN_DETIL> getAllPenugasanDetil() {
        List<SQII_PENUGASAN_DETIL> listData = new ArrayList<SQII_PENUGASAN_DETIL>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PENUGASAN_DETIL", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PENUGASAN_DETIL item = new SQII_PENUGASAN_DETIL();
                item.setNO_PENUGASAN(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setBLOK(cursor.getString(2));
                item.setNOMOR(cursor.getString(3));
                item.setPENGAWAS(cursor.getString(4));
                item.setSM(cursor.getString(5));
                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertPenugasanDetil(SQII_PENUGASAN_DETIL item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        long id = db.insert("SQII_PENUGASAN_DETIL", null, values);
        db.close();

        return id;
    }

    public boolean updatePenugasanDetil(SQII_PENUGASAN_DETIL item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR();

        int affected_rows = db.update("SQII_PENUGASAN_DETIL", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deletePenugasanDetil(SQII_PENUGASAN_DETIL item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR();

        int affected_rows = db.delete("SQII_PENUGASAN_DETIL", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_ITEM_DEFECT_PENUGASAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_ITEM_DEFECT_PENUGASAN> getAllItemDefectPenugasanData() {
        List<SQII_ITEM_DEFECT_PENUGASAN> listData = new ArrayList<SQII_ITEM_DEFECT_PENUGASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_ITEM_DEFECT_PENUGASAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_ITEM_DEFECT_PENUGASAN item = new SQII_ITEM_DEFECT_PENUGASAN();
                item.setNO_PENUGASAN(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setBLOK(cursor.getString(2));
                item.setNOMOR(cursor.getString(3));
                item.setKD_JENIS(cursor.getString(4));
                item.setKD_TIPE(cursor.getString(5));
                item.setKD_ITEM_DEFECT(cursor.getFloat(6));
                item.setKD_LANTAI(cursor.getFloat(7));
                item.setJML_FOTO_PENUGASAN(cursor.getString(8));
                item.setFLAG_AKTIF(cursor.getString(9));
                item.setFLAG_BATAL(cursor.getString(10));
                item.setUSER_BATAL(cursor.getString(11));
                item.setTGL_BATAL(cursor.getString(12));
                item.setALASAN_BATAL(cursor.getString(13));
                item.setUSER_ENTRY(cursor.getString(14));
                item.setTGL_ENTRY(cursor.getString(15));
                item.setUSER_UPDATE(cursor.getString(16));
                item.setTGL_UPDATE(cursor.getString(17));
                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertItemDefectPenugasan(SQII_ITEM_DEFECT_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("JML_FOTO_PENUGASAN", item.getJML_FOTO_PENUGASAN());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        long id = db.insert("SQII_ITEM_DEFECT_PENUGASAN", null, values);
        db.close();

        return id;
    }

    public boolean updateItemDefectPenugasan(SQII_ITEM_DEFECT_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("JML_FOTO_PENUGASAN", item.getJML_FOTO_PENUGASAN());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.update("SQII_ITEM_DEFECT_PENUGASAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteItemDefectPenugasan(SQII_ITEM_DEFECT_PENUGASAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.delete("SQII_ITEM_DEFECT_PENUGASAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_PELAKSANAAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_PELAKSANAAN> getAllPelaksanaan() {
        List<SQII_PELAKSANAAN> listData = new ArrayList<SQII_PELAKSANAAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PELAKSANAAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PELAKSANAAN item = new SQII_PELAKSANAAN();
                item.setNO_PENUGASAN(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setBLOK(cursor.getString(2));
                item.setNOMOR(cursor.getString(3));
                item.setKD_JENIS(cursor.getString(4));
                item.setKD_TIPE(cursor.getString(5));
                item.setKD_ITEM_DEFECT(cursor.getFloat(6));
                item.setKD_LANTAI(cursor.getFloat(7));
                item.setURUT_PELAKSANAAN(cursor.getFloat(8));
                item.setTGL_PELAKSANAAN(cursor.getString(9));
                item.setPETUGAS_QC(cursor.getString(10));
                item.setPENGAWAS(cursor.getString(11));
                item.setSM(cursor.getString(12));
                item.setSTATUS_DEFECT(cursor.getString(13));
                item.setSTATUS_PEKERJAAN(cursor.getString(14));
                item.setCATATAN(cursor.getString(15));
                item.setFLAG_UPLOAD(cursor.getString(16));
                item.setTGL_UPLOAD(cursor.getString(17));
                item.setSRC_FOTO_DENAH(cursor.getString(18));
                item.setSRC_FOTO_DEFECT(cursor.getString(19));
                item.setLAMA_PERBAIKAN(cursor.getFloat(20));
                item.setTGL_ENTRY_LAMA_PERBAIKAN(cursor.getString(21));
                item.setTGL_JATUH_TEMPO_PERBAIKAN(cursor.getString(22));
                item.setFLAG_AKTIF(cursor.getString(23));
                item.setUSER_AKTIF(cursor.getString(24));
                item.setTGL_AKTIF(cursor.getString(25));
                item.setALASAN_AKTIF(cursor.getString(26));
                item.setFLAG_BATAL(cursor.getString(27));
                item.setUSER_BATAL(cursor.getString(28));
                item.setTGL_BATAL(cursor.getString(29));
                item.setALASAN_BATAL(cursor.getString(30));
                item.setUSER_ENTRY(cursor.getString(31));
                item.setTGL_ENTRY(cursor.getString(32));
                item.setUSER_UPDATE(cursor.getString(33));
                item.setTGL_UPDATE(cursor.getString(34));
                item.setROWID(cursor.getFloat(35));
                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertPelaksanaan(SQII_PELAKSANAAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("URUT_PELAKSANAAN", item.getURUT_PELAKSANAAN());
        values.put("TGL_PELAKSANAAN", item.getTGL_PELAKSANAAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("STATUS_DEFECT", item.getSTATUS_DEFECT());
        values.put("STATUS_PEKERJAAN", item.getSTATUS_PEKERJAAN());
        values.put("CATATAN", item.getCATATAN());
        values.put("FLAG_UPLOAD", item.getFLAG_UPLOAD());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("SRC_FOTO_DEFECT", item.getSRC_FOTO_DEFECT());
        values.put("LAMA_PERBAIKAN", item.getLAMA_PERBAIKAN());
        values.put("TGL_ENTRY_LAMA_PERBAIKAN", item.getTGL_ENTRY_LAMA_PERBAIKAN());
        values.put("TGL_JATUH_TEMPO_PERBAIKAN", item.getTGL_JATUH_TEMPO_PERBAIKAN());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_AKTIF", item.getUSER_AKTIF());
        values.put("TGL_AKTIF", item.getTGL_AKTIF());
        values.put("ALASAN_AKTIF", item.getALASAN_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        values.put("ROWID", item.getROWID());
        long id = db.insert("SQII_PELAKSANAAN", null, values);
        db.close();

        return id;
    }

    public boolean updatePelaksanaan(SQII_PELAKSANAAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("URUT_PELAKSANAAN", item.getURUT_PELAKSANAAN());
        values.put("TGL_PELAKSANAAN", item.getTGL_PELAKSANAAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("STATUS_DEFECT", item.getSTATUS_DEFECT());
        values.put("STATUS_PEKERJAAN", item.getSTATUS_PEKERJAAN());
        values.put("CATATAN", item.getCATATAN());
        values.put("FLAG_UPLOAD", item.getFLAG_UPLOAD());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("SRC_FOTO_DEFECT", item.getSRC_FOTO_DEFECT());
        values.put("LAMA_PERBAIKAN", item.getLAMA_PERBAIKAN());
        values.put("TGL_ENTRY_LAMA_PERBAIKAN", item.getTGL_ENTRY_LAMA_PERBAIKAN());
        values.put("TGL_JATUH_TEMPO_PERBAIKAN", item.getTGL_JATUH_TEMPO_PERBAIKAN());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_AKTIF", item.getUSER_AKTIF());
        values.put("TGL_AKTIF", item.getTGL_AKTIF());
        values.put("ALASAN_AKTIF", item.getALASAN_AKTIF());
        values.put("FLAG_BATAL", item.getFLAG_BATAL());
        values.put("USER_BATAL", item.getUSER_BATAL());
        values.put("TGL_BATAL", item.getTGL_BATAL());
        values.put("ALASAN_BATAL", item.getALASAN_BATAL());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        values.put("ROWID", item.getROWID());

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI() + " AND " +
                "URUT_PELAKSANAAN =" + item.getURUT_PELAKSANAAN();

        int affected_rows = db.update("SQII_PELAKSANAAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deletePelaksanaan(SQII_PELAKSANAAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI() + " AND " +
                "URUT_PELAKSANAAN =" + item.getURUT_PELAKSANAAN();

        int affected_rows = db.delete("SQII_PELAKSANAAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_HISTORY_UPLOAD*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_HISTORY_UPLOAD> getAllHistoryUpload() {
        List<SQII_HISTORY_UPLOAD> listData = new ArrayList<SQII_HISTORY_UPLOAD>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_HISTORY_UPLOAD", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_HISTORY_UPLOAD item = new SQII_HISTORY_UPLOAD();
                item.setROWID(cursor.getFloat(0));
                item.setNO_PENUGASAN(cursor.getString(1));
                item.setKD_KAWASAN(cursor.getString(2));
                item.setBLOK(cursor.getString(3));
                item.setNOMOR(cursor.getString(4));
                item.setKD_JENIS(cursor.getString(5));
                item.setKD_TIPE(cursor.getString(6));
                item.setKD_ITEM_DEFECT(cursor.getFloat(7));
                item.setKD_LANTAI(cursor.getFloat(8));
                item.setURUT_PELAKSANAAN(cursor.getFloat(9));
                item.setTGL_UPLOAD(cursor.getString(10));
                item.setCATATAN(cursor.getString(11));
                item.setUSER_ENTRY(cursor.getString(12));
                item.setTGL_ENTRY(cursor.getString(13));
                item.setUSER_UPDATE(cursor.getString(14));
                item.setTGL_UPDATE(cursor.getString(15));
                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertHistoryUpload(SQII_HISTORY_UPLOAD item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("ROWID", item.getROWID());
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("URUT_PELAKSANAAN", item.getURUT_PELAKSANAAN());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("CATATAN", item.getCATATAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        long id = db.insert("SQII_HISTORY_UPLOAD", null, values);
        db.close();

        return id;
    }

    public boolean updateHistoryUpload(SQII_HISTORY_UPLOAD item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().toString()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME,null);
        ContentValues values = new ContentValues();
        values.put("ROWID", item.getROWID());
        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("URUT_PELAKSANAAN", item.getURUT_PELAKSANAAN());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("CATATAN", item.getCATATAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        long id = db.insert("SQII_HISTORY_UPLOAD", null, values);

        String filter = "ROW_ID = " + item.getROWID();

        int affected_rows = db.update("SQII_HISTORY_UPLOAD", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteHistoryUpload(SQII_HISTORY_UPLOAD item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "ROW_ID = " + item.getROWID();

        int affected_rows = db.delete("SQII_HISTORY_UPLOAD", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

}
