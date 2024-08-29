package com.example.javaweb.alem.controller.laboratoire;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.core.ConsultationButton;
import com.example.javaweb.alem.core.ExamenButton;
import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.secretariat.PatientAccueil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Accueil implements Initializable {
    @FXML
    private Label faireAnalyseLabel;

    @FXML
    private TableView<ExamenModel> tablePatient;

    @FXML
    private Label tableLabel;

    @FXML
    private Button deconnexionButton;


    private static final  ExamenModel examenModel = ExamenModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableLabel.textProperty().setValue("Analyse à faire");

        //Création des cellules pour les colonnes de la table
        tablePatient.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idCarnet"));
        tablePatient.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        tablePatient.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("examen"));

        TableColumn<ExamenModel,Void> idCarnet = new TableColumn<>("Envoyer");
        tablePatient.getColumns().set(3,idCarnet);
        tablePatient.getColumns().get(3).setMaxWidth(500.0);
        tablePatient.getColumns().get(3).setPrefWidth(500.0);
        tablePatient.getColumns().get(3).setMinWidth(200.0);

        idCarnet.setCellFactory(patientAccueilVoidTableColumn -> {
            return new ExamenButton();
        });

        tablePatient.itemsProperty().setValue(examenModel.getAllExamens());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    tablePatient.itemsProperty().setValue(examenModel.getAllExamens());
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        deconnexionButton.setOnMouseClicked(mouseEvent -> {
            Router.toConnexion();
        });


    }
}
