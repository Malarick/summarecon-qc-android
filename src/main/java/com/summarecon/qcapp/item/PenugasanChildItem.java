package com.summarecon.qcapp.item;

import com.summarecon.qcapp.db.SQII_PELAKSANAAN;

import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanChildItem {
    String path;
    List<SQII_PELAKSANAAN> list;

    public PenugasanChildItem() {
    }

    public PenugasanChildItem(String path) {

        this.path = path;
    }

    public PenugasanChildItem(List list) {
        this.list = list;

    }

    public String getPath() {
        return path;
    }

    public List<SQII_PELAKSANAAN> getList() {
        return list;
    }
}
