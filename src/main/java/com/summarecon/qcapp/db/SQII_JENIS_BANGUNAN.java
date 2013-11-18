package com.summarecon.qcapp.db;

/**
 * Created by wahyu_wibisana on 11/9/13.
 */
public class SQII_JENIS_BANGUNAN {
    private String KD_JENIS;
    private String NM_JENIS;
    private String FLAG_AKTIF;
    private String USER_ENTRY;
    private String TGL_ENTRY;
    private String USER_UPDATE;
    private String TGL_UPDATE;

    public String getKD_JENIS() {
        return KD_JENIS;
    }

    public void setKD_JENIS(String KD_JENIS) {
        this.KD_JENIS = KD_JENIS;
    }

    public String getNM_JENIS() {
        return NM_JENIS;
    }

    public void setNM_JENIS(String NM_JENIS) {
        this.NM_JENIS = NM_JENIS;
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
