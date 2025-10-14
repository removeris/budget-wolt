package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    private Button registerButton;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("budgetwolt");

    public void attemptLogin(ActionEvent e) {
        System.out.println("Attempting to login");
    }

    public void redirectToRegistrationForm(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-form.fxml"));

        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setData(entityManagerFactory);

        Scene scene = new Scene(parent, 600, 600);
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
