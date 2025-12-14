package com.example.budgetwolt.fxControllers.tabManagers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.fxControllers.RegistrationController;
import com.example.budgetwolt.fxControllers.UserTableParameters;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Driver;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class UserTabManager {

    private final EntityManagerFactory entityManagerFactory;
    private final CustomHibernate customHibernate;

    private final User currentUser;

    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    private final TableColumn<UserTableParameters, Integer> idCol;
    private final TableColumn<UserTableParameters, String> userTypeCol;
    private final TableColumn<UserTableParameters, String> usernameCol;
    private final TableColumn<UserTableParameters, String> passwordCol;
    private final TableColumn<UserTableParameters, String> nameCol;
    private final TableColumn<UserTableParameters, String> surnameCol;
    private final TableColumn<UserTableParameters, String> addressCol;
    private final TableColumn<UserTableParameters, String> phoneNumCol;
    private final TableView<UserTableParameters> userTable;
    private final TextField usernameField;
    private final TextField nameField;
    private final TextField surnameField;
    private final TextField phoneNumberField;

    public UserTabManager(EntityManagerFactory entityManagerFactory, CustomHibernate customHibernate, User currentUser,
                          TableColumn<UserTableParameters, Integer> idCol, TableColumn<UserTableParameters, String> userTypeCol,
                          TableColumn<UserTableParameters, String> usernameCol, TableColumn<UserTableParameters, String> passwordCol,
                          TableColumn<UserTableParameters, String> nameCol, TableColumn<UserTableParameters, String> surnameCol,
                          TableColumn<UserTableParameters, String> addressCol, TableColumn<UserTableParameters, String> phoneNumCol,
                          TableView<UserTableParameters> userTable, TextField usernameField, TextField nameField,
                          TextField surnameField, TextField phoneNumberField) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = customHibernate;
        this.currentUser = currentUser;
        this.idCol = idCol;
        this.userTypeCol = userTypeCol;
        this.usernameCol = usernameCol;
        this.passwordCol = passwordCol;
        this.nameCol = nameCol;
        this.surnameCol = surnameCol;
        this.addressCol = addressCol;
        this.phoneNumCol = phoneNumCol;
        this.userTable = userTable;
        this.usernameField = usernameField;
        this.nameField = nameField;
        this.surnameField = surnameField;
        this.phoneNumberField = phoneNumberField;

        initUserTable();
    }

    public void initUserTable() {
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
    }

    public void loadData() {
        data.clear();
        List<User> users = this.customHibernate.getAllRecords(User.class);
        for (User user : users) {
            UserTableParameters userTableParameters = getUserTableParameters(user);

            data.add(userTableParameters);
        }
        userTable.getItems().clear();
        userTable.getItems().addAll(data);
    }

    private static UserTableParameters getUserTableParameters(User user) {
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
            // TODO
        }
        if (user instanceof Driver) {
            // TODO
        }
        return userTableParameters;
    }

    public void createUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, null, false, true);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        loadData();
    }

    public void updateUser() throws IOException {
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

        loadData();
    }

    public void deleteUser() {
        UserTableParameters selectedUser = userTable.getSelectionModel().getSelectedItem();

        customHibernate.delete(User.class, selectedUser.getId());

        loadData();
    }

    public void searchUsers() {
        String username = usernameField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phoneNumber = phoneNumberField.getText();

        List<User> foundUsers = customHibernate.searchUsers(username, name, surname, phoneNumber);
        data.clear();

        for (User user : foundUsers) {
            UserTableParameters userTableParameters = getUserTableParameters(user);

            data.add(userTableParameters);
        }
        userTable.getItems().clear();
        userTable.getItems().addAll(data);
    }

    public void clearFilters() {
        usernameField.clear();
        nameField.clear();
        surnameField.clear();
        phoneNumberField.clear();

        loadData();
    }
}
