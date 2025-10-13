package com.example.budgetwolt.models;

public class Review {
    private int id;
    private int rate;
    private String text;

    public Review(int rate, String text) {
        this.rate = rate;
        this.text = text;
    }
}
