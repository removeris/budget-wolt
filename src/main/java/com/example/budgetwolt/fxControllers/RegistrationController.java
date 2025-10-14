package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.GenericHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField nameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneNumberField;
    @FXML
    public TextField addressField;
    @FXML
    public ToggleGroup userRole;
    @FXML
    public RadioButton userRadio;
    @FXML
    public RadioButton restaurantRadio;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton courierRadio;
    @FXML
    public Pane driverPane;
    @FXML
    public Pane clientPane;
    @FXML
    public Pane restaurantPane;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void redirectToLoginForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
    }

    public void setRole(ActionEvent actionEvent) {
        if (userRadio.isSelected()) {
            driverPane.setVisible(false);
            clientPane.setVisible(false);
            restaurantPane.setVisible(false);
        } else if (restaurantRadio.isSelected()) {
            driverPane.setVisible(false);
            clientPane.setVisible(true);
            restaurantPane.setVisible(true);
        } else if (clientRadio.isSelected()) {
            driverPane.setVisible(false);
            clientPane.setVisible(true);
            restaurantPane.setVisible(false);
        } else if (courierRadio.isSelected()) {
            driverPane.setVisible(true);
            clientPane.setVisible(true);
            restaurantPane.setVisible(false);
        }
    }

    public void registerNewUser(ActionEvent actionEvent) {
        if(userRadio.isSelected()) {
            User user = new User(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            genericHibernate.create(user);
        } else if (clientRadio.isSelected()) {
            BasicUser basicUser = new BasicUser(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText());
            genericHibernate.create(basicUser);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRole(null);
    }

}
