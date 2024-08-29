package com.example.javaweb.alem.controller.infirmerie;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.core.ConstanteButton;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.core.SoinsButton;
import com.example.javaweb.alem.model.infirmerie.PatientInfirmerie;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Accueil implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label constantesText;

    @FXML
    private Text decompteText;

    @FXML
    private GridPane gridSide;

    @FXML
    private Label nomLabel;

    @FXML
    private Label nombreConstantesLabel;

    @FXML
    private Label nombreSoinsLabel;

    @FXML
    private Label soinsText;

    @FXML
    private Label tableLabel;

    @FXML
    private Label patientLabel;

    @FXML
    private Button deconnexionButton;

    @FXML
    private TableView<PatientInfirmerie> tableConstante;


    @FXML
    private TableView<PatientInfirmerie> tableSoins;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(SessionUser.getInstance().getSexe() != null && SessionUser.getInstance().getNomPrenom()!=null) {
                nomLabel.textProperty().setValue(SessionUser.getInstance().getNomPrenom());

        }

        //Table Constante
        tableLabel.textProperty().set("Table Des Constantes à Prendre");


        // Création des cellules pour les colonnes de la table
        tableConstante.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        tableConstante.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        tableConstante.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dernierRdv"));
        tableConstante.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("statutConstante"));

        // Ajouter la colonne de CheckBox
        TableColumn<PatientInfirmerie, Void> buttonConstante = new TableColumn<>("Prendre Constante");
        tableConstante.getColumns().set(4, buttonConstante);
        tableConstante.getColumns().get(4).setMaxWidth(500.0);
        tableConstante.getColumns().get(4).setPrefWidth(500.0);
        tableConstante.getColumns().get(4).setMinWidth(200.0);

        buttonConstante.setCellFactory(patientAccueilVoidTableColumn -> {
            return new ConstanteButton();
        });

        tableConstante.itemsProperty().setValue(PatientInfirmerie.getInstance().getPatientsWithStatutConstanteZero());

        //Timer
        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    tableConstante.itemsProperty().setValue(PatientInfirmerie.getInstance().getPatientsWithStatutConstanteZero());
                })
        );
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();

        //table soins
        tableSoins.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        tableSoins.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        tableSoins.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("soins"));
        tableSoins.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("idPrescription"));

        // Ajouter la colonne de CheckBox
        TableColumn<PatientInfirmerie, Void> buttonSoins = new TableColumn<>("Fournir soins");
        tableSoins.getColumns().set(4, buttonSoins);
        tableSoins.getColumns().get(4).setMaxWidth(500.0);
        tableSoins.getColumns().get(4).setPrefWidth(500.0);
        tableSoins.getColumns().get(4).setMinWidth(200.0);

        buttonSoins.setCellFactory(patientAccueilVoidTableColumn -> {
            return new SoinsButton();
        });
        tableSoins.itemsProperty().setValue(PatientInfirmerie.getInstance().getPatientsSoins());


        //Timer
        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    tableSoins.itemsProperty().setValue(PatientInfirmerie.getInstance().getPatientsSoins());
                })
        );
        timeline2.setCycleCount(Animation.INDEFINITE);
        timeline2.play();

        constantesText.setOnMouseClicked(mouseEvent -> {
            GridPane.setMargin(constantesText, new Insets(5, 0, 5, 15));
            GridPane.setMargin(soinsText, new Insets(5, 15, 5, 0));
            tableSoins.setVisible(false);
            tableConstante.setVisible(true);
            tableLabel.textProperty().set("Table Des Constantes à Prendre");

        });

        soinsText.setOnMouseClicked(mouseEvent -> {
            GridPane.setMargin(soinsText, new Insets(5, 0, 5, 15));
            GridPane.setMargin(constantesText, new Insets(5, 15, 5, 0));
            tableSoins.setVisible(true);
            tableConstante.setVisible(false);
            tableLabel.textProperty().set("Table Des Soins à Fournir");

        });

        nombreConstantesLabel.textProperty().bind(PatientInfirmerie.nombreConstantes().asString());
        nombreSoinsLabel.textProperty().bind(PatientInfirmerie.nombreSoins().asString());




        Timeline timeline3 = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    try {
                        patientLabel.textProperty().bind(PatientObservation.getInstance().getPatientTime());
                        decompteText.textProperty().bind(PatientObservation.getTimeElapsed());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                })
        );
        timeline3.setCycleCount(Animation.INDEFINITE);
        timeline3.play();

        deconnexionButton.setOnMouseClicked(mouseEvent -> {
            Router.toConnexion();
        });
    }


}

