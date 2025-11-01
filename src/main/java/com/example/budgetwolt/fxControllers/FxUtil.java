package com.example.budgetwolt.fxControllers;

import javafx.scene.control.Alert;

public class FxUtil {
    public static void generateAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
