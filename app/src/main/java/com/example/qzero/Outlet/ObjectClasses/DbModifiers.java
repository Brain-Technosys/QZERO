package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/7/2015.
 */
public class DbModifiers {

    String item_name;

    String quantity;
    String modifier_name;
    String modifier_price;

    public DbModifiers(String item_name, String quantity, String modifier_name, String modifier_price) {
        this.item_name = item_name;
        this.quantity = quantity;
        this.modifier_name = modifier_name;
        this.modifier_price = modifier_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModifier_name() {
        return modifier_name;
    }

    public void setModifier_name(String modifier_name) {
        this.modifier_name = modifier_name;
    }

    public String getModifier_price() {
        return modifier_price;
    }

    public void setModifier_price(String modifier_price) {
        this.modifier_price = modifier_price;
    }
}
