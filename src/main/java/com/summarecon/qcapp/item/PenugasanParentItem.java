package com.summarecon.qcapp.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanParentItem {
    private List<PenugasanChildItem> childItemList;
    private String parentItemLbl;

    public PenugasanParentItem(String parentItemLbl) {
        this.parentItemLbl = parentItemLbl;
        childItemList = new ArrayList<PenugasanChildItem>();

    }

    public String getParentItemLbl() {
        return parentItemLbl;
    }

    public List<PenugasanChildItem> getChildItemList() {
        return childItemList;
    }

}
