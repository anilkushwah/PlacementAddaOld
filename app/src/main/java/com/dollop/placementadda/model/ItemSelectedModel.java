package com.dollop.placementadda.model;

/**
 * Created by Kasim on 22-01-2018.
 */

public class ItemSelectedModel {

    String selectedListNo;

    public String getSelectedListNo() {
        return selectedListNo;
    }

    public void setSelectedListNo(String selectedListNo) {
        this.selectedListNo = selectedListNo;
    }

    private boolean selected = false;

    boolean attemped = false;

    public boolean isAttemped() {
        return attemped;
    }

    public void setAttemped(boolean attemped) {
        this.attemped = attemped;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
