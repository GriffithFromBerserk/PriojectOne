package com.example.gifts;

import java.util.List;

public class Gift {
    private int id;
    private String name;
    private String gift;
    private List<Integer> ageIds;
    private String gender;

    public Gift(int id, String name, String gift, List<Integer> ageIds, String gender) {
        this.id = id;
        this.name = name;
        this.gift = gift;
        this.ageIds = ageIds;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGift() {
        return gift;
    }

    public List<Integer> getAgeIds() {
        return ageIds;
    }

    public String getGender() {
        return gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public void setAgeIds(List<Integer> ageIds) {
        this.ageIds = ageIds;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}