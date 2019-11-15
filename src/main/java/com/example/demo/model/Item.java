package com.example.demo.model;

public class Item {
    String itemId;
    String message;

    public Item() {
    }

    public Item(String itemId, String message) {
        this.itemId = itemId;
        this.message = message;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
