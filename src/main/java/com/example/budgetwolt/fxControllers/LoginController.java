package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.BasicUser;
import com.example.budgetwolt.models.Restaurant;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    private Button registerButton;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManagerFactory = Persistence.createEntityManagerFactory("budgetwolt");
        customHibernate = new CustomHibernate(entityManagerFactory);
    }

    public void attemptLogin(ActionEvent e) throws IOException {

        String username = this.usernameField.getText();
        String psw = this.passwordField.getText();

        User user = customHibernate.getUserByCredentials(username, psw);

        if(user == null) {
            FxUtil.generateAlert(Alert.AlertType.ERROR, "Error!", "Incorrect credentials", "Retry with correct credentials.");
        }
        else if (user instanceof BasicUser && !(user instanceof Restaurant)) {
            FxUtil.generateAlert(Alert.AlertType.INFORMATION, "INFO", "Clients and Drivers App", "Clients and Drivers must use the mobile app instead!");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));

            Parent parent = fxmlLoader.load();
            MainViewController mainViewController = fxmlLoader.getController();
            mainViewController.initData(entityManagerFactory, user);

            Scene scene = new Scene(parent, 900, 600);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


    }

    public void redirectToRegistrationForm(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory, null, false, false);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
