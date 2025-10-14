package com.example.budgetwolt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BasicUser extends User  {
    protected String address;
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<FoodOrder> myOrders;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> myReviews;
    @OneToMany(mappedBy = "feedbackOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> feedback;

    public BasicUser(String username, String password, String name, String surname, String phoneNumber, String address) {
        super(username, password, name, surname, phoneNumber);
        this.address = address;
        this.myOrders = new ArrayList<FoodOrder>();
        this.myReviews = new ArrayList<Review>();
        this.feedback = new ArrayList<Review>();
    }
}
