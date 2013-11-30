package com.summarecon.qcapp.item;

import com.summarecon.qcapp.db.SQII_PELAKSANAAN;

import java.io.Serializable;
import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanChildItem {
    String path;
    SQII_PELAKSANAAN parent;
    List<SQII_PELAKSANAAN> listChild;

    public PenugasanChildItem() {
    }

    public PenugasanChildItem(String path) {

        this.path = path;
    }

    public PenugasanChildItem(SQII_PELAKSANAAN parent, List listChild) {
        this.parent = parent;
        this.listChild = listChild;
    }

    public String getPath() {
        return path;
    }

    public SQII_PELAKSANAAN getParent() {
        return parent;
    }

    public List<SQII_PELAKSANAAN> getListChild() {
        return listChild;
    }
}
