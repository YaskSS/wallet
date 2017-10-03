package com.example.yass.wallet.model;

/**
 * Created by yass on 9/6/17.
 */

public class Vin {
    private  String address;
    private Float value;
    private Long valuesat;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Long getValuesat() {
        return valuesat;
    }

    public void setValuesat(Long valuesat) {
        this.valuesat = valuesat;
    }
}
