package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.hibernateControl.GenericHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Driver;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import jakarta.persistence.Basic;
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
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
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
    @FXML
    public Pane radioPane;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    private User userToUpdate;
    private boolean editMode;
    private boolean isAdmin;

    public void setData(EntityManagerFactory entityManagerFactory, User userToUpdate, boolean isEditModeEnabled, boolean isAdmin) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);

        this.editMode = isEditModeEnabled;
        this.isAdmin = isAdmin;

        if(isAdmin) {
            returnButton.setDisable(true);
            returnButton.setVisible(false);
            submitButton.setText("Save");
            formLabel.setText("Create new user");

        }

        if(isEditModeEnabled) {
            formLabel.setText("Edit user");
            radioPane.setVisible(false);

            this.userToUpdate = userToUpdate;
            fillUserData(this.userToUpdate);
        }

        this.setRole(null);
    }

    public void fillUserData(User userToUpdate) {
        usernameField.setText(userToUpdate.getUsername());
        passwordField.setText(userToUpdate.getPassword());
        nameField.setText(userToUpdate.getName());
        surnameField.setText(userToUpdate.getSurname());
        phoneNumberField.setText(userToUpdate.getPhoneNumber());

        if(userToUpdate instanceof BasicUser client)
            addressField.setText(client.getAddress());
        if(userToUpdate instanceof Restaurant restaurant)
            workingHours.setText(restaurant.getWorkHours());
        if(userToUpdate instanceof Driver driver) {
            driverLicense.setText(driver.getDriverLicense());
            driverDateOfBirth.setValue(driver.getDateOfBirth());
        }
    }

    public void redirectToLoginForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
    }

    public void setRole(ActionEvent actionEvent) {

        boolean isUser = userRadio.isSelected();
        boolean isRestaurant = restaurantRadio.isSelected() || userToUpdate instanceof Restaurant;
        boolean isClient = clientRadio.isSelected() || userToUpdate instanceof BasicUser;
        boolean isDriver = courierRadio.isSelected() || userToUpdate instanceof Driver;

        if (isClient) {
            clientPane.setVisible(true);
            driverPane.setVisible(false);
            restaurantPane.setVisible(false);
        } else {
            driverPane.setVisible(false);
            clientPane.setVisible(false);
            restaurantPane.setVisible(false);
        }

        if (isRestaurant) {
            clientPane.setVisible(true);
            driverPane.setVisible(false);
            restaurantPane.setVisible(true);
        } if (isDriver) {
            clientPane.setVisible(true);
            driverPane.setVisible(true);
            restaurantPane.setVisible(false);
        }
    }

    public void submitForm(ActionEvent actionEvent) throws IOException {

        if (editMode) {
            updateUser();
        } else {
            registerNewUser();
        }

        if(!isAdmin) {
            redirectToLoginForm(actionEvent);
        } else {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        }

    }

    public void registerNewUser() {
        if (!customHibernate.isUniqueUsername(usernameField.getText())) {
            FxUtil.generateAlert(Alert.AlertType.ERROR, "Error!", "Username is already in use!", "Please select a different username.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());

        if (userRadio.isSelected()) {
            User user = new User(
                    usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            user.setAdmin(true);
            customHibernate.create(user);
        } else if (clientRadio.isSelected()) {
            BasicUser basicUser = new BasicUser(
                    usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText());
            customHibernate.create(basicUser);
        } else if (courierRadio.isSelected()) {
            Driver driver = new Driver(
                    usernameField.getText(),
                    hashedPassword,
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
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    workingHours.getText());
            customHibernate.create(restaurant);
        }
    }

    public void updateUser() {

        if(!Objects.equals(userToUpdate.getUsername(), usernameField.getText())) {
            if (!customHibernate.isUniqueUsername(usernameField.getText())) {
                FxUtil.generateAlert(Alert.AlertType.ERROR, "Error!", "Username is already in use!", "Please select a different username.");
                return;
            }
        }

        String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());

        userToUpdate.setUsername(usernameField.getText());
        userToUpdate.setPassword(hashedPassword);
        userToUpdate.setName(nameField.getText());
        userToUpdate.setSurname(surnameField.getText());
        userToUpdate.setPhoneNumber(phoneNumberField.getText());

        if (userToUpdate instanceof BasicUser client) {
            client.setAddress(addressField.getText());
        }
        if (userToUpdate instanceof Restaurant restaurant) {
            restaurant.setWorkHours(workingHours.getText());
        }
        if (userToUpdate instanceof Driver driver) {
            driver.setDriverLicense(driverLicense.getText());
            driver.setDateOfBirth(driverDateOfBirth.getValue());
        }

        customHibernate.update(userToUpdate);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRole(null);
    }

}
