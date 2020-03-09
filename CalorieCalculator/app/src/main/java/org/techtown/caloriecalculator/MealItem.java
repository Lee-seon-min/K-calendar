package org.techtown.caloriecalculator;

public class MealItem {
    private String kind;
    private int kcal;
    public MealItem(String kind,int kcal){
        this.kind=kind;
        this.kcal=kcal;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }
}
