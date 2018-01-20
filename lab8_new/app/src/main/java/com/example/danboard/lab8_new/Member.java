package com.example.danboard.lab8_new;

/**
 * Created by Danboard on 17-12-11.
 */

public class Member {
    private Integer id;
    private String name;
    private String birth;
    private String gift;

    public Member(String name, String birth, String gift) {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBirth() {
        return birth;
    }
    public String getGift() {
        return gift;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public void setGift(String gift) {
        this.gift = gift;
    }
}
