package com.kv.dailyaccountbook.transaction;

import java.io.Serializable;

public class TransModel implements Serializable {
    private String user_id;
    private String amount;
    private String type;
    private String paymentType;
    private String summery;
    private String date;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getSummery() {
        return summery;
    }

    public void setSummery(String summery) {
        this.summery = summery;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
