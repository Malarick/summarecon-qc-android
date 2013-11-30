package com.summarecon.qcapp.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnold on 9/11/13.
 */
public class PenugasanParentItem {
    private List<PenugasanChildItem> childItemList;
    private String parentItemLbl;
    private String parentTglLbl;
    private String parentBlokLbl;
    private String parentTotalImagesLbl;

    private Integer reqImages;

    public PenugasanParentItem(String parentItemLbl) {
        this.parentItemLbl = parentItemLbl;
        childItemList = new ArrayList<PenugasanChildItem>();
    }

    public PenugasanParentItem(String parentItemLbl, String parentTglLbl, String parentBlokLbl, String parentTotalImagesLbl, Float reqImages) {
        this.parentItemLbl = parentItemLbl;
        this.parentTglLbl = parentTglLbl;
        this.parentBlokLbl = parentBlokLbl;
        this.parentTotalImagesLbl = parentTotalImagesLbl;
        this.reqImages = Integer.valueOf(String.format("%.0f", reqImages));

        childItemList = new ArrayList<PenugasanChildItem>();
    }

    public String getParentItemLbl() {
        return parentItemLbl;
    }

    public String getParentTglLbl() {
        return parentTglLbl;
    }

    public String getParentBlokLbl() {
        return parentBlokLbl;
    }

    public String getParentTotalImagesLbl() {
        return parentTotalImagesLbl;
    }

    public Integer getReqImages() {
        return reqImages;
    }

    public List<PenugasanChildItem> getChildItemList() {
        return childItemList;
    }

}
