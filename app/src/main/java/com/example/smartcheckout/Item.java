package com.example.smartcheckout;

import java.io.Serializable;

class Item implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    private double cost;

    public Item(String name, double cost){
        this.name = name;
        this.cost = cost;
    }
    public Item(){
        name = "";
        cost = 0;
    }

}
