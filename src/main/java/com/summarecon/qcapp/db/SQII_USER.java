package com.summarecon.qcapp.db;

/**
 * Created by wahyu_wibisana on 11/9/13.
 */
public class SQII_USER {
    private String NO_INDUK;
    private String NAMA;
    private String PASSWORD;
    private String FLAG_PETUGAS_ADMIN;
    private String FLAG_SM;
    private String FLAG_PETUGAS_QC;
    private String FLAG_PENGAWAS;
    private String FLAG_AKTIF;
    private String USER_ENTRY;
    private String TGL_ENTRY;
    private String USER_UPDATE;
    private String TGL_UPDATE;

    public String getNO_INDUK() {
        return NO_INDUK;
    }

    public void setNO_INDUK(String NO_INDUK) {
        this.NO_INDUK = NO_INDUK;
    }

    public String getNAMA() {
        return NAMA;
    }

    public void setNAMA(String NAMA) {
        this.NAMA = NAMA;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getFLAG_PETUGAS_ADMIN() {
        return FLAG_PETUGAS_ADMIN;
    }

    public void setFLAG_PETUGAS_ADMIN(String FLAG_PETUGAS_ADMIN) {
        this.FLAG_PETUGAS_ADMIN = FLAG_PETUGAS_ADMIN;
    }

    public String getFLAG_SM() {
        return FLAG_SM;
    }

    public void setFLAG_SM(String FLAG_SM) {
        this.FLAG_SM = FLAG_SM;
    }

    public String getFLAG_PETUGAS_QC() {
        return FLAG_PETUGAS_QC;
    }

    public void setFLAG_PETUGAS_QC(String FLAG_PETUGAS_QC) {
        this.FLAG_PETUGAS_QC = FLAG_PETUGAS_QC;
    }

    public String getFLAG_PENGAWAS() {
        return FLAG_PENGAWAS;
    }

    public void setFLAG_PENGAWAS(String FLAG_PENGAWAS) {
        this.FLAG_PENGAWAS = FLAG_PENGAWAS;
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
