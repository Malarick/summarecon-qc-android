package com.summarecon.qcapp.item;

import java.io.File;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanGridItem {

    private File file;
    private int res;
    private Boolean isFile = false;

    public PenugasanGridItem(int res) {
        this.res = res;
        isFile = false;
    }

    public PenugasanGridItem(File file) {
        this.file = file;
        isFile = true;
    }

    public File getFile() {
        return file;
    }

    public int getRes() {
        return res;
    }

    public Boolean getIsFile() {
        return isFile;
    }
}
