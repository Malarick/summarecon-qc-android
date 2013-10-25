package com.summarecon.qcapp.db;

/*Created By Wahyu Wibisana on 10/19/2013*/

public class SQII_PENUGASAN_DETIL {
    private String NO_PENUGASAN;
    private String KD_KAWASAN;
    private String BLOK;
    private String NOMOR;
    private String PENGAWAS;
    private String SM;

    public String getKD_KAWASAN() {
        return KD_KAWASAN;
    }

    public void setKD_KAWASAN(String KD_KAWASAN) {
        this.KD_KAWASAN = KD_KAWASAN;
    }

    public String getNO_PENUGASAN() {
        return NO_PENUGASAN;
    }

    public void setNO_PENUGASAN(String NO_PENUGASAN) {
        this.NO_PENUGASAN = NO_PENUGASAN;
    }

    public String getBLOK() {
        return BLOK;
    }

    public void setBLOK(String BLOK) {
        this.BLOK = BLOK;
    }

    public String getNOMOR() {
        return NOMOR;
    }

    public void setNOMOR(String NOMOR) {
        this.NOMOR = NOMOR;
    }

    public String getPENGAWAS() {
        return PENGAWAS;
    }

    public void setPENGAWAS(String PENGAWAS) {
        this.PENGAWAS = PENGAWAS;
    }

    public String getSM() {
        return SM;
    }

    public void setSM(String SM) {
        this.SM = SM;
    }
}
