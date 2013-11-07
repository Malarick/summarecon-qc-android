package com.summarecon.qcapp.item;

/**
 * Created by arnold on 7/11/13.
 */
public class NotificationsItem {
    String itemLabel;
    int itemCounter;
    int itemIcon;

    public String getItemLabel() {
        return itemLabel;
    }

    public int getItemCounter() {
        return itemCounter;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public NotificationsItem(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public NotificationsItem(String itemLabel, int itemIcon) {
        this.itemLabel = itemLabel;
        this.itemIcon = itemIcon;
    }

    public NotificationsItem(String itemLabel, int itemCounter, int itemIcon) {
        this.itemLabel = itemLabel;
        this.itemCounter = itemCounter;
        this.itemIcon = itemIcon;

    }
}
