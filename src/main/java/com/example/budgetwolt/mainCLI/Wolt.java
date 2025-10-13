package com.example.budgetwolt.mainCLI;

import com.example.budgetwolt.models.FoodOrder;
import com.example.budgetwolt.models.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Wolt implements Serializable {
    private List<User> allUsers;
    private List<FoodOrder> allOrders;

    public Wolt() {
        allUsers = new ArrayList<User>();
        allOrders = new ArrayList<FoodOrder>();
    }
}
