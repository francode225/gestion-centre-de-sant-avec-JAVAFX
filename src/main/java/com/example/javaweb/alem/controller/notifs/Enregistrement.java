package com.example.javaweb.alem.controller.notifs;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Enregistrement implements Initializable {

    @FXML
    private Text notifText;

    @FXML
    private Button okButton;

    private NotificationSuccesModel notificationModel = NotificationSuccesModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notifText.textProperty().bind(notificationModel.messageProperty());

        okButton.setOnMouseClicked(mouseEvent -> {
            Help.close();
        });
    }
}
