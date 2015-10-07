package com.example.qzero.Outlet.DatabseTable;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Braintech on 10/5/2015.
 */
public class ModifierDetails implements Serializable {


    /**
     * Model class for student_details database table
     */
    private static final long serialVersionUID = -222864131214757024L;

    public static final String ID_FIELD = "modifier_id";
    public static final String ITEM_ID_FIELD = "item_id";

    // Primary key defined as an auto generated integer
    // If the database table column name differs than the Model class variable name, the way to map to use columnName
    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int modifierId;

    // Define a String type field to hold modifier name
    @DatabaseField(columnName = "modifier_name")
    public String modifierName;

    // Define a String type field to hold modifier name
    @DatabaseField(columnName = "modifier_price")
    public String modifierPrice;


    // Foreign key defined to hold associations
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public ItemDetails item;


    // Default constructor is needed for the SQLite, so make sure you also have it
    public ModifierDetails(){

    }

    //For our own purpose, so it's easier to create a StudentDetails object
    public ModifierDetails(String modifierName,String modifierPrice, ItemDetails item){
        this.modifierName = modifierName;
        this.modifierPrice = modifierPrice;
        this.item=item;

    }
}
