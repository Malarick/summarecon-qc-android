package com.summarecon.qcapp.item;

/**
 * Created by arnold on 31/10/13.
 */
public class SpinnerListItem {
    private String key_item;
    private String lbl_item;

    public SpinnerListItem(String lbl_item) {
        this.lbl_item = lbl_item;
    }

    public SpinnerListItem(String key_item, String lbl_item) {
        this.key_item = key_item;
        this.lbl_item = lbl_item;
    }

    public String getKey_item() {
        return key_item;
    }

    public String getLbl_item() {
        return lbl_item;
    }
}
