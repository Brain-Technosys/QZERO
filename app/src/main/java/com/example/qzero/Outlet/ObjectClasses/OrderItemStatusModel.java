package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/13/2015.
 */
public class OrderItemStatusModel {

    String itemId;
    String mod_name;
    String quantity;
    String mod_price;
    String mod_id;
    String itemCode;

    Double itemPrice;
    Double discountAmt;

    Boolean isModifier;


    public OrderItemStatusModel(String itemCode,String itemId, String mod_name, String quantity, Boolean isModifier, String mod_price, String mod_id,Double itemPrice,Double discountAmt) {

       this.itemCode=itemCode;
        this.itemId = itemId;
        this.mod_name = mod_name;
        this.quantity = quantity;
        this.isModifier = isModifier;
        this.mod_price = mod_price;
        this.mod_id = mod_id;

        this.discountAmt=discountAmt;
        this.itemPrice=itemPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }
}
