package com.arttelit.chibisov.simplestock.models;

public class Store {
    private  int id;
    private  String name;

    public Store() {
    }

    public Store(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
