package com.example.qzero.Outlet.ObjectClasses;

/**
 * Created by Braintech on 10/19/2015.
 */
public class State {

    int stateId;
    String stateName;

    public State(int stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
