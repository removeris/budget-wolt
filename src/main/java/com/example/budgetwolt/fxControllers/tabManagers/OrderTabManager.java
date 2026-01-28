package com.example.budgetwolt.fxControllers.tabManagers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.hibernate.query.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTabManager {

    private final CustomHibernate customHibernate;
    private final User currentUser;

    private final ListView<FoodOrder> ordersListView;
    private final ComboBox<BasicUser> clientComboBox;
    private final TextField orderTitleField;
    private final TextField orderPriceField;
    private final ComboBox<Restaurant> restaurantComboBox;
    private final ComboBox<OrderStatus> orderStatusComboBox;
    private final ListView<Cuisine> restaurantMenuListView;
    private final ComboBox<BasicUser> clientFilterComboBox;
    private final ComboBox<OrderStatus> statusFilterComboBox;
    private final ComboBox<Restaurant> restaurantFilterComboBox;
    private final DatePicker fromDateFilter;
    private final DatePicker toDateFilter;

    public OrderTabManager(CustomHibernate customHibernate, User currentUser,
                           ListView<FoodOrder> ordersListView, ComboBox<BasicUser> clientComboBox,
                           TextField orderTitleField, TextField orderPriceField, ComboBox<Restaurant> restaurantComboBox,
                           ComboBox<OrderStatus> orderStatusComboBox, ListView<Cuisine> restaurantMenuListView,
                           ComboBox<BasicUser> clientFilterComboBox, ComboBox<OrderStatus> statusFilterComboBox,
                           ComboBox<Restaurant> restaurantFilterComboBox, DatePicker fromDateFilter, DatePicker toDateFilter) {

        this.customHibernate = customHibernate;
        this.currentUser = currentUser;
        this.ordersListView = ordersListView;
        this.clientComboBox = clientComboBox;
        this.orderTitleField = orderTitleField;
        this.orderPriceField = orderPriceField;
        this.restaurantComboBox = restaurantComboBox;
        this.orderStatusComboBox = orderStatusComboBox;
        this.restaurantMenuListView = restaurantMenuListView;
        this.clientFilterComboBox = clientFilterComboBox;
        this.statusFilterComboBox = statusFilterComboBox;
        this.restaurantFilterComboBox = restaurantFilterComboBox;
        this.fromDateFilter = fromDateFilter;
        this.toDateFilter = toDateFilter;

        initListeners();
    }

    private void initListeners() {
        ordersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clientComboBox.setValue(newValue.getBuyer());
                orderTitleField.setText(newValue.getTitle());
                orderPriceField.setText(String.valueOf(newValue.getPrice()));
                restaurantComboBox.setValue(newValue.getRestaurant());
                orderStatusComboBox.setValue(newValue.getStatus());
            }
        });
    }

    public void loadData() {

        List<FoodOrder> orders = new ArrayList<>();

        if (currentUser.isAdmin()) {
            orders = customHibernate.getAllRecords(FoodOrder.class);
        } else if (currentUser instanceof Restaurant) {
            orders = ((Restaurant) currentUser).getFoodOrders();
        }
        ordersListView.setItems(FXCollections.observableList(orders));

        List<BasicUser> clients = customHibernate.getAllRecords(BasicUser.class)
                .stream()
                .filter(c -> !(c instanceof Restaurant || c instanceof Driver))
                .collect(Collectors.toList());
        clientComboBox.setItems(FXCollections.observableList(clients));
        List<Restaurant> restaurants = customHibernate.getAllRecords(Restaurant.class);
        restaurantComboBox.setItems(FXCollections.observableList(restaurants));
        orderStatusComboBox.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        restaurantFilterComboBox.setItems(FXCollections.observableList(restaurants));
        statusFilterComboBox.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        clientFilterComboBox.setItems(FXCollections.observableList(clients));
    }

    public void createOrder() {
        BasicUser selectedClient = clientComboBox.getSelectionModel().getSelectedItem();
        List<Cuisine> selectedItems = restaurantMenuListView.getSelectionModel().getSelectedItems();
        double orderPrice = Double.parseDouble(orderPriceField.getText());
        Restaurant selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();

        FoodOrder foodOrder = new FoodOrder(
                orderTitleField.getText(),
                selectedClient,
                selectedItems,
                orderPrice,
                orderStatusComboBox.getValue(),
                selectedRestaurant);

        customHibernate.create(foodOrder);
        loadData();
    }

    public void updateOrder() {
        FoodOrder selectedOrder = ordersListView.getSelectionModel().getSelectedItem();

        BasicUser client = clientComboBox.getSelectionModel().getSelectedItem();
        List<Cuisine> foodItems = restaurantMenuListView.getSelectionModel().getSelectedItems();
        String title = orderTitleField.getText();
        double price = Double.parseDouble(orderPriceField.getText());
        Restaurant restaurant = restaurantComboBox.getSelectionModel().getSelectedItem();
        OrderStatus status = orderStatusComboBox.getValue();

        selectedOrder.setBuyer(client);
        selectedOrder.setItems(foodItems);
        selectedOrder.setTitle(title);
        selectedOrder.setPrice(price);
        selectedOrder.setRestaurant(restaurant);
        selectedOrder.setStatus(status);

        customHibernate.update(selectedOrder);

        loadData();
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = ordersListView.getSelectionModel().getSelectedItem();

        customHibernate.deleteFoodOrder(selectedOrder.getId());

        loadData();
    }




}
