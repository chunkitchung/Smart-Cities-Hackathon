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

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    private String store;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    private String timeString;
    private double Cost;

    public Transaction(){
        items = new ArrayList<Item>();
    }

    public Transaction(ArrayList<Item> items, String time){
        this.items = items;
        this.timeString = time;
    }

}
