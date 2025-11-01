package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.Main;
import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    private Button registerButton;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("budgetwolt");
    private CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);

    public void attemptLogin(ActionEvent e) throws IOException {

        String username = this.usernameField.getText();
        String psw = this.passwordField.getText();

        User user = customHibernate.getUserByCredentials(username, psw);

        if(user == null) {
            System.out.printf("Unauthorized!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));

            Parent parent = fxmlLoader.load();
            MainViewController mainViewController = fxmlLoader.getController();
            mainViewController.initData(user.getName());

            Scene scene = new Scene(parent, 600, 600);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


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
