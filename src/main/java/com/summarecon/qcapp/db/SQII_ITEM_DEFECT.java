package com.summarecon.qcapp.db;

/**
 * Created by wahyu_wibisana on 11/9/13.
 */
public class SQII_ITEM_DEFECT {
    private Float KD_ITEM_DEFECT;
    private String NM_ITEM_DEFECT;
    private Float KD_KATEGORI_DEFECT;
    private String FLAG_AKTIF;
    private String USER_ENTRY;
    private String TGL_ENTRY;
    private String USER_UPDATE;
    private String TGL_UPDATE;

    public Float getKD_ITEM_DEFECT() {
        return KD_ITEM_DEFECT;
    }

    public void setKD_ITEM_DEFECT(Float KD_ITEM_DEFECT) {
        this.KD_ITEM_DEFECT = KD_ITEM_DEFECT;
    }

    public String getNM_ITEM_DEFECT() {
        return NM_ITEM_DEFECT;
    }

    public void setNM_ITEM_DEFECT(String NM_ITEM_DEFECT) {
        this.NM_ITEM_DEFECT = NM_ITEM_DEFECT;
    }

    public Float getKD_KATEGORI_DEFECT() {
        return KD_KATEGORI_DEFECT;
    }

    public void setKD_KATEGORI_DEFECT(Float KD_KATEGORI_DEFECT) {
        this.KD_KATEGORI_DEFECT = KD_KATEGORI_DEFECT;
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
