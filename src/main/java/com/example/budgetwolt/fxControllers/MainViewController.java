package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.fxControllers.tabManagers.MenuTabManager;
import com.example.budgetwolt.fxControllers.tabManagers.OrderTabManager;
import com.example.budgetwolt.fxControllers.tabManagers.UserTabManager;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainViewController implements Initializable {

    @FXML
    public Label greetingMessage;
    @FXML
    public TabPane tabsPane;
    // Users
    @FXML
    public Tab usersTab;
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
    // Menu
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
    @FXML
    public ComboBox<Restaurant> restaurantSelectComboBox;
    @FXML
    public Pane restaurantSelectPane;
    // Orders
    @FXML
    public Tab ordersTab;
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

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    private User currentUser;

    private MenuTabManager menuTabManager;
    private OrderTabManager orderTabManager;
    private UserTabManager userTabManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);

        this.currentUser = user;

        this.menuTabManager = new MenuTabManager(customHibernate, currentUser, foodList, foodName,
                                                 priceField, ingredientsField, instructionsField, spicyCheckbox,
                                                 veganCheckbox, restaurantSelectComboBox);
        this.orderTabManager = new OrderTabManager(customHibernate, currentUser, ordersListView,
                                                   clientComboBox, orderTitleField, orderPriceField,
                                                   restaurantComboBox, orderStatusComboBox, restaurantMenuListView);
        this.userTabManager = new UserTabManager(this.entityManagerFactory, customHibernate, currentUser, idCol, userTypeCol,
                                                 usernameCol, passwordCol, nameCol, surnameCol, addressCol, phoneNumCol,
                                                 userTable);

        greetingMessage.setText("Welcome " + this.currentUser.getName() + "!");

        setVisibility();
        reloadTableData();
    }

    public void setVisibility() {
        if (currentUser.isAdmin()) {
            restaurantSelectPane.setVisible(true);
        } else if (currentUser instanceof Restaurant) {
            tabsPane.getSelectionModel().select(ordersTab);
            tabsPane.getTabs().remove(usersTab);
            restaurantSelectPane.setVisible(false);
        }
    }

    public void reloadTableData() {
        if (usersTab.isSelected()) {
            loadUserData();
        } else if (menuTab.isSelected()) {
            loadMenuData();
        } else if (ordersTab.isSelected()) {
            loadOrders();
        }
    }

    public void loadUserData() {
        userTabManager.loadData();
    }
    public void createUser(ActionEvent actionEvent) throws IOException {
        userTabManager.createUser();
    }
    public void updateUser(ActionEvent actionEvent) throws IOException {
        userTabManager.updateUser();
    }
    public void deleteUser(ActionEvent actionEvent) {
        userTabManager.deleteUser();
    }

    public void loadMenuData() {
        menuTabManager.loadData();
    }
    public void deleteCuisine(ActionEvent actionEvent) {
        menuTabManager.deleteCuisine();
    }
    public void updateCuisine(ActionEvent actionEvent) {
        menuTabManager.updateCuisine();
    }
    public void addCuisine(ActionEvent actionEvent) {
        menuTabManager.addCuisine();
    }
    public void clearCuisineFields(ActionEvent actionEvent) {
        menuTabManager.clearFields();
    }

    public void loadOrders() {
        orderTabManager.loadData();
    }
    public void createOrder(ActionEvent actionEvent) {
        orderTabManager.createOrder();
    }

    public void updateOrder(ActionEvent actionEvent) {
        orderTabManager.updateOrder();
    }

    public void deleteOrder(ActionEvent actionEvent) {
        orderTabManager.deleteOrder();
    }

    public void loadRestaurantMenu(ActionEvent actionEvent) {
        Restaurant selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();

        restaurantMenuListView.setItems(FXCollections.observableList(selectedRestaurant.getDishes()));
    }
}
