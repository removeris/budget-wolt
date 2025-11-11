package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {

    @FXML
    public Label greetingMessage;
    @FXML
    public TableColumn<UserTableParameters, Integer> idCol;
    @FXML
    public TableColumn<UserTableParameters, String> userTypeCol;
    @FXML
    public TableColumn<UserTableParameters, String> usernameCol;
    @FXML
    public TableColumn<UserTableParameters, String> passwordCol;
    @FXML
    public TableColumn<UserTableParameters, String> nameCol;
    @FXML
    public TableColumn<UserTableParameters, String> surnameCol;
    @FXML
    public TableColumn<UserTableParameters, String> addressCol;
    @FXML
    public TableColumn<UserTableParameters, String> phoneNumCol;
    @FXML
    public TableView<UserTableParameters> userTable;
    @FXML
    public Tab usersTab;

    // Food Tab
    @FXML
    public Tab menuTab;
    @FXML
    public ListView<Cuisine> foodList;
    @FXML
    public TextField foodName;
    @FXML
    public TextField priceField;
    @FXML
    public TextArea ingredientsField;
    @FXML
    public TextArea instructionsField;
    @FXML
    public CheckBox spicyCheckbox;
    @FXML
    public CheckBox veganCheckbox;
    // Orders
    @FXML
    public ListView<FoodOrder> ordersListView;
    @FXML
    public ComboBox<BasicUser> clientComboBox;
    @FXML
    public TextField orderTitleField;
    @FXML
    public TextField orderPriceField;
    @FXML
    public ComboBox<Restaurant> restaurantComboBox;
    @FXML
    public ComboBox<OrderStatus> orderStatusComboBox;
    @FXML
    public ListView<Cuisine> restaurantMenuListView;
    @FXML
    public Tab ordersTab;

    private User currentUser;
    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.setEditable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setOnEditCommit(event -> {
           event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
           User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
           user.setPassword(event.getNewValue());
           customHibernate.update(user);
        });
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

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

    public void initData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        System.out.println(entityManagerFactory);
        this.currentUser = user;
        greetingMessage.setText("Welcome " + this.currentUser.getName() + "!");
        reloadTableData();
    }

    public void reloadTableData() {
        if (usersTab.isSelected()) {
            data.clear();
            loadUserData();
        } else if (menuTab.isSelected()) {
            List<Cuisine> food = customHibernate.getAllRecords(Cuisine.class);
            foodList.setItems(FXCollections.observableList(food));
        } else if (ordersTab.isSelected()) {
            loadOrders();


        }
    }

    public void loadUserData() {
        List<User> users = this.customHibernate.getAllRecords(User.class);
        for (User user : users) {
            UserTableParameters userTableParameters = new UserTableParameters();
            userTableParameters.setId(user.getId());
            userTableParameters.setUserType(user.getClass().getSimpleName());
            userTableParameters.setUsername(user.getUsername());
            userTableParameters.setPassword(user.getPassword());
            userTableParameters.setName(user.getName());
            userTableParameters.setSurname(user.getSurname());
            userTableParameters.setPhoneNum(user.getPhoneNumber());
            if (user instanceof BasicUser) {
                userTableParameters.setAddress(((BasicUser) user).getAddress());
            }

            if (user instanceof Restaurant) {
                // do smth
            }

            if (user instanceof Driver) {
                // do smth
            }

            data.add(userTableParameters);
        }
        userTable.getItems().clear();
        userTable.getItems().addAll(data);
    }
    public void createUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, null, false, true);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        reloadTableData();
    }
    public void updateUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        UserTableParameters selectedUser = userTable.getSelectionModel().getSelectedItem();
        User userToUpdate = customHibernate.getEntityById(User.class, selectedUser.getId());

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, userToUpdate, true, true);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        reloadTableData();
    }
    public void deleteUser(ActionEvent actionEvent) {
        UserTableParameters selectedUser = userTable.getSelectionModel().getSelectedItem();
        User userToUpdate = customHibernate.getEntityById(User.class, selectedUser.getId());

        customHibernate.delete(userToUpdate);
        reloadTableData();
    }

    public void deleteCuisine(ActionEvent actionEvent) {
        Cuisine selectedCuisine = foodList.getSelectionModel().selectedItemProperty().getValue();
        customHibernate.delete(selectedCuisine);
        reloadTableData();
    }
    public void updateCuisine(ActionEvent actionEvent) {
        Cuisine selectedCuisine = foodList.getSelectionModel().selectedItemProperty().getValue();

        selectedCuisine.setName(foodName.getText());
        selectedCuisine.setIngredients(ingredientsField.getText());
        selectedCuisine.setPrice(Double.parseDouble(priceField.getText()));
        selectedCuisine.setInstructions(instructionsField.getText());
        selectedCuisine.setSpicy(spicyCheckbox.isSelected());
        selectedCuisine.setVegan(veganCheckbox.isSelected());

        customHibernate.update(selectedCuisine);
        reloadTableData();
    }
    public void addCuisine(ActionEvent actionEvent) {

        Cuisine food = new Cuisine(
                foodName.getText(),
                Double.parseDouble(priceField.getText()),
                ingredientsField.getText(),
                instructionsField.getText(),
                spicyCheckbox.isSelected(),
                veganCheckbox.isSelected()
        );

        customHibernate.create(food);

        reloadTableData();
    }
    public void clearCuisineFields(ActionEvent actionEvent) {
        foodName.clear();
        priceField.clear();
        ingredientsField.clear();
        instructionsField.clear();
        spicyCheckbox.setSelected(false);
        veganCheckbox.setSelected(false);

        foodList.getSelectionModel().clearSelection();
    }

    public void loadOrders() {
        List<FoodOrder> orders = customHibernate.getAllRecords(FoodOrder.class);
        ordersListView.setItems(FXCollections.observableList(orders));
        List<BasicUser> clients = customHibernate.getAllRecords(BasicUser.class)
                .stream()
                .filter(c -> !(c instanceof Restaurant || c instanceof Driver))
                .collect(Collectors.toList());
        clientComboBox.setItems(FXCollections.observableList(clients));
        List<Restaurant> restaurants = customHibernate.getAllRecords(Restaurant.class);
        restaurantComboBox.setItems(FXCollections.observableList(restaurants));
        orderStatusComboBox.setItems(FXCollections.observableList(List.of(OrderStatus.values())));
    }
    public void createOrder(ActionEvent actionEvent) {
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
        reloadTableData();
    }

    public void updateOrder(ActionEvent actionEvent) {
        FoodOrder selectedOrder = ordersListView.getSelectionModel().getSelectedItem();

        BasicUser selectedClient = clientComboBox.getSelectionModel().getSelectedItem();
        List<Cuisine> selectedItems = restaurantMenuListView.getSelectionModel().getSelectedItems();
        double orderPrice = Double.parseDouble(orderPriceField.getText());
        Restaurant selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();


    }

    public void deleteOrder(ActionEvent actionEvent) {
    }
}
