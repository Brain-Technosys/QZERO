package com.example.qzero.Outlet.DatabseTable;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Braintech on 10/5/2015.
 */
public class ItemDetails implements Serializable {

    /**
     *  Model class for teacher_details database table
     */
    private static final long serialVersionUID = -222864131214757024L;

    // Primary key defined as an auto generated integer
    // If the database table column name differs than the Model class variable name, the way to map to use columnName
    @DatabaseField(generatedId = true, columnName = "item_id")
    public int itemId;

    // Define a String type field to hold item name
    @DatabaseField(columnName = "item_name")
    public String itemName;

    // Define a String type field to hold item price
    public String item_price;

    // Define a String type field to hold item discount
    public String item_discount;

    // Define a String type field to hold item quantity
    public String item_quantity;

    // Default constructor is needed for the SQLite, so make sure you also have it
    public ItemDetails(){

    }

    //For our own purpose, so it's easier to create a itemdetails object
    public ItemDetails(String itemName,String item_price,String item_discount,String item_quantity){
        this.itemName = itemName;
        this.item_price = item_price;
        this.item_discount = item_discount;
        this.item_quantity = item_quantity;
    }
}
