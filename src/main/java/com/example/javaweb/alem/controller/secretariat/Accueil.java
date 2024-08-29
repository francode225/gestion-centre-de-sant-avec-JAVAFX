package com.example.javaweb.alem.controller.secretariat;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.core.SendButton;
import com.example.javaweb.alem.model.secretariat.PatientAccueil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Accueil implements Initializable {

    @FXML
    private Button storeCarnetButton;


    @FXML
    private TableView<PatientAccueil> tablePatient;

    @FXML
    private TextField textFieldSearchPatient;



    private final PatientAccueil patientAccueil = PatientAccueil.getInstance();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ///INITIALISATION DE LA TABLE//////

        //Création des cellules pour les colonnes de la table
        tablePatient.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        tablePatient.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        tablePatient.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("sexePatient"));
        tablePatient.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("dernierRdv"));

        TableColumn<PatientAccueil,Void> buttonColum = new TableColumn<PatientAccueil,Void>("Envoyer");
        tablePatient.getColumns().set(4,buttonColum);
        tablePatient.getColumns().get(4).setMaxWidth(500.0);
        buttonColum.setCellFactory(patientAccueilVoidTableColumn -> {
            return new SendButton();
        });


        tablePatient.itemsProperty().setValue(patientAccueil.getAllPatients(""));

        //CONFIGURATION DU TEXTFIELD DE RECHERCHE////
        textFieldSearchPatient.setOnKeyReleased(keyEvent -> {
            tablePatient.itemsProperty().setValue(patientAccueil.getAllPatients(textFieldSearchPatient.getText().toString()));
        });

        //ENREGISTREMENT D'UN NOUVEAU PATIENT
        storeCarnetButton.setOnMouseClicked(mouseEvent -> {
            Router.toCarnet();
        });
    }
}
