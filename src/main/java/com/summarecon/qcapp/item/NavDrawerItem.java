package com.summarecon.qcapp.item;

/**
 * Created by arnold on 2/11/13.
 */
public class NavDrawerItem {

    int itemIcon;
    String itemLabel;
    int itemCounter;

    public int getItemIcon() {
        return itemIcon;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public int getItemCounter() {
        return itemCounter;
    }

    public NavDrawerItem(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public NavDrawerItem(int itemIcon, String itemLabel) {
        this.itemIcon = itemIcon;
        this.itemLabel = itemLabel;
    }

    public NavDrawerItem(int itemIcon, String itemLabel, int itemCounter) {
        this.itemIcon = itemIcon;
        this.itemLabel = itemLabel;
        this.itemCounter = itemCounter;

    }
}
