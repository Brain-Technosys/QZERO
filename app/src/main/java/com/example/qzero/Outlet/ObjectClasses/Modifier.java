package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 9/24/2015.
 */
public class Modifier {

    String mod_name;
    String mod_price;
    String choice_name;

    public Modifier(String mod_name, String mod_price,String choice_name) {
        this.mod_name = mod_name;
        this.mod_price = mod_price;
        this.choice_name=choice_name;
    }

    public String getMod_name() {
        return mod_name;
    }

    public void setMod_name(String mod_name) {
        this.mod_name = mod_name;
    }

    public String getMod_price() {
        return mod_price;
    }

    public void setMod_price(String mod_price) {
        this.mod_price = mod_price;
    }

    public String getChoice_name() {
        return choice_name;
    }

    public void setChoice_name(String choice_name) {
        this.choice_name = choice_name;
    }
}
