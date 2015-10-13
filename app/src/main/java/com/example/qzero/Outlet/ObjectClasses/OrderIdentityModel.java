package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/13/2015.
 */
public class OrderIdentityModel {

    String item_id;
    String outletId;
    String itemCount;

    public OrderIdentityModel(String item_id, String outletId, String itemCount) {
        this.item_id = item_id;
        this.outletId = outletId;
        this.itemCount = itemCount;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
