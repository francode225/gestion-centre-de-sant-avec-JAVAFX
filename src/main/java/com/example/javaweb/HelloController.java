package com.example.javaweb;

import com.example.javaweb.alem.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        Router.toConnexion();
    }
}