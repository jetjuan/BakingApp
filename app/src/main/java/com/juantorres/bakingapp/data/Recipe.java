package com.juantorres.bakingapp.data;


import org.parceler.Parcel;

import java.util.List;

/**
 * Created by juantorres on 11/6/17.
 */
//TODO latest todo: use Parcelabler Library to convert Recipe, Steps and ingredients to Parcelable, then pass them as Extras to Detail Activity, then display all the ingredients
@Parcel
public class Recipe {
    public long id;
    public String name;
    public int servings;
    public String image;
    public List<Ingredient> ingredients;
    public List<Step> steps;

    public Recipe(){

    }

    public String getName(){
        return this.name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getIdAsString(){
        return String.valueOf(this.id);
    }
}
