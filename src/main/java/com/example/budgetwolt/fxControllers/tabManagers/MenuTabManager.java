package com.example.budgetwolt.fxControllers.tabManagers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.Cuisine;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.util.List;

public class MenuTabManager {

    private final CustomHibernate customHibernate;
    private final User currentUser;

    private final ListView<Cuisine> foodList;
    private final TextField foodName;
    private final TextField priceField;
    private final TextArea ingredientsField;
    private final TextArea instructionsField;
    private final CheckBox spicyCheckbox;
    private final CheckBox veganCheckbox;
    private final ComboBox<Restaurant> restaurantComboBox;

    public MenuTabManager(CustomHibernate customHibernate, User currentUser,
                          ListView<Cuisine> foodList, TextField foodName, TextField priceField,
                          TextArea ingredientsField, TextArea instructionsField, CheckBox spicyCheckbox,
                          CheckBox veganCheckbox, ComboBox<Restaurant> restaurantComboBox) {

        this.customHibernate = customHibernate;
        this.currentUser = currentUser;
        this.foodList = foodList;
        this.foodName = foodName;
        this.priceField = priceField;
        this.ingredientsField = ingredientsField;
        this.instructionsField = instructionsField;
        this.spicyCheckbox = spicyCheckbox;
        this.veganCheckbox = veganCheckbox;
        this.restaurantComboBox = restaurantComboBox;

        initListeners();
    }

    private void initListeners() {
        foodList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                foodName.setText(newValue.getName());
                priceField.setText(String.valueOf(newValue.getPrice()));
                instructionsField.setText(newValue.getInstructions());
                ingredientsField.setText(newValue.getIngredients());
                spicyCheckbox.setSelected(newValue.isSpicy());
                veganCheckbox.setSelected(newValue.isVegan());
            }
        });
    }

    public void loadData() {
        List<Restaurant> restaurants = customHibernate.getAllRecords(Restaurant.class);
        restaurantComboBox.setItems(FXCollections.observableList(restaurants));

        if (currentUser instanceof Restaurant) {
            restaurantComboBox.getSelectionModel().select((Restaurant) currentUser);
        } else {
            if(!restaurants.isEmpty()) {
                restaurantComboBox.getSelectionModel().select(restaurants.get(0));
            }
        }

        Restaurant selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();
        foodList.setItems(FXCollections.observableList(selectedRestaurant.getDishes()));
    }

    public void addCuisine() {
        Cuisine food = new Cuisine(
                foodName.getText(),
                Double.parseDouble(priceField.getText()),
                ingredientsField.getText(),
                instructionsField.getText(),
                spicyCheckbox.isSelected(),
                veganCheckbox.isSelected(),
                restaurantComboBox.getSelectionModel().getSelectedItem()
        );

        customHibernate.create(food);

        loadData();
    }

    public void updateCuisine() {
        Cuisine selectedCuisine = foodList.getSelectionModel().getSelectedItem();

        selectedCuisine.setName(foodName.getText());
        selectedCuisine.setIngredients(ingredientsField.getText());
        selectedCuisine.setPrice(Double.parseDouble(priceField.getText()));
        selectedCuisine.setInstructions(instructionsField.getText());
        selectedCuisine.setSpicy(spicyCheckbox.isSelected());
        selectedCuisine.setVegan(veganCheckbox.isSelected());
        selectedCuisine.setRestaurant(restaurantComboBox.getSelectionModel().getSelectedItem());

        customHibernate.update(selectedCuisine);

        loadData();
    }

    public void deleteCuisine() {
        Cuisine selectedCuisine = foodList.getSelectionModel().getSelectedItem();
        customHibernate.delete(Cuisine.class, selectedCuisine.getId());

        loadData();
    }

    public void clearFields() {
        foodName.clear();
        priceField.clear();
        ingredientsField.clear();
        instructionsField.clear();
        spicyCheckbox.setSelected(false);
        veganCheckbox.setSelected(false);

        foodList.getSelectionModel().clearSelection();
    }
}
