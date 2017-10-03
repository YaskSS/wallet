package com.example.yass.wallet.model;

import java.util.List;

/**
 * Created by yass on 9/6/17.
 */

public class Vout {
    private List<String> addresses = null;
    private String value;

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
