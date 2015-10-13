package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/13/2015.
 */
public class OrderItemModifierModel {

    String item_id;
    String modifier_id;
    Double modifier_price;

    public OrderItemModifierModel(String item_id, String modifier_id, Double modifier_price) {
        this.item_id = item_id;
        this.modifier_id = modifier_id;
        this.modifier_price = modifier_price;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getModifier_id() {
        return modifier_id;
    }

    public void setModifier_id(String modifier_id) {
        this.modifier_id = modifier_id;
    }

    public Double getModifier_price() {
        return modifier_price;
    }

    public void setModifier_price(Double modifier_price) {
        this.modifier_price = modifier_price;
    }

}
