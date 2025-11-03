package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Driver;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
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
    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    private User currentUser;

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
        //addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        //phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
    }

    public void initData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        System.out.println(entityManagerFactory);
        this.currentUser = user;
        greetingMessage.setText("Welcome " + this.currentUser.getName() + "!");
    }

    public void reloadTableData() {
        if (usersTab.isSelected()) {
            data.clear();
            readUserData();
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


}
