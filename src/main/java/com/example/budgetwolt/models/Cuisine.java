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
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "items", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FoodOrder> foodOrders;
    @ManyToOne
    private Restaurant restaurant;
    private String ingredients;
    private double price;
    private String instructions;
    private boolean isSpicy;
    private boolean isVegan;
    private boolean active = true;

    public Cuisine(String name, double price, String ingredients, String instructions, boolean isSpicy, boolean isVegan, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.instructions = instructions;
        this.isSpicy = isSpicy;
        this.isVegan = isVegan;
        this.ingredients = ingredients;
        this.restaurant = restaurant;
    }

    public String toString(){
        return this.name;
    }
}
