package com.summarecon.qcapp.db;

/**
 * Created by wahyu_wibisana on 11/9/13.
 */
public class SQII_CATATAN {
    private Float KD_CATATAN;
    private Float KD_ITEM_DEFECT;
    private String DESKRIPSI;
    private String FLAG_AKTIF;
    private String USER_ENTRY;
    private String TGL_ENTRY;
    private String USER_UPDATE;
    private String TGL_UPDATE;

    public Float getKD_CATATAN() {
        return KD_CATATAN;
    }

    public void setKD_CATATAN(Float KD_CATATAN) {
        this.KD_CATATAN = KD_CATATAN;
    }

    public Float getKD_ITEM_DEFECT() {
        return KD_ITEM_DEFECT;
    }

    public void setKD_ITEM_DEFECT(Float KD_ITEM_DEFECT) {
        this.KD_ITEM_DEFECT = KD_ITEM_DEFECT;
    }

    public String getDESKRIPSI() {
        return DESKRIPSI;
    }

    public void setDESKRIPSI(String DESKRIPSI) {
        this.DESKRIPSI = DESKRIPSI;
    }

    public String getFLAG_AKTIF() {
        return FLAG_AKTIF;
    }

    public void setFLAG_AKTIF(String FLAG_AKTIF) {
        this.FLAG_AKTIF = FLAG_AKTIF;
    }

    public String getUSER_ENTRY() {
        return USER_ENTRY;
    }

    public void setUSER_ENTRY(String USER_ENTRY) {
        this.USER_ENTRY = USER_ENTRY;
    }

    public String getTGL_ENTRY() {
        return TGL_ENTRY;
    }

    public void setTGL_ENTRY(String TGL_ENTRY) {
        this.TGL_ENTRY = TGL_ENTRY;
    }

    public String getUSER_UPDATE() {
        return USER_UPDATE;
    }

    public void setUSER_UPDATE(String USER_UPDATE) {
        this.USER_UPDATE = USER_UPDATE;
    }

    public String getTGL_UPDATE() {
        return TGL_UPDATE;
    }

    public void setTGL_UPDATE(String TGL_UPDATE) {
        this.TGL_UPDATE = TGL_UPDATE;
    }
}
