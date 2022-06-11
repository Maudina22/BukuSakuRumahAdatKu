package com.bukusaku.rumah.adat.models;

import java.util.ArrayList;
import java.util.List;

public class Objek {

    private String title, type;
    private int total = 0, available = 0, id;
    private List<Integer> unit = new ArrayList<Integer>();
    public String image;


    public Objek() {
    }

    public Objek(String title, String type, int total, int id, String image) {
        this.title = title;
        this.type = type;
        this.total = total;
        this.id = id;
        this.available=total;
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getUnit() {
        return unit;
    }

    public void setUnit(List<Integer> unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }
}

