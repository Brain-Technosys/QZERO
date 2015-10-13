package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/13/2015.
 */
public class OrderItemStatusModel {

    String item_id;
    String quantity;
    Boolean isModifier;

    public OrderItemStatusModel(String item_id, String quantity, Boolean isModifier) {
        this.item_id = item_id;
        this.quantity = quantity;
        this.isModifier = isModifier;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsModifier() {
        return isModifier;
    }

    public void setIsModifier(Boolean isModifier) {
        this.isModifier = isModifier;
    }
}
