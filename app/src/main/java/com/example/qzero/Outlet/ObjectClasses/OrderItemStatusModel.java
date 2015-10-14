package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/13/2015.
 */
public class OrderItemStatusModel {

    String mod_name;
    String quantity;
    String mod_price;
    String mod_id;
    Boolean isModifier;

    public OrderItemStatusModel(String mod_name,String quantity, Boolean isModifier,String mod_price,String mod_id) {

        this.mod_name=mod_name;
        this.quantity = quantity;
        this.isModifier = isModifier;
        this.mod_price=mod_price;
        this.mod_id=mod_id;
    }

    public String getMod_name() {
        return mod_name;
    }

    public void setMod_name(String mod_name) {
        this.mod_name = mod_name;
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

    public String getMod_price() {
        return mod_price;
    }

    public void setMod_price(String mod_price) {
        this.mod_price = mod_price;
    }

    public String getMod_id() {
        return mod_id;
    }

    public void setMod_id(String mod_id) {
        this.mod_id = mod_id;
    }
}
