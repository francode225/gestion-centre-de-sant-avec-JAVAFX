package com.example.javaweb.alem.controller;

import com.example.javaweb.alem.model.LoginPersonnel;
import com.example.javaweb.alem.Router;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Login extends GridPane implements Initializable {


    @FXML
    private Button connectButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        connectButton.setOnMouseClicked(mouseEvent -> {

            LoginPersonnel.getInstance().findPersonnel(loginTextField.getText(),passwordTextField.getText(),"");

        });
    }



}
