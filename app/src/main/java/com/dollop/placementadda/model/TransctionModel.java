package com.dollop.placementadda.model;

public class TransctionModel {
    String Transction_type,Transction_date,Transction_amount,Transction_id,dateChangeCheck;

    public String getTransction_type() {
        return Transction_type;
    }

    public void setTransction_type(String transction_type) {
        Transction_type = transction_type;
    }

    public String getTransction_date() {
        return Transction_date;
    }

    public void setTransction_date(String transction_date) {
        Transction_date = transction_date;
    }

    public String getTransction_amount() {
        return Transction_amount;
    }

    public void setTransction_amount(String transction_amount) {
        Transction_amount = transction_amount;
    }

    public String getTransction_id() {
        return Transction_id;
    }

    public void setTransction_id(String transction_id) {
        Transction_id = transction_id;
    }

    public String getDateChangeCheck() {
        return dateChangeCheck;
    }

    public void setDateChangeCheck(String dateChangeCheck) {
        this.dateChangeCheck = dateChangeCheck;
    }
}
