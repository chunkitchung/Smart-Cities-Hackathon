package com.example.smartcheckout;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Transaction implements Serializable {
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    private String store;
    private LocalDateTime time;
    private double Cost;

    public Transaction(){
        items = new ArrayList<Item>();
    }

    public Transaction(ArrayList<Item> items, LocalDateTime time){
        this.items = items;
        this.time = time;
    }

}
