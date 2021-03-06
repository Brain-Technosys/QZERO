package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/7/2015.
 */
public class DbItems {

    String item_id;
    String item_name;
    String item_price;
    String discount_price;
    String item_image;

    int count;

    public DbItems(String item_id,String item_name, String item_price, String discount_price,String item_image,int count) {
        this.item_id=item_id;
        this.item_name = item_name;
        this.item_price = item_price;
        this.discount_price = discount_price;
        this.item_image=item_image;
        this.count=count;
    }


    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
