package com.example.qzero.Outlet.ObjectClasses;


public class ChoiceGroup {

    String choice_name;

    Boolean isComplusory;

    public ChoiceGroup(String choice_name, Boolean isComplusory)
    {
        this.choice_name = choice_name;
        this.isComplusory = isComplusory;
    }

    public String getChoice_name() {
        return choice_name;
    }

    public void setChoice_name(String choice_name) {
        this.choice_name = choice_name;
    }

    public Boolean getIsComplusory() {
        return isComplusory;
    }

    public void setIsComplusory(Boolean isComplusory) {
        this.isComplusory = isComplusory;
    }
}
