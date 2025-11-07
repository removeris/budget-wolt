package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    public ListView<Ingredients> ingredientsList;
    @FXML
    public TextArea instructionsField;
    @FXML
    public CheckBox spicyCheckbox;
    @FXML
    public CheckBox veganCheckbox;


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
           foodName.setText(newValue.getName());
           priceField.setText(String.valueOf(newValue.getPrice()));
           instructionsField.setText(newValue.getInstructions());
           List<Ingredients> ingredients = newValue.getIngredients();

           System.out.println(newValue.isSpicy());
           spicyCheckbox.setSelected(newValue.isSpicy());
           veganCheckbox.setSelected(newValue.isVegan());
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
            readUserData();
        } else if (menuTab.isSelected()) {
            List<Cuisine> food = customHibernate.getAllRecords(Cuisine.class);
            foodList.setItems(FXCollections.observableList(food));

            ingredientsList.setItems(FXCollections.observableArrayList(Ingredients.values()));
            ingredientsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    public void readUserData() {
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


    public void updateUser(ActionEvent actionEvent) throws IOException {
        redirectToUserCreation();
    }

    public void redirectToUserCreation() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        UserTableParameters selectedUser = userTable.getSelectionModel().getSelectedItem();
        User userToUpdate = customHibernate.getEntityById(User.class, selectedUser.getId());

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, userToUpdate, true, true);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void loadUserCreation(ActionEvent actionEvent) {
    }

    public void deleteUser(ActionEvent actionEvent) {
    }

    public void deleteCuisine(ActionEvent actionEvent) {
    }

    public void updateCuisine(ActionEvent actionEvent) {

    }

    public void addCuisine(ActionEvent actionEvent) {

        List<Ingredients> selectedIngredients = new ArrayList<>(ingredientsList.getSelectionModel().getSelectedItems());

        Cuisine food = new Cuisine(
                foodName.getText(),
                Double.parseDouble(priceField.getText()),
                selectedIngredients,
                instructionsField.getText(),
                spicyCheckbox.isSelected(),
                veganCheckbox.isSelected()
        );

        customHibernate.create(food);

        reloadTableData();
    }
}
