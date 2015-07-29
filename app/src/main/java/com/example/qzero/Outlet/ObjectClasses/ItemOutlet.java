package com.example.qzero.Outlet.ObjectClasses;

import java.io.Serializable;

/**
 * Created by Braintech on 7/28/2015.
 */
public class ItemOutlet implements Serializable {

    String item_id;
    String name;
    String price;
    String description;
    String subCategoryId;

    public ItemOutlet(String item_id, String name, String price, String description, String subCategoryId) {
        this.item_id = item_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.subCategoryId = subCategoryId;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
