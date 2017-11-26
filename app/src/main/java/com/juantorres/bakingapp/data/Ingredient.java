package com.juantorres.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by juantorres on 11/6/17.
 */
@Parcel
public class Ingredient {
    public double quantity;
    public String measure;
    @SerializedName("ingredient")
    public String name;

    public Ingredient(){

    }

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
