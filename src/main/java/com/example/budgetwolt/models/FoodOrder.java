package com.example.budgetwolt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToOne
    private BasicUser buyer;
    @ManyToMany
    private List<Cuisine> items;
    private double price;
    @OneToMany
    private List<Chat> chat;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    private Restaurant restaurant;

    public FoodOrder(String title, BasicUser buyer, List<Cuisine> items, double price, OrderStatus status, Restaurant restaurant) {
        this.title = title;
        this.buyer = buyer;
        this.items = items;
        this.price = price;
        this.status = status;
        this.restaurant = restaurant;
    }

    public String toString() {
        return "ID: " + id + " " + title;
    }
}
