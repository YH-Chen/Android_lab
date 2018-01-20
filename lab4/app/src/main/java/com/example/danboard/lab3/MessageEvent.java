package com.example.danboard.lab3;

/**
 * Created by Danboard on 2017/10/30.
 * 事件类，传递商品信息
 */

public class MessageEvent {

    private String name;
    private String price;

    public MessageEvent(String name, String price) {
        super();
        this.name = name;
        this.price= price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
