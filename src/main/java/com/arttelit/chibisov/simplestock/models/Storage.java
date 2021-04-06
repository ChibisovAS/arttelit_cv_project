package com.arttelit.chibisov.simplestock.models;

import java.util.List;

public class Storage {

    private  String storeName;
    private List<Product> products;

    public Storage() {
    }

    public Storage(String storeName, List<Product> products) {
        this.storeName = storeName;
        this.products = products;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
