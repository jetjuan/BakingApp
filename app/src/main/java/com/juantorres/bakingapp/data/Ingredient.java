package com.juantorres.bakingapp.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juantorres on 11/6/17.
 */

public class Ingredient {
    private double quantity;
    private String measure;
    @SerializedName("ingredient")
    private String name;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
