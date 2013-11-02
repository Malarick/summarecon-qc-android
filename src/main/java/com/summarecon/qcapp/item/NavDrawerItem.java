package com.summarecon.qcapp.item;

/**
 * Created by arnold on 2/11/13.
 */
public class NavDrawerItem {

    int img_item_icon;
    String txt_item_label;
    int img_item_counter;

    public int getImg_item_icon() {
        return img_item_icon;
    }

    public String getTxt_item_label() {
        return txt_item_label;
    }

    public int getImg_item_counter() {
        return img_item_counter;
    }

    public NavDrawerItem(String txt_item_label) {
        this.txt_item_label = txt_item_label;
    }

    public NavDrawerItem(String txt_item_label, int img_item_icon) {
        this.txt_item_label = txt_item_label;

        this.img_item_icon = img_item_icon;
    }

    public NavDrawerItem(int img_item_icon, String txt_item_label, int img_item_counter) {
        this.img_item_icon = img_item_icon;
        this.txt_item_label = txt_item_label;
        this.img_item_counter = img_item_counter;

    }
}
