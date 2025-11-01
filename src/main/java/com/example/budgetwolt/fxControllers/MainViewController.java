package com.example.budgetwolt.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML
    public Label greetingMessage;

    public void initData(String name) {
        greetingMessage.setText("Welcome " + name + "!");
    }
}
