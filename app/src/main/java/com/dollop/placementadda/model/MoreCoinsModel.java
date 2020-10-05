package com.dollop.placementadda.model;

/**
 * Created by Kasim on 03-04-2018.
 */

public class MoreCoinsModel {
    String coins;

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getRupees() {
        return rupees;
    }

    public void setRupees(String rupees) {
        this.rupees = rupees;
    }

    public MoreCoinsModel(String coins, String rupees) {

        this.coins = coins;
        this.rupees = rupees;
    }

    String rupees;

}