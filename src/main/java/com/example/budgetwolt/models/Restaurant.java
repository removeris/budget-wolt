package com.example.budgetwolt.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Restaurant extends BasicUser {
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<Cuisine> dishes;
    protected String workHours;
    protected double rating;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodOrder> foodOrders;

    public Restaurant(String username, String password, String name, String surname, String phoneNumber, String address, String workHours) {
        super(username, password, name, surname, phoneNumber, address);
        this.workHours = workHours;
        this.rating = 0.0;
        this.dishes = new ArrayList<Cuisine>();
    }
}
