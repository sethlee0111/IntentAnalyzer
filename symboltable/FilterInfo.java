package com.sethlee0111.basicintentactivity.symboltable;


import java.util.ArrayList;

public class FilterInfo {
    private ArrayList<String> action = new ArrayList<>();
    private ArrayList<String> category = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();

    public ArrayList<String> getAction() {
        return action;
    }

    public void addAction(String action) {
        this.action.add(action);
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void addCategory(String category) {
        this.category.add(category);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void addData(String data) {
        this.data.add(data);
    }

    @Override
    public String toString() {
        return action + " / " + category + " / " + data;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}