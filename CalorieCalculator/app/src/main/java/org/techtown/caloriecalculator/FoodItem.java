package org.techtown.caloriecalculator;

import android.util.Log;

public class FoodItem {
    private String name;
    private double kcal;
    private double gram;
    private double carbor;
    private double protein;
    private double fat;

    public FoodItem(String name, double gram, double kcal, double carbor, double protein, double fat) {
        this.name = name;
        this.gram=gram;
        this.kcal = kcal;
        this.carbor = carbor;
        this.protein = protein;
        this.fat = fat;
    }
    public FoodItem(){
        this.name = null;
        this.gram=0;
        this.kcal = 0;
        this.carbor = 0;
        this.protein = 0;
        this.fat = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Log.d("myNNNNNNNNNNNNNNNNNNNNN",name);
        this.name = name;
    }

    public double getGram() {
        return gram;
    }

    public void setGram(double gram) {
        this.gram = gram;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getCarbor() {
        return carbor;
    }

    public void setCarbor(double carbor) {
        this.carbor = carbor;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }
}
