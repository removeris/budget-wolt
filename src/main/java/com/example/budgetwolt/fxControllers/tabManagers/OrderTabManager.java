package com.example.budgetwolt.fxControllers.tabManagers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.hibernate.query.Order;

import java.time.LocalDate;
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
    private final Label restaurantLabel;
    private final ListView<Cuisine> selectedMenuItemsListView;
    private ObservableList<Cuisine> selectedMenuItems = FXCollections.observableArrayList();

    public OrderTabManager(CustomHibernate customHibernate, User currentUser,
                           ListView<FoodOrder> ordersListView, ComboBox<BasicUser> clientComboBox,
                           TextField orderTitleField, TextField orderPriceField, ComboBox<Restaurant> restaurantComboBox,
                           ComboBox<OrderStatus> orderStatusComboBox, ListView<Cuisine> restaurantMenuListView,
                           ComboBox<BasicUser> clientFilterComboBox, ComboBox<OrderStatus> statusFilterComboBox,
                           ComboBox<Restaurant> restaurantFilterComboBox, DatePicker fromDateFilter, DatePicker toDateFilter,
                           Label restaurantLabel, ListView<Cuisine> selectedMenuItemsListView) {

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
        this.restaurantLabel = restaurantLabel;
        this.selectedMenuItemsListView = selectedMenuItemsListView;
        this.selectedMenuItemsListView.setItems(selectedMenuItems);

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
                selectedMenuItems.clear();
                selectedMenuItems.setAll(customHibernate.getCuisineList(newValue));
            }
        });
        restaurantMenuListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.out.println("OOGABOOGA");
                Cuisine selectedMenuItem = restaurantMenuListView.getSelectionModel().getSelectedItem();
                if (selectedMenuItem != null) {
                    selectedMenuItems.add(selectedMenuItem);
                    //selectedMenuItemsListView.setItems(selectedMenuItems);
                }
            }
        });
    }

    public void loadData() {

        List<FoodOrder> orders = new ArrayList<>();

        if (currentUser.isAdmin()) {
            orders = customHibernate.getAllRecords(FoodOrder.class);
        } else if (currentUser instanceof Restaurant) {
            orders = customHibernate.searchOrders((Restaurant) currentUser, null, null, null, null);
            restaurantFilterComboBox.setVisible(false);
            restaurantLabel.setVisible(false);
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

        clientFilterComboBox.setConverter(new StringConverter<BasicUser>() {
            @Override
            public String toString(BasicUser client) {
                if(client == null) {
                    return null;
                }
                return client.getName() + " " + client.getSurname() + " " + client.getUsername();
            }

            @Override
            public BasicUser fromString(String string) {
                return null;
            }
        });

        restaurantFilterComboBox.setConverter(new StringConverter<Restaurant>() {
            @Override
            public String toString(Restaurant restaurant) {
                if(restaurant == null) {
                    return null;
                }
                return restaurant.getName();
            }
            @Override
            public Restaurant fromString(String string) {
                return null;
            }
        });
    }

    public void createOrder() {
        BasicUser selectedClient = clientComboBox.getSelectionModel().getSelectedItem();
        List<Cuisine> selectedItems = selectedMenuItems;
        // TODO get items from selectedItems listview,
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
        List<Cuisine> foodItems = selectedMenuItems;
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


    public void filterOrders() {
        Restaurant restaurant = restaurantFilterComboBox.getSelectionModel().getSelectedItem();
        if (currentUser instanceof Restaurant) {
            restaurant = (Restaurant) currentUser;
        }
        OrderStatus orderStatus = statusFilterComboBox.getValue();
        BasicUser client = clientFilterComboBox.getSelectionModel().getSelectedItem();
        LocalDate fromDate = fromDateFilter.getValue();
        LocalDate toDate = toDateFilter.getValue();

        List<FoodOrder> foundOrders = customHibernate.searchOrders(restaurant, orderStatus, client, fromDate, toDate);

        ordersListView.setItems(FXCollections.observableList(foundOrders));
    }

    public void clearFilters() {
        restaurantFilterComboBox.getSelectionModel().clearSelection();
        statusFilterComboBox.getSelectionModel().clearSelection();
        clientFilterComboBox.getSelectionModel().clearSelection();
        fromDateFilter.setValue(null);
        toDateFilter.setValue(null);

    }
}
