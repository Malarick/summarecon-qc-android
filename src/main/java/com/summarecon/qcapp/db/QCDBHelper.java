package com.summarecon.qcapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.summarecon.qcapp.core.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class QCDBHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = "QCDBHelper";
    private static QCDBHelper sInstance = null;
    private static int DB_VERSION = 1;
    private static int success = 1;
    private static int failed = -1;

    private QCDBHelper(Context context) {
        super(context, Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null, DB_VERSION);
        getReadableDatabase();
    }

    public static QCDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QCDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG_TAG, "onCreate Database...");

        /*Table SQII_KAWASAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_KAWASAN (" +
                "KD_KAWASAN CHAR(3), " +
                "NM_KAWASAN VARCHAR(50), " +
                "KONEKSI VARCHAR(50), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_KAWASAN))"
        );

        /*Table SQII_CLUSTER*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_CLUSTER (" +
                "KD_CLUSTER VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "NM_CLUSTER VARCHAR(50), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_CLUSTER, KD_KAWASAN)" +
                "FOREIGN KEY (KD_KAWASAN) REFERENCES SQII_KAWASAN (KD_KAWASAN))"
        );

        /*Table SQII_JENIS_BANGUNAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_JENIS_BANGUNAN (" +
                "KD_JENIS VARCHAR(50), " +
                "NM_JENIS VARCHAR(50), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_JENIS))"
        );

        /*Table SQII_TIPE_RUMAH*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_TIPE_RUMAH (" +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "KD_CLUSTER VARCHAR(50), " +
                "NM_TIPE VARCHAR(50), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_JENIS, KD_TIPE, KD_KAWASAN)" +
                "FOREIGN KEY (KD_JENIS) REFERENCES SQII_JENIS_BANGUNAN (KD_JENIS)" +
                "FOREIGN KEY (KD_KAWASAN) REFERENCES SQII_KAWASAN (KD_KAWASAN))"
        );

        /*Table SQII_LANTAI*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_LANTAI (" +
                "KD_LANTAI DECIMAL(18,0), " +
                "NM_LANTAI VARCHAR(50), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_LANTAI))"
        );

        /*Table SQII_USER*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_USER (" +
                "NO_INDUK VARCHAR(50), " +
                "NAMA VARCHAR(50), " +
                "PASSWORD VARCHAR(255), " +
                "FLAG_PETUGAS_ADMIN CHAR(1), " +
                "FLAG_SM CHAR(1), " +
                "FLAG_PETUGAS_QC CHAR(1), " +
                "FLAG_PENGAWAS CHAR(1), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_INDUK))"
        );

        /*Table SQII_KAWASAN_USER*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_KAWASAN_USER (" +
                "NO_INDUK VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_INDUK, KD_KAWASAN)" +
                "FOREIGN KEY (KD_KAWASAN) REFERENCES SQII_KAWASAN (KD_KAWASAN))"
        );

        /*Table SQII_KATEGORI_DEFECT*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_KATEGORI_DEFECT (" +
                "KD_KATEGORI_DEFECT DECIMAL(18,0), " +
                "NM_KATEGORI_DEFECT VARCHAR(50), " +
                "DESKRIPSI VARCHAR(255), " +
                "TIPE_DENAH CHAR(1), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_KATEGORI_DEFECT))"
        );

        /*Table SQII_ITEM_DEFECT*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_ITEM_DEFECT (" +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "NM_ITEM_DEFECT VARCHAR(50), " +
                "KD_KATEGORI_DEFECT DECIMAL(18,0), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_ITEM_DEFECT)" +
                "FOREIGN KEY (KD_KATEGORI_DEFECT) REFERENCES SQII_KATEGORI_DEFECT (KD_KATEGORI_DEFECT))"
        );

        /*Table SQII_ITEM_DEFECT_TIPE_RUMAH*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_ITEM_DEFECT_TIPE_RUMAH (" +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "KD_LANTAI DECIMAL(18,0), " +
                "JML_KUOTA_FOTO DECIMAL(18,0), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_JENIS, KD_TIPE, KD_KAWASAN, KD_ITEM_DEFECT, KD_LANTAI)" +
                "FOREIGN KEY (KD_ITEM_DEFECT) REFERENCES SQII_ITEM_DEFECT (KD_ITEM_DEFECT)" +
                "FOREIGN KEY (KD_LANTAI) REFERENCES SQII_LANTAI (KD_LANTAI)" +
                "FOREIGN KEY (KD_JENIS, KD_TIPE, KD_KAWASAN) REFERENCES SQII_TIPE_RUMAH (KD_JENIS, KD_TIPE, KD_KAWASAN))"
        );

        /*Table SQII_KONTRAKTOR*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_KONTRAKTOR (" +
                "KD_KONTRAKTOR DECIMAL(18,0), " +
                "KD_KAWASAN CHAR(3), " +
                "KODE_KONTRAKTOR VARCHAR(50), " +
                "NM_KONTRAKTOR VARCHAR(255), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_KONTRAKTOR)" +
                "FOREIGN KEY (KD_KAWASAN) REFERENCES SQII_KAWASAN (KD_KAWASAN))"
        );

        /*Table SQII_LANTAI_TIPE_RUMAH*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_LANTAI_TIPE_RUMAH (" +
                "KD_LANTAI DECIMAL(18,0), " +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "PATH_FOTO_DENAH VARCHAR(255), " +
                "SRC_FOTO_DENAH VARCHAR(255), " +
                "PATH_FOTO_DENAH_2 VARCHAR(255), " +
                "SRC_FOTO_DENAH_2 VARCHAR(255), " +
                "KETERANGAN VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_LANTAI, KD_JENIS, KD_TIPE, KD_KAWASAN)" +
                "FOREIGN KEY (KD_LANTAI) REFERENCES SQII_LANTAI (KD_LANTAI)" +
                "FOREIGN KEY (KD_JENIS, KD_TIPE, KD_KAWASAN) REFERENCES SQII_TIPE_RUMAH (KD_JENIS, KD_TIPE, KD_KAWASAN))"
        );

        /*Table SQII_CATATAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_CATATAN (" +
                "KD_CATATAN DECIMAL(18,0), " +
                "KD_ITEM_DEFECT DECIMAL(18,0), " +
                "DESKRIPSI VARCHAR(255), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_CATATAN)" +
                "FOREIGN KEY (KD_ITEM_DEFECT) REFERENCES SQII_ITEM_DEFECT (KD_ITEM_DEFECT))"
        );

        /*Table SQII_STOK*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_STOK (" +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "KD_CLUSTER VARCHAR(50), " +
                "STOK_ID NUMERIC(18,0), " +
                "KD_KONTRAKTOR DECIMAL(18,0), " +
                "KD_JENIS VARCHAR(50), " +
                "KD_TIPE VARCHAR(50), " +
                "PENGAWAS VARCHAR(50), " +
                "SM VARCHAR(50), " +
                "PETUGAS_QC VARCHAR(50), " +
                "DESKRIPSI VARCHAR(255), " +
                "FLAG_AKTIF CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (KD_KAWASAN, BLOK, NOMOR)" +
                "FOREIGN KEY (KD_CLUSTER, KD_KAWASAN) REFERENCES SQII_CLUSTER (KD_CLUSTER, KD_KAWASAN)" +
                "FOREIGN KEY (KD_KONTRAKTOR) REFERENCES SQII_KONTRAKTOR (KD_KONTRAKTOR)" +
                "FOREIGN KEY (KD_JENIS, KD_TIPE, KD_KAWASAN) REFERENCES SQII_TIPE_RUMAH (KD_JENIS, KD_TIPE, KD_KAWASAN))"
        );

        /*Table SQII_PENUGASAN*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_PENUGASAN (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "TGL_PENUGASAN DATETIME, " +
                "KD_KAWASAN CHAR(3), " +
                "KD_CLUSTER VARCHAR(50), " +
                "PETUGAS_QC VARCHAR(50), " +
                "DESKRIPSI VARCHAR(255), " +
                "STATUS_SELESAI CHAR(1), " +
                "FLAG_AKTIF CHAR(1), " +
                "FLAG_BATAL CHAR(1), " +
                "USER_BATAL VARCHAR(50), " +
                "TGL_BATAL DATETIME, " +
                "ALASAN_BATAL VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_PENUGASAN)" +
                "FOREIGN KEY (KD_KAWASAN) REFERENCES SQII_KAWASAN (KD_KAWASAN)" +
                "FOREIGN KEY (KD_CLUSTER, KD_KAWASAN) REFERENCES SQII_CLUSTER (KD_CLUSTER, KD_KAWASAN))"
        );

        /*Table SQII_PENUGASAN_DETIL*/
        db.execSQL("CREATE TABLE IF NOT EXISTS SQII_PENUGASAN_DETIL (" +
                "NO_PENUGASAN VARCHAR(50), " +
                "KD_KAWASAN CHAR(3), " +
                "BLOK VARCHAR(50), " +
                "NOMOR VARCHAR(50), " +
                "KD_CLUSTER VARCHAR(50), " +
                "PENGAWAS VARCHAR(50), " +
                "SM VARCHAR(50), " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR)" +
                "FOREIGN KEY (NO_PENUGASAN) REFERENCES SQII_PENUGASAN (NO_PENUGASAN)" +
                "FOREIGN KEY (KD_CLUSTER, KD_KAWASAN) REFERENCES SQII_CLUSTER (KD_CLUSTER, KD_KAWASAN)" +
                "FOREIGN KEY (KD_KAWASAN, BLOK, NOMOR) REFERENCES SQII_STOK (KD_KAWASAN, BLOK, NOMOR))"
        );

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
                "KD_CLUSTER VARCHAR(50), " +
                "JML_FOTO_PENUGASAN DECIMAL(18,0), " +
                "JML_FOTO_REALISASI DECIMAL(18,0), " +
                "FLAG_AKTIF CHAR(1), " +
                "FLAG_BATAL CHAR(1), " +
                "USER_BATAL VARCHAR(50), " +
                "TGL_BATAL DATETIME, " +
                "ALASAN_BATAL VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI)" +
                "FOREIGN KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR) REFERENCES SQII_PENUGASAN_DETIL (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR)" +
                "FOREIGN KEY (KD_JENIS, KD_TIPE, KD_KAWASAN, KD_ITEM_DEFECT, KD_LANTAI) REFERENCES SQII_ITEM_DEFECT_TIPE_RUMAH (KD_JENIS, KD_TIPE, KD_KAWASAN, KD_ITEM_DEFECT, KD_LANTAI)" +
                "FOREIGN KEY (KD_CLUSTER, KD_KAWASAN) REFERENCES SQII_CLUSTER (KD_CLUSTER, KD_KAWASAN))"
        );

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
                "URUT_FOTO DECIMAL(18,0), " +
                "JENIS_PENUGASAN CHAR(1), " +
                "TGL_PELAKSANAAN DATETIME, " +
                "PETUGAS_QC VARCHAR(50), " +
                "PENGAWAS VARCHAR(50), " +
                "SM VARCHAR(50), " +
                "STATUS_DEFECT CHAR(1), " +
                "STATUS_PEKERJAAN CHAR(1), " +
                "CATATAN VARCHAR(255), " +
                "FLAG_UPLOAD CHAR(1), " +
                "TGL_UPLOAD DATETIME, " +
                "PATH_FOTO_DENAH VARCHAR(255), " +
                "SRC_FOTO_DENAH VARCHAR(255), " +
                "PATH_FOTO_DEFECT VARCHAR(255), " +
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
                "STATUS_SIMPAN CHAR(1), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PARENT_ROWID DECIMAL(18,0), " +
                "ROWID DECIMAL(18,0), " +
                "PRIMARY KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI, URUT_PELAKSANAAN, URUT_FOTO)" +
                "FOREIGN KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI) REFERENCES SQII_ITEM_DEFECT_PENUGASAN (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI))"
        );

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
                "URUT_FOTO DECIMAL(18,0), " +
                "TGL_UPLOAD DATETIME, " +
                "CATATAN VARCHAR(255), " +
                "USER_ENTRY VARCHAR(50), " +
                "TGL_ENTRY DATETIME, " +
                "USER_UPDATE VARCHAR(50), " +
                "TGL_UPDATE DATETIME, " +
                "PRIMARY KEY (ROWID)" +
                "FOREIGN KEY (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI, URUT_PELAKSANAAN, URUT_FOTO) REFERENCES SQII_ITEM_DEFECT_PENUGASAN (NO_PENUGASAN, KD_KAWASAN, BLOK, NOMOR, KD_JENIS, KD_TIPE, KD_ITEM_DEFECT, KD_LANTAI, URUT_PELAKSANAAN, URUT_FOTO))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int executeSQLScriptFile() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        File dbScriptFile = new File(Configuration.APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY);

        db.beginTransaction();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbScriptFile));
            String line;

            while ((line = br.readLine()) != null) {
                try {
                    db.execSQL(line);
                } catch (Exception e) {
                    Log.e("ERROR_BATCH_READLINE", e.toString());
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("error_batch", e.toString());
            return failed;
        } finally {
            db.endTransaction();
        }

        return success;
    }

    /*LIST FUNCTION TABLE SQII_CATATAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_CATATAN> getAllCatatan() {
        List<SQII_CATATAN> listData = new ArrayList<SQII_CATATAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_CATATAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_CATATAN item = new SQII_CATATAN();

                item.setKD_CATATAN(cursor.getFloat(0));
                item.setKD_ITEM_DEFECT(cursor.getFloat(1));
                item.setDESKRIPSI(cursor.getString(2));
                item.setFLAG_AKTIF(cursor.getString(3));
                item.setUSER_ENTRY(cursor.getString(4));
                item.setTGL_ENTRY(cursor.getString(5));
                item.setUSER_UPDATE(cursor.getString(6));
                item.setTGL_UPDATE(cursor.getString(7));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertCatatan(SQII_CATATAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_CATATAN", item.getKD_CATATAN());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_CATATAN", null, values);
        db.close();

        return id;
    }

    public boolean updateCatatan(SQII_CATATAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_CATATAN", item.getKD_CATATAN());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_CATATAN = " + item.getKD_CATATAN();

        int affected_rows = db.update("SQII_CATATAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteCatatan(SQII_CATATAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_CATATAN = " + item.getKD_CATATAN();

        int affected_rows = db.delete("SQII_CATATAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_CLUSTER*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_CLUSTER> getAllCluster() {
        List<SQII_CLUSTER> listData = new ArrayList<SQII_CLUSTER>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_CLUSTER", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_CLUSTER item = new SQII_CLUSTER();

                item.setKD_CLUSTER(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setNM_CLUSTER(cursor.getString(2));
                item.setFLAG_AKTIF(cursor.getString(3));
                item.setUSER_ENTRY(cursor.getString(4));
                item.setTGL_ENTRY(cursor.getString(5));
                item.setUSER_UPDATE(cursor.getString(6));
                item.setTGL_UPDATE(cursor.getString(7));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertCluster(SQII_CLUSTER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("NM_CLUSTER", item.getNM_CLUSTER());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_CLUSTER", null, values);
        db.close();

        return id;
    }

    public boolean updateCluster(SQII_CLUSTER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("NM_CLUSTER", item.getNM_CLUSTER());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_CLUSTER = " + item.getKD_CLUSTER() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.update("SQII_CLUSTER", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteCluster(SQII_CLUSTER item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_CLUSTER = " + item.getKD_CLUSTER() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.delete("SQII_CLUSTER", filter, null);
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
                item.setURUT_FOTO(cursor.getFloat(10));
                item.setTGL_UPLOAD(cursor.getString(11));
                item.setCATATAN(cursor.getString(12));
                item.setUSER_ENTRY(cursor.getString(13));
                item.setTGL_ENTRY(cursor.getString(14));
                item.setUSER_UPDATE(cursor.getString(15));
                item.setTGL_UPDATE(cursor.getString(16));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertHistoryUpload(SQII_HISTORY_UPLOAD item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
        values.put("URUT_FOTO", item.getURUT_FOTO());
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
        values.put("URUT_FOTO", item.getURUT_FOTO());
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

    /*LIST FUNCTION TABLE SQII_ITEM_DEFECT*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_ITEM_DEFECT> getAllItemDefect() {
        List<SQII_ITEM_DEFECT> listData = new ArrayList<SQII_ITEM_DEFECT>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_ITEM_DEFECT", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_ITEM_DEFECT item = new SQII_ITEM_DEFECT();

                item.setKD_ITEM_DEFECT(cursor.getFloat(0));
                item.setNM_ITEM_DEFECT(cursor.getString(1));
                item.setKD_KATEGORI_DEFECT(cursor.getFloat(2));
                item.setFLAG_AKTIF(cursor.getString(3));
                item.setUSER_ENTRY(cursor.getString(4));
                item.setTGL_ENTRY(cursor.getString(5));
                item.setUSER_UPDATE(cursor.getString(6));
                item.setTGL_UPDATE(cursor.getString(7));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertItemDefect(SQII_ITEM_DEFECT item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("NM_ITEM_DEFECT", item.getNM_ITEM_DEFECT());
        values.put("KD_KATEGORI_DEFECT", item.getKD_KATEGORI_DEFECT());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_ITEM_DEFECT", null, values);
        db.close();

        return id;
    }

    public boolean updateCatatan(SQII_ITEM_DEFECT item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("NM_ITEM_DEFECT", item.getNM_ITEM_DEFECT());
        values.put("KD_KATEGORI_DEFECT", item.getKD_KATEGORI_DEFECT());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_ITEM_DEFECT = " + item.getKD_ITEM_DEFECT();

        int affected_rows = db.update("SQII_ITEM_DEFECT", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteItemDefect(SQII_ITEM_DEFECT item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_ITEM_DEFECT = " + item.getKD_ITEM_DEFECT();

        int affected_rows = db.delete("SQII_ITEM_DEFECT", filter, null);
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
    public List<SQII_ITEM_DEFECT_PENUGASAN> getAllItemDefectPenugasan() {
        List<SQII_ITEM_DEFECT_PENUGASAN> listData = new ArrayList<SQII_ITEM_DEFECT_PENUGASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
                item.setKD_CLUSTER(cursor.getString(8));
                item.setJML_FOTO_PENUGASAN(cursor.getFloat(9));
                item.setJML_FOTO_REALISASI(cursor.getFloat(10));
                item.setFLAG_AKTIF(cursor.getString(11));
                item.setFLAG_BATAL(cursor.getString(12));
                item.setUSER_BATAL(cursor.getString(13));
                item.setTGL_BATAL(cursor.getString(14));
                item.setALASAN_BATAL(cursor.getString(15));
                item.setUSER_ENTRY(cursor.getString(16));
                item.setTGL_ENTRY(cursor.getString(17));
                item.setUSER_UPDATE(cursor.getString(18));
                item.setTGL_UPDATE(cursor.getString(19));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public List<SQII_ITEM_DEFECT_PENUGASAN> getAllItemDefectPenugasanFoto() {
        String query;

        List<SQII_ITEM_DEFECT_PENUGASAN> listData = new ArrayList<SQII_ITEM_DEFECT_PENUGASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);

        query = "SELECT  SQII_CLUSTER.NM_CLUSTER, \n" +
                "        SQII_ITEM_DEFECT_PENUGASAN.KD_CLUSTER, \n" +
                "        SQII_ITEM_DEFECT_PENUGASAN.BLOK, \n" +
                "        SQII_ITEM_DEFECT_PENUGASAN.NOMOR, \n" +
                "        SQII_ITEM_DEFECT_PENUGASAN.NO_PENUGASAN,\n" +
                "        SQII_PENUGASAN.TGL_PENUGASAN, \n" +
                "        IFNULL(SQII_ITEM_DEFECT_PENUGASAN.JML_FOTO_PENUGASAN,0), \n" +
                "        IFNULL(SQII_ITEM_DEFECT_PENUGASAN.JML_FOTO_REALISASI,0)\n" +
                "FROM    SQII_ITEM_DEFECT_PENUGASAN\n" +
                "        \n" +
                "        INNER JOIN SQII_CLUSTER\n" +
                "        ON  SQII_ITEM_DEFECT_PENUGASAN.KD_CLUSTER = SQII_CLUSTER.KD_CLUSTER AND\n" +
                "            SQII_ITEM_DEFECT_PENUGASAN.KD_KAWASAN = SQII_CLUSTER.KD_KAWASAN\n" +
                "        \n" +
                "        INNER JOIN SQII_PENUGASAN\n" +
                "        ON  SQII_ITEM_DEFECT_PENUGASAN.NO_PENUGASAN = SQII_PENUGASAN.NO_PENUGASAN";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_ITEM_DEFECT_PENUGASAN item = new SQII_ITEM_DEFECT_PENUGASAN();

                item.setNM_CLUSTER(cursor.getString(0));
                item.setKD_CLUSTER(cursor.getString(1));
                item.setBLOK(cursor.getString(2));
                item.setNOMOR(cursor.getString(3));
                item.setNO_PENUGASAN(cursor.getString(4));
                item.setTGL_PENUGASAN(cursor.getString(5));
                item.setJML_FOTO_PENUGASAN(cursor.getFloat(6));
                item.setJML_FOTO_REALISASI(cursor.getFloat(7));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertItemDefectPenugasan(SQII_ITEM_DEFECT_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("JML_FOTO_PENUGASAN", item.getJML_FOTO_PENUGASAN());
        values.put("JML_FOTO_REALISASI", item.getJML_FOTO_REALISASI());
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("JML_FOTO_PENUGASAN", item.getJML_FOTO_PENUGASAN());
        values.put("JML_FOTO_REALISASI", item.getJML_FOTO_REALISASI());
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

    /*LIST FUNCTION TABLE SQII_ITEM_DEFECT_TIPE_RUMAH*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_ITEM_DEFECT_TIPE_RUMAH> getAllItemDefectTipeRumah() {
        List<SQII_ITEM_DEFECT_TIPE_RUMAH> listData = new ArrayList<SQII_ITEM_DEFECT_TIPE_RUMAH>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_ITEM_DEFECT_TIPE_RUMAH", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_ITEM_DEFECT_TIPE_RUMAH item = new SQII_ITEM_DEFECT_TIPE_RUMAH();

                item.setKD_JENIS(cursor.getString(0));
                item.setKD_TIPE(cursor.getString(1));
                item.setKD_KAWASAN(cursor.getString(2));
                item.setKD_ITEM_DEFECT(cursor.getFloat(3));
                item.setKD_LANTAI(cursor.getFloat(4));
                item.setJML_KUOTA_FOTO(cursor.getFloat(5));
                item.setUSER_ENTRY(cursor.getString(6));
                item.setTGL_ENTRY(cursor.getString(7));
                item.setUSER_UPDATE(cursor.getString(8));
                item.setTGL_UPDATE(cursor.getString(9));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertItemDefectTipeRumah(SQII_ITEM_DEFECT_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("JML_KUOTA_FOTO", item.getJML_KUOTA_FOTO());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_ITEM_DEFECT_TIPE_RUMAH", null, values);
        db.close();

        return id;
    }

    public boolean updateItemDefectTipeRumah(SQII_ITEM_DEFECT_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_ITEM_DEFECT", item.getKD_ITEM_DEFECT());
        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("JML_KUOTA_FOTO", item.getJML_KUOTA_FOTO());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.update("SQII_ITEM_DEFECT_TIPE_RUMAH", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteItemDefectTipeRumah(SQII_ITEM_DEFECT_TIPE_RUMAH item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.delete("SQII_ITEM_DEFECT_TIPE_RUMAH", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_JENIS_BANGUNAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_JENIS_BANGUNAN> getAllJenisBangunan() {
        List<SQII_JENIS_BANGUNAN> listData = new ArrayList<SQII_JENIS_BANGUNAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_JENIS_BANGUNAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_JENIS_BANGUNAN item = new SQII_JENIS_BANGUNAN();

                item.setKD_JENIS(cursor.getString(0));
                item.setNM_JENIS(cursor.getString(1));
                item.setFLAG_AKTIF(cursor.getString(2));
                item.setUSER_ENTRY(cursor.getString(3));
                item.setTGL_ENTRY(cursor.getString(4));
                item.setUSER_UPDATE(cursor.getString(5));
                item.setTGL_UPDATE(cursor.getString(6));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertJenisBangunan(SQII_JENIS_BANGUNAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("NM_JENIS", item.getNM_JENIS());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_JENIS_BANGUNAN", null, values);
        db.close();

        return id;
    }

    public boolean updateJenisBangunan(SQII_JENIS_BANGUNAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("NM_JENIS", item.getNM_JENIS());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_JENIS = " + item.getKD_JENIS();

        int affected_rows = db.update("SQII_JENIS_BANGUNAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteJenisBangunan(SQII_JENIS_BANGUNAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_JENIS = " + item.getKD_JENIS();

        int affected_rows = db.delete("SQII_JENIS_BANGUNAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_KATEGORI_DEFECT*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_KATEGORI_DEFECT> getAllKategoriDefect() {
        List<SQII_KATEGORI_DEFECT> listData = new ArrayList<SQII_KATEGORI_DEFECT>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_KATEGORI_DEFECT", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_KATEGORI_DEFECT item = new SQII_KATEGORI_DEFECT();

                item.setKD_KATEGORI_DEFECT(cursor.getFloat(0));
                item.setNM_KATEGORI_DEFECT(cursor.getString(1));
                item.setDESKRIPSI(cursor.getString(2));
                item.setTIPE_DENAH(cursor.getString(3));
                item.setFLAG_AKTIF(cursor.getString(4));
                item.setUSER_ENTRY(cursor.getString(5));
                item.setTGL_ENTRY(cursor.getString(6));
                item.setUSER_UPDATE(cursor.getString(7));
                item.setTGL_UPDATE(cursor.getString(8));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertKategoriDefect(SQII_KATEGORI_DEFECT item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KATEGORI_DEFECT", item.getKD_KATEGORI_DEFECT());
        values.put("NM_KATEGORI_DEFECT", item.getNM_KATEGORI_DEFECT());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("TIPE_DENAH", item.getTIPE_DENAH());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_KATEGORI_DEFECT", null, values);
        db.close();

        return id;
    }

    public boolean updateKategoriDefect(SQII_KATEGORI_DEFECT item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KATEGORI_DEFECT", item.getKD_KATEGORI_DEFECT());
        values.put("NM_KATEGORI_DEFECT", item.getNM_KATEGORI_DEFECT());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("TIPE_DENAH", item.getTIPE_DENAH());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_KATEGORI_DEFECT = " + item.getKD_KATEGORI_DEFECT();

        int affected_rows = db.update("SQII_KATEGORI_DEFECT", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteKategoriDefect(SQII_KATEGORI_DEFECT item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_KATEGORI_DEFECT = " + item.getKD_KATEGORI_DEFECT();

        int affected_rows = db.delete("SQII_KATEGORI_DEFECT", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_KAWASAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_KAWASAN> getAllKawasan() {
        List<SQII_KAWASAN> listData = new ArrayList<SQII_KAWASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_KAWASAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_KAWASAN item = new SQII_KAWASAN();

                item.setKD_KAWASAN(cursor.getString(0));
                item.setNM_KAWASAN(cursor.getString(1));
                item.setKONEKSI(cursor.getString(2));
                item.setFLAG_AKTIF(cursor.getString(3));
                item.setUSER_ENTRY(cursor.getString(4));
                item.setTGL_ENTRY(cursor.getString(5));
                item.setUSER_UPDATE(cursor.getString(6));
                item.setTGL_UPDATE(cursor.getString(7));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertKawasan(SQII_KAWASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("NM_KAWASAN", item.getNM_KAWASAN());
        values.put("KONEKSI", item.getKONEKSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_KAWASAN", null, values);
        db.close();

        return id;
    }

    public boolean updateKawasan(SQII_KAWASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("NM_KAWASAN", item.getNM_KAWASAN());
        values.put("KONEKSI", item.getKONEKSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_KAWASAN = " + item.getKD_KAWASAN();

        int affected_rows = db.update("SQII_KAWASAN", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteKawasan(SQII_KAWASAN item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_KAWASAN = " + item.getKD_KAWASAN();

        int affected_rows = db.delete("SQII_KAWASAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_KAWASAN_USER*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_KAWASAN_USER> getAllKawasanUser() {
        List<SQII_KAWASAN_USER> listData = new ArrayList<SQII_KAWASAN_USER>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_KAWASAN_USER", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_KAWASAN_USER item = new SQII_KAWASAN_USER();

                item.setNO_INDUK(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setUSER_ENTRY(cursor.getString(2));
                item.setTGL_ENTRY(cursor.getString(3));
                item.setUSER_UPDATE(cursor.getString(4));
                item.setTGL_UPDATE(cursor.getString(5));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertKawasanUser(SQII_KAWASAN_USER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_INDUK", item.getNO_INDUK());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_KAWASAN_USER", null, values);
        db.close();

        return id;
    }

    public boolean updateKawasanUser(SQII_KAWASAN_USER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_INDUK", item.getNO_INDUK());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "NO_INDUK =" + item.getNO_INDUK() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.update("SQII_KAWASAN_USER", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteKawasanUser(SQII_KAWASAN_USER item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_INDUK =" + item.getNO_INDUK() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.delete("SQII_KAWASAN_USER", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_KONTRAKTOR*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_KONTRAKTOR> getAllKontraktor() {
        List<SQII_KONTRAKTOR> listData = new ArrayList<SQII_KONTRAKTOR>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_KONTRAKTOR", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_KONTRAKTOR item = new SQII_KONTRAKTOR();

                item.setKD_KONTRAKTOR(cursor.getFloat(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setKODE_KONTRAKTOR(cursor.getString(2));
                item.setNM_KONTRAKTOR(cursor.getString(3));
                item.setFLAG_AKTIF(cursor.getString(4));
                item.setUSER_ENTRY(cursor.getString(5));
                item.setTGL_ENTRY(cursor.getString(6));
                item.setUSER_UPDATE(cursor.getString(7));
                item.setTGL_UPDATE(cursor.getString(8));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertKontraktor(SQII_KONTRAKTOR item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KONTRAKTOR", item.getKD_KONTRAKTOR());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KODE_KONTRAKTOR", item.getKODE_KONTRAKTOR());
        values.put("NM_KONTRAKTOR", item.getNM_KONTRAKTOR());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_KONTRAKTOR", null, values);
        db.close();

        return id;
    }

    public boolean updateKontraktor(SQII_KONTRAKTOR item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KONTRAKTOR", item.getKD_KONTRAKTOR());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KODE_KONTRAKTOR", item.getKODE_KONTRAKTOR());
        values.put("NM_KONTRAKTOR", item.getNM_KONTRAKTOR());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_KONTRAKTOR =" + item.getKD_KONTRAKTOR();

        int affected_rows = db.update("SQII_KONTRAKTOR", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteKontraktor(SQII_KONTRAKTOR item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_KONTRAKTOR =" + item.getKD_KONTRAKTOR();

        int affected_rows = db.delete("SQII_KONTRAKTOR", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_LANTAI*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_LANTAI> getAllLantai() {
        List<SQII_LANTAI> listData = new ArrayList<SQII_LANTAI>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_LANTAI", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_LANTAI item = new SQII_LANTAI();

                item.setKD_LANTAI(cursor.getFloat(0));
                item.setNM_LANTAI(cursor.getString(1));
                item.setFLAG_AKTIF(cursor.getString(2));
                item.setUSER_ENTRY(cursor.getString(3));
                item.setTGL_ENTRY(cursor.getString(4));
                item.setUSER_UPDATE(cursor.getString(5));
                item.setTGL_UPDATE(cursor.getString(6));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertLantai(SQII_LANTAI item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("NM_LANTAI", item.getNM_LANTAI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_LANTAI", null, values);
        db.close();

        return id;
    }

    public boolean updateLantai(SQII_LANTAI item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("NM_LANTAI", item.getNM_LANTAI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.update("SQII_LANTAI", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteLantai(SQII_LANTAI item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_LANTAI =" + item.getKD_LANTAI();

        int affected_rows = db.delete("SQII_LANTAI", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_LANTAI_TIPE_RUMAH*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_LANTAI_TIPE_RUMAH> getAllLantaiTipeRumah() {
        List<SQII_LANTAI_TIPE_RUMAH> listData = new ArrayList<SQII_LANTAI_TIPE_RUMAH>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_LANTAI_TIPE_RUMAH", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_LANTAI_TIPE_RUMAH item = new SQII_LANTAI_TIPE_RUMAH();

                item.setKD_LANTAI(cursor.getFloat(0));
                item.setKD_JENIS(cursor.getString(1));
                item.setKD_TIPE(cursor.getString(2));
                item.setKD_KAWASAN(cursor.getString(3));
                item.setPATH_FOTO_DENAH(cursor.getString(4));
                item.setSRC_FOTO_DENAH(cursor.getString(5));
                item.setPATH_FOTO_DENAH_2(cursor.getString(6));
                item.setSRC_FOTO_DENAH_2(cursor.getString(7));
                item.setKETERANGAN(cursor.getString(8));
                item.setUSER_ENTRY(cursor.getString(9));
                item.setTGL_ENTRY(cursor.getString(10));
                item.setUSER_UPDATE(cursor.getString(11));
                item.setTGL_UPDATE(cursor.getString(12));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertLantaiTipeRumah(SQII_LANTAI_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("PATH_FOTO_DENAH", item.getPATH_FOTO_DENAH());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("PATH_FOTO_DENAH_2", item.getPATH_FOTO_DENAH_2());
        values.put("SRC_FOTO_DENAH_2", item.getSRC_FOTO_DENAH_2());
        values.put("KETERANGAN", item.getKETERANGAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_LANTAI_TIPE_RUMAH", null, values);
        db.close();

        return id;
    }

    public boolean updateLantaiTipeRumah(SQII_LANTAI_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_LANTAI", item.getKD_LANTAI());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("PATH_FOTO_DENAH", item.getPATH_FOTO_DENAH());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("PATH_FOTO_DENAH_2", item.getPATH_FOTO_DENAH_2());
        values.put("SRC_FOTO_DENAH_2", item.getSRC_FOTO_DENAH_2());
        values.put("KETERANGAN", item.getKETERANGAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_LANTAI = " + item.getKD_LANTAI() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.update("SQII_LANTAI_TIPE_RUMAH", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteLantaiTipeRumah(SQII_LANTAI_TIPE_RUMAH item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_LANTAI = " + item.getKD_LANTAI() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.delete("SQII_LANTAI_TIPE_RUMAH", filter, null);
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
                item.setURUT_FOTO(cursor.getFloat(9));
                item.setJENIS_PENUGASAN(cursor.getString(10));
                item.setTGL_PELAKSANAAN(cursor.getString(11));
                item.setPETUGAS_QC(cursor.getString(12));
                item.setPENGAWAS(cursor.getString(13));
                item.setSM(cursor.getString(14));
                item.setSTATUS_DEFECT(cursor.getString(15));
                item.setSTATUS_PEKERJAAN(cursor.getString(16));
                item.setCATATAN(cursor.getString(17));
                item.setFLAG_UPLOAD(cursor.getString(18));
                item.setTGL_UPLOAD(cursor.getString(19));
                item.setPATH_FOTO_DENAH(cursor.getString(20));
                item.setSRC_FOTO_DENAH(cursor.getString(21));
                item.setPATH_FOTO_DEFECT(cursor.getString(22));
                item.setSRC_FOTO_DEFECT(cursor.getString(23));
                item.setLAMA_PERBAIKAN(cursor.getFloat(24));
                item.setTGL_ENTRY_LAMA_PERBAIKAN(cursor.getString(25));
                item.setTGL_JATUH_TEMPO_PERBAIKAN(cursor.getString(26));
                item.setFLAG_AKTIF(cursor.getString(27));
                item.setUSER_AKTIF(cursor.getString(28));
                item.setTGL_AKTIF(cursor.getString(29));
                item.setALASAN_AKTIF(cursor.getString(30));
                item.setFLAG_BATAL(cursor.getString(31));
                item.setUSER_BATAL(cursor.getString(32));
                item.setTGL_BATAL(cursor.getString(33));
                item.setALASAN_BATAL(cursor.getString(34));
                item.setSTATUS_SIMPAN(cursor.getString(35));
                item.setUSER_ENTRY(cursor.getString(36));
                item.setTGL_ENTRY(cursor.getString(37));
                item.setUSER_UPDATE(cursor.getString(38));
                item.setTGL_UPDATE(cursor.getString(39));
                item.setPARENT_ROWID(cursor.getFloat(40));
                item.setROWID(cursor.getFloat(41));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public List<SQII_PELAKSANAAN> getAllPelaksanaan(String kdCluster, String kdKawasan, String blok, String nomor, String tglPenugasan) {
        String query;

        List<SQII_PELAKSANAAN> listData = new ArrayList<SQII_PELAKSANAAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);

        query = "SELECT  SQII_PELAKSANAAN.PATH_FOTO_DEFECT,\n" +
                "        SQII_PELAKSANAAN.SRC_FOTO_DEFECT\n" +
                "FROM    SQII_PELAKSANAAN\n" +
                "WHERE   KD_CLUSTER = '" + kdCluster + "'\n" +
                "        KD_KAWASAN = '" + kdKawasan + "'\n" +
                "        BLOK = '" + blok + "'\n" +
                "        NOMOR = '" + nomor + "'\n" +
                "        TANGGAL_PENUGASAN = '" + tglPenugasan + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PELAKSANAAN item = new SQII_PELAKSANAAN();

                item.setPATH_FOTO_DEFECT(cursor.getString(0));
                item.setSRC_FOTO_DEFECT(cursor.getString(1));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public List<SQII_PELAKSANAAN> getAllPelaksanaan(String petugasQC, String jenisPenugasan) {
        /*tes*/
        List<SQII_PELAKSANAAN> listData = new ArrayList<SQII_PELAKSANAAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PELAKSANAAN WHERE PETUGAS_QC = '" + petugasQC + "' AND JENIS_PENUGASAN = '" + jenisPenugasan + "'", null);
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
                item.setURUT_FOTO(cursor.getFloat(9));
                item.setJENIS_PENUGASAN(cursor.getString(10));
                item.setTGL_PELAKSANAAN(cursor.getString(11));
                item.setPETUGAS_QC(cursor.getString(12));
                item.setPENGAWAS(cursor.getString(13));
                item.setSM(cursor.getString(14));
                item.setSTATUS_DEFECT(cursor.getString(15));
                item.setSTATUS_PEKERJAAN(cursor.getString(16));
                item.setCATATAN(cursor.getString(17));
                item.setFLAG_UPLOAD(cursor.getString(18));
                item.setTGL_UPLOAD(cursor.getString(19));
                item.setPATH_FOTO_DENAH(cursor.getString(20));
                item.setSRC_FOTO_DENAH(cursor.getString(21));
                item.setPATH_FOTO_DEFECT(cursor.getString(22));
                item.setSRC_FOTO_DEFECT(cursor.getString(23));
                item.setLAMA_PERBAIKAN(cursor.getFloat(24));
                item.setTGL_ENTRY_LAMA_PERBAIKAN(cursor.getString(25));
                item.setTGL_JATUH_TEMPO_PERBAIKAN(cursor.getString(26));
                item.setFLAG_AKTIF(cursor.getString(27));
                item.setUSER_AKTIF(cursor.getString(28));
                item.setTGL_AKTIF(cursor.getString(29));
                item.setALASAN_AKTIF(cursor.getString(30));
                item.setFLAG_BATAL(cursor.getString(31));
                item.setUSER_BATAL(cursor.getString(32));
                item.setTGL_BATAL(cursor.getString(33));
                item.setALASAN_BATAL(cursor.getString(34));
                item.setSTATUS_SIMPAN(cursor.getString(35));
                item.setUSER_ENTRY(cursor.getString(36));
                item.setTGL_ENTRY(cursor.getString(37));
                item.setUSER_UPDATE(cursor.getString(38));
                item.setTGL_UPDATE(cursor.getString(39));
                item.setPARENT_ROWID(cursor.getFloat(40));
                item.setROWID(cursor.getFloat(41));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public List<SQII_PELAKSANAAN> getAllPelaksanaanPenugasan(String tglPenugasan, String petugasQC) {
        String query;

        List<SQII_PELAKSANAAN> listData = new ArrayList<SQII_PELAKSANAAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);

        query = "SELECT * FROM SQII_PELAKSANAAN WHERE TGL_PENUGASAN = '" + tglPenugasan + "' AND PETUGAS_QC = '" + petugasQC + "'";

        Cursor cursor = db.rawQuery(query, null);
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
                item.setURUT_FOTO(cursor.getFloat(9));
                item.setJENIS_PENUGASAN(cursor.getString(10));
                item.setTGL_PELAKSANAAN(cursor.getString(11));
                item.setPETUGAS_QC(cursor.getString(12));
                item.setPENGAWAS(cursor.getString(13));
                item.setSM(cursor.getString(14));
                item.setSTATUS_DEFECT(cursor.getString(15));
                item.setSTATUS_PEKERJAAN(cursor.getString(16));
                item.setCATATAN(cursor.getString(17));
                item.setFLAG_UPLOAD(cursor.getString(18));
                item.setTGL_UPLOAD(cursor.getString(19));
                item.setPATH_FOTO_DENAH(cursor.getString(20));
                item.setSRC_FOTO_DENAH(cursor.getString(21));
                item.setPATH_FOTO_DEFECT(cursor.getString(22));
                item.setSRC_FOTO_DEFECT(cursor.getString(23));
                item.setLAMA_PERBAIKAN(cursor.getFloat(24));
                item.setTGL_ENTRY_LAMA_PERBAIKAN(cursor.getString(25));
                item.setTGL_JATUH_TEMPO_PERBAIKAN(cursor.getString(26));
                item.setFLAG_AKTIF(cursor.getString(27));
                item.setUSER_AKTIF(cursor.getString(28));
                item.setTGL_AKTIF(cursor.getString(29));
                item.setALASAN_AKTIF(cursor.getString(30));
                item.setFLAG_BATAL(cursor.getString(31));
                item.setUSER_BATAL(cursor.getString(32));
                item.setTGL_BATAL(cursor.getString(33));
                item.setALASAN_BATAL(cursor.getString(34));
                item.setSTATUS_SIMPAN(cursor.getString(35));
                item.setUSER_ENTRY(cursor.getString(36));
                item.setTGL_ENTRY(cursor.getString(37));
                item.setUSER_UPDATE(cursor.getString(38));
                item.setTGL_UPDATE(cursor.getString(39));
                item.setPARENT_ROWID(cursor.getFloat(40));
                item.setROWID(cursor.getFloat(41));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertPelaksanaan(SQII_PELAKSANAAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
        values.put("URUT_FOTO", item.getURUT_FOTO());
        values.put("JENIS_PENUGASAN", item.getJENIS_PENUGASAN());
        values.put("TGL_PELAKSANAAN", item.getTGL_PELAKSANAAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("STATUS_DEFECT", item.getSTATUS_DEFECT());
        values.put("STATUS_PEKERJAAN", item.getSTATUS_PEKERJAAN());
        values.put("CATATAN", item.getCATATAN());
        values.put("FLAG_UPLOAD", item.getFLAG_UPLOAD());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("PATH_FOTO_DENAH", item.getPATH_FOTO_DENAH());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("PATH_FOTO_DEFECT", item.getPATH_FOTO_DEFECT());
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
        values.put("STATUS_SIMPAN", item.getSTATUS_SIMPAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        values.put("PARENT_ROWID", item.getROWID());
        values.put("ROWID", item.getROWID());

        long id = db.insert("SQII_PELAKSANAAN", null, values);
        db.close();

        return id;
    }

    public boolean updatePelaksanaan(SQII_PELAKSANAAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
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
        values.put("URUT_FOTO", item.getURUT_FOTO());
        values.put("JENIS_PENUGASAN", item.getJENIS_PENUGASAN());
        values.put("TGL_PELAKSANAAN", item.getTGL_PELAKSANAAN());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("STATUS_DEFECT", item.getSTATUS_DEFECT());
        values.put("STATUS_PEKERJAAN", item.getSTATUS_PEKERJAAN());
        values.put("CATATAN", item.getCATATAN());
        values.put("FLAG_UPLOAD", item.getFLAG_UPLOAD());
        values.put("TGL_UPLOAD", item.getTGL_UPLOAD());
        values.put("PATH_FOTO_DENAH", item.getPATH_FOTO_DENAH());
        values.put("SRC_FOTO_DENAH", item.getSRC_FOTO_DENAH());
        values.put("PATH_FOTO_DEFECT", item.getPATH_FOTO_DEFECT());
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
        values.put("STATUS_SIMPAN", item.getSTATUS_SIMPAN());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());
        values.put("PARENT_ROWID", item.getROWID());
        values.put("ROWID", item.getROWID());

        String filter = "NO_PENUGASAN = " + item.getNO_PENUGASAN() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR() + " AND " +
                "KD_JENIS =" + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_IEM_DEFECT =" + item.getKD_ITEM_DEFECT() + " AND " +
                "KD_LANTAI =" + item.getKD_LANTAI() + " AND " +
                "URUT_PELAKSANAAN =" + item.getURUT_PELAKSANAAN() + " AND " +
                "URUT_FOTO =" + item.getURUT_FOTO();

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
                "URUT_PELAKSANAAN =" + item.getURUT_PELAKSANAAN() + " AND " +
                "URUT_FOTO =" + item.getURUT_FOTO();

        int affected_rows = db.delete("SQII_PELAKSANAAN", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_PENUGASAN*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_PENUGASAN> getAllPenugasan() {
        List<SQII_PENUGASAN> listData = new ArrayList<SQII_PENUGASAN>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PENUGASAN", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PENUGASAN item = new SQII_PENUGASAN();

                item.setNO_PENUGASAN(cursor.getString(0));
                item.setTGL_PENUGASAN(cursor.getString(1));
                item.setKD_KAWASAN(cursor.getString(2));
                item.setKD_CLUSTER(cursor.getString(3));
                item.setPETUGAS_QC(cursor.getString(4));
                item.setDESKRIPSI(cursor.getString(5));
                item.setSTATUS_SELESAI(cursor.getString(6));
                item.setFLAG_AKTIF(cursor.getString(7));
                item.setFLAG_BATAL(cursor.getString(8));
                item.setUSER_BATAL(cursor.getString(9));
                item.setTGL_BATAL(cursor.getString(10));
                item.setALASAN_BATAL(cursor.getString(11));
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

    public long insertPenugasan(SQII_PENUGASAN item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("TGL_PENUGASAN", item.getTGL_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("STATUS_SELESAI", item.getSTATUS_SELESAI());
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("TGL_PENUGASAN", item.getTGL_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("STATUS_SELESAI", item.getSTATUS_SELESAI());
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
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_PENUGASAN_DETIL", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_PENUGASAN_DETIL item = new SQII_PENUGASAN_DETIL();

                item.setNO_PENUGASAN(cursor.getString(0));
                item.setKD_KAWASAN(cursor.getString(1));
                item.setBLOK(cursor.getString(2));
                item.setNOMOR(cursor.getString(3));
                item.setKD_CLUSTER(cursor.getString(4));
                item.setPENGAWAS(cursor.getString(5));
                item.setSM(cursor.getString(6));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertPenugasanDetil(SQII_PENUGASAN_DETIL item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());

        long id = db.insert("SQII_PENUGASAN_DETIL", null, values);
        db.close();

        return id;
    }

    public boolean updatePenugasanDetil(SQII_PENUGASAN_DETIL item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_PENUGASAN", item.getNO_PENUGASAN());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
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

    /*LIST FUNCTION TABLE SQII_STOK*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_STOK> getAllStok() {
        List<SQII_STOK> listData = new ArrayList<SQII_STOK>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_STOK", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_STOK item = new SQII_STOK();

                item.setKD_KAWASAN(cursor.getString(0));
                item.setBLOK(cursor.getString(1));
                item.setNOMOR(cursor.getString(2));
                item.setKD_CLUSTER(cursor.getString(3));
                item.setSTOK_ID(cursor.getFloat(4));
                item.setKD_KONTRAKTOR(cursor.getString(5));
                item.setKD_JENIS(cursor.getString(6));
                item.setKD_TIPE(cursor.getString(7));
                item.setPENGAWAS(cursor.getString(8));
                item.setSM(cursor.getString(9));
                item.setPETUGAS_QC(cursor.getString(10));
                item.setDESKRIPSI(cursor.getString(11));
                item.setFLAG_AKTIF(cursor.getString(12));
                item.setUSER_ENTRY(cursor.getString(13));
                item.setTGL_ENTRY(cursor.getString(14));
                item.setUSER_UPDATE(cursor.getString(15));
                item.setTGL_UPDATE(cursor.getString(16));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertStok(SQII_STOK item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("STOK_ID", item.getSTOK_ID());
        values.put("KD_KONTRAKTOR", item.getKD_KONTRAKTOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_STOK", null, values);
        db.close();

        return id;
    }

    public boolean updateStok(SQII_STOK item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("BLOK", item.getBLOK());
        values.put("NOMOR", item.getNOMOR());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("STOK_ID", item.getSTOK_ID());
        values.put("KD_KONTRAKTOR", item.getKD_KONTRAKTOR());
        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("PENGAWAS", item.getPENGAWAS());
        values.put("SM", item.getSM());
        values.put("PETUGAS_QC", item.getPETUGAS_QC());
        values.put("DESKRIPSI", item.getDESKRIPSI());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_KAWASAN = " + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR();

        int affected_rows = db.update("SQII_STOK", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteStok(SQII_STOK item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_KAWASAN = " + item.getKD_KAWASAN() + " AND " +
                "BLOK =" + item.getBLOK() + " AND " +
                "NOMOR =" + item.getNOMOR();

        int affected_rows = db.delete("SQII_STOK", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_TIPE_RUMAH*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_TIPE_RUMAH> getAllTipeRumah() {
        List<SQII_TIPE_RUMAH> listData = new ArrayList<SQII_TIPE_RUMAH>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_TIPE_RUMAH", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_TIPE_RUMAH item = new SQII_TIPE_RUMAH();

                item.setKD_JENIS(cursor.getString(0));
                item.setKD_TIPE(cursor.getString(1));
                item.setKD_KAWASAN(cursor.getString(2));
                item.setKD_CLUSTER(cursor.getString(3));
                item.setNM_TIPE(cursor.getString(4));
                item.setFLAG_AKTIF(cursor.getString(5));
                item.setUSER_ENTRY(cursor.getString(6));
                item.setTGL_ENTRY(cursor.getString(7));
                item.setUSER_UPDATE(cursor.getString(8));
                item.setTGL_UPDATE(cursor.getString(9));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertTipeRumah(SQII_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("NM_TIPE", item.getNM_TIPE());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_TIPE_RUMAH", null, values);
        db.close();

        return id;
    }

    public boolean updateTipeRumah(SQII_TIPE_RUMAH item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("KD_JENIS", item.getKD_JENIS());
        values.put("KD_TIPE", item.getKD_TIPE());
        values.put("KD_KAWASAN", item.getKD_KAWASAN());
        values.put("KD_CLUSTER", item.getKD_CLUSTER());
        values.put("NM_TIPE", item.getNM_TIPE());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "KD_JENIS = " + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.update("SQII_TIPE_RUMAH", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteTipeRumah(SQII_TIPE_RUMAH item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "KD_JENIS = " + item.getKD_JENIS() + " AND " +
                "KD_TIPE =" + item.getKD_TIPE() + " AND " +
                "KD_KAWASAN =" + item.getKD_KAWASAN();

        int affected_rows = db.delete("SQII_TIPE_RUMAH", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*LIST FUNCTION TABLE SQII_USER*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    /*-----------------------------------------*/
    public List<SQII_USER> getUser(String nik) {
        List<SQII_USER> listData = new ArrayList<SQII_USER>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_USER WHERE NO_INDUK LIKE '%" + nik + "%'", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_USER item = new SQII_USER();

                item.setNO_INDUK(cursor.getString(0));
                item.setNAMA(cursor.getString(1));
                item.setPASSWORD(cursor.getString(2));
                item.setFLAG_PETUGAS_ADMIN(cursor.getString(3));
                item.setFLAG_SM(cursor.getString(4));
                item.setFLAG_PETUGAS_QC(cursor.getString(5));
                item.setFLAG_PENGAWAS(cursor.getString(6));
                item.setFLAG_AKTIF(cursor.getString(7));
                item.setUSER_ENTRY(cursor.getString(8));
                item.setTGL_ENTRY(cursor.getString(9));
                item.setUSER_UPDATE(cursor.getString(10));
                item.setTGL_UPDATE(cursor.getString(11));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public List<SQII_USER> getAllUser() {
        List<SQII_USER> listData = new ArrayList<SQII_USER>();
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_USER", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SQII_USER item = new SQII_USER();

                item.setNO_INDUK(cursor.getString(0));
                item.setNAMA(cursor.getString(1));
                item.setPASSWORD(cursor.getString(2));
                item.setFLAG_PETUGAS_ADMIN(cursor.getString(3));
                item.setFLAG_SM(cursor.getString(4));
                item.setFLAG_PETUGAS_QC(cursor.getString(5));
                item.setFLAG_PENGAWAS(cursor.getString(6));
                item.setFLAG_AKTIF(cursor.getString(7));
                item.setUSER_ENTRY(cursor.getString(8));
                item.setTGL_ENTRY(cursor.getString(9));
                item.setUSER_UPDATE(cursor.getString(10));
                item.setTGL_UPDATE(cursor.getString(11));

                listData.add(item);
            }
        }
        cursor.close();
        db.close();

        return listData;
    }

    public long insertUser(SQII_USER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_INDUK", item.getNO_INDUK());
        values.put("NAMA", item.getNAMA());
        values.put("PASSWORD", item.getPASSWORD());
        values.put("FLAG_PETUGAS_ADMIN", item.getFLAG_PETUGAS_ADMIN());
        values.put("FLAG_SM", item.getFLAG_SM());
        values.put("FLAG_PETUGAS_QC", item.getFLAG_PETUGAS_QC());
        values.put("FLAG_PENGAWAS", item.getFLAG_PENGAWAS());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        long id = db.insert("SQII_USER", null, values);
        db.close();

        return id;
    }

    public boolean updateUser(SQII_USER item) {
        /*SQLiteDatabase db = this.getWritableDatabase();*/
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        ContentValues values = new ContentValues();

        values.put("NO_INDUK", item.getNO_INDUK());
        values.put("NAMA", item.getNAMA());
        values.put("PASSWORD", item.getPASSWORD());
        values.put("FLAG_PETUGAS_ADMIN", item.getFLAG_PETUGAS_ADMIN());
        values.put("FLAG_SM", item.getFLAG_SM());
        values.put("FLAG_PETUGAS_QC", item.getFLAG_PETUGAS_QC());
        values.put("FLAG_PENGAWAS", item.getFLAG_PENGAWAS());
        values.put("FLAG_AKTIF", item.getFLAG_AKTIF());
        values.put("USER_ENTRY", item.getUSER_ENTRY());
        values.put("TGL_ENTRY", item.getTGL_ENTRY());
        values.put("USER_UPDATE", item.getUSER_UPDATE());
        values.put("TGL_UPDATE", item.getTGL_UPDATE());

        String filter = "NO_INDUK =" + item.getNO_INDUK();

        int affected_rows = db.update("SQII_USER", values, filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteUser(SQII_USER item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String filter = "NO_INDUK =" + item.getNO_INDUK();

        int affected_rows = db.delete("SQII_USER", filter, null);
        db.close();

        if (affected_rows > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkTabelPenugasan() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_USER", null);
        if (cursor.getCount() > 0) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public boolean checkLogin(String nik, String password) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Configuration.APP_EXTERNAL_DATABASE_DIRECTORY, null);
        Cursor cursor = db.rawQuery("SELECT * FROM SQII_USER WHERE NO_INDUK='" + nik + "' AND PASSWORD='" + password + "'", null);
        if (cursor.getCount() > 0) {
            db.close();
            return true; /* Login Sukses*/
        } else {
            db.close();
            return false; /* Login Gagal*/
        }
    }

}
