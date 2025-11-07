package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.hibernateControl.GenericHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Driver;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    @FXML
    public TextField driverLicense;
    @FXML
    public DatePicker driverDateOfBirth;
    @FXML
    public TextField workingHours;
    @FXML
    public Text formLabel;
    @FXML
    public Button submitButton;
    @FXML
    public Button returnButton;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    public void setData(EntityManagerFactory entityManagerFactory, User userToUpdate, boolean isEditModeEnabled, boolean isAdmin) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);

        if(isAdmin) {
            returnButton.setDisable(true);
            returnButton.setVisible(false);
            submitButton.setText("Save");
            formLabel.setText("Create new user");
        }

        if(isEditModeEnabled) {
            formLabel.setText("Edit user");

            fillUserData(userToUpdate);
        }
    }

    public void fillUserData(User userToUpdate) {
        usernameField.setText(userToUpdate.getUsername());
        passwordField.setText(userToUpdate.getPassword());
        nameField.setText(userToUpdate.getName());
        surnameField.setText(userToUpdate.getSurname());
        phoneNumberField.setText(userToUpdate.getPhoneNumber());
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

    public void registerNewUser(ActionEvent actionEvent) throws IOException {

        if (!customHibernate.isUniqueUsername(usernameField.getText())) {
            FxUtil.generateAlert(Alert.AlertType.ERROR, "Error!", "Username is already in use!", "Please select a different username.");
            return;
        }

        if(userRadio.isSelected()) {
            User user = new User(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            customHibernate.create(user);
        } else if (clientRadio.isSelected()) {
            BasicUser basicUser = new BasicUser(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText());
            customHibernate.create(basicUser);
        } else if (courierRadio.isSelected()) {
            Driver driver = new Driver(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    driverLicense.getText(),
                    driverDateOfBirth.getValue());
            customHibernate.create(driver);
        } else if (restaurantRadio.isSelected()) {
            Restaurant restaurant = new Restaurant(
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    workingHours.getText());
            customHibernate.create(restaurant);
        }

        redirectToLoginForm(actionEvent);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRole(null);
    }

}
