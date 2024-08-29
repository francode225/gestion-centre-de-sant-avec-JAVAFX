package com.example.javaweb.alem.controller.medecine;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.core.ConsultationButton;
import com.example.javaweb.alem.core.ResultatAnalyseButton;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import com.example.javaweb.alem.model.medecine.PatientRendezVous;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static java.awt.Color.blue;

public class Accueil implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label consultationLabel;

    @FXML
    private Button deconnexionButton;

    @FXML
    private Text nomPersonnelText;

    @FXML
    private Text nombreConsultation;

    @FXML
    private Text nombrePatientObservation;

    @FXML
    private Text nombreResultatsAnalyse;

    @FXML
    private Label observationsLabel;

    @FXML
    private Label rendezVousLabel;

    @FXML
    private Label resultatLabel;

    @FXML
    private Label tableLabel;
    @FXML
    private TableView<PatientConsultation> tablePatient;

    @FXML
    private TableView<PatientRendezVous> tableRendezVous;

    @FXML
    private TableView<PatientObservation> tableObservation;

    @FXML
    private TableView<ExamenModel> tableResultat;

    private final PatientConsultation patientConsultation = PatientConsultation.getInstance();
    private final PatientObservation patientObservation = PatientObservation.getInstance();

    private final ExamenModel patientExam = ExamenModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        tableObservation.setVisible(false);
        tableResultat.setVisible(false);
        tableRendezVous.setVisible(false);

        //Création des cellules pour les colonnes de la table
        tablePatient.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        tablePatient.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        tablePatient.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("sexePatient"));

        TableColumn<PatientConsultation, Void> buttonColum = new TableColumn<>("Envoyer");
        tablePatient.getColumns().set(3, buttonColum);
        tablePatient.getColumns().get(3).setMaxWidth(500.0);
        tablePatient.getColumns().get(3).setPrefWidth(500.0);
        tablePatient.getColumns().get(3).setMinWidth(200.0);
        buttonColum.setCellFactory(patientAccueilVoidTableColumn -> new ConsultationButton());
        tablePatient.itemsProperty().setValue(patientConsultation.getAllPatients());

        //Timer
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> tablePatient.itemsProperty().setValue(patientConsultation.getAllPatients()))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        tableObservation.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idPatient"));
        tableObservation.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        tableObservation.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("sexePatient"));
        tableObservation.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("dernierRdv"));
        tableObservation.itemsProperty().setValue(patientObservation.getAllPatients());


        //Timer
        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    tableObservation.itemsProperty().setValue(patientObservation.getAllPatients());
                })
        );
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();

        tableResultat.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("idCarnet"));
        tableResultat.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        tableResultat.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("resultatExamen"));
        TableColumn<ExamenModel, Void> see = new TableColumn<>("Voir");
        tableResultat.getColumns().set(3, see);
        tableResultat.getColumns().get(3).setMaxWidth(500.0);
        tableResultat.getColumns().get(3).setPrefWidth(500.0);
        tableResultat.getColumns().get(3).setMinWidth(200.0);
        see.setCellFactory(patientAccueilVoidTableColumn -> new ResultatAnalyseButton());
        tableResultat.itemsProperty().setValue(patientExam.getAllEndedExamens());

        //Timer
        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    tableResultat.itemsProperty().setValue(patientExam.getAllEndedExamens());
                }));
        timeline2.setCycleCount(Animation.INDEFINITE);
        timeline2.play();

        tableRendezVous.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id_rdv"));
        tableRendezVous.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("idCarnet"));
        tableRendezVous.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dateRdv"));

        tableRendezVous.itemsProperty().setValue(PatientRendezVous.getInstance().getAllRdv());


        nomPersonnelText.setText(SessionUser.getInstance().getLogin());
        observationsLabel.setOnMouseClicked(mouseEvent -> {
            tableObservation.setVisible(true);
            tablePatient.setVisible(false);
            tableResultat.setVisible(false);
            tableRendezVous.setVisible(false);

        });

        consultationLabel.setOnMouseClicked(mouseEvent -> {
            tablePatient.setVisible(true);
            tableObservation.setVisible(false);
            tableResultat.setVisible(false);
            tableRendezVous.setVisible(false);
            GridPane.setMargin(consultationLabel,new Insets(5,0,5,15));
            GridPane.setMargin(resultatLabel,new Insets(5,15,5,0));
            GridPane.setMargin(rendezVousLabel,new Insets(5,15,5,0));
            GridPane.setMargin(observationsLabel,new Insets(5,15,5,0));
            tableLabel.textProperty().setValue("Consultations à faire");
        });

        resultatLabel.setOnMouseClicked(mouseEvent -> {
            tableResultat.setVisible(true);
            tableObservation.setVisible(false);
            tablePatient.setVisible(false);
            tableRendezVous.setVisible(false);
            GridPane.setMargin(resultatLabel,new Insets(5,0,5,15));
            GridPane.setMargin(consultationLabel,new Insets(5,15,5,0));
            GridPane.setMargin(rendezVousLabel,new Insets(5,15,5,0));
            GridPane.setMargin(observationsLabel,new Insets(5,15,5,0));
            tableLabel.textProperty().setValue("Resultats d'analyse à vérifier");

        });

        rendezVousLabel.setOnMouseClicked(mouseEvent -> {
            tableRendezVous.setVisible(true);
            tableResultat.setVisible(false);
            tableObservation.setVisible(false);
            tablePatient.setVisible(false);
            GridPane.setMargin(rendezVousLabel,new Insets(5,0,5,15));
            GridPane.setMargin(resultatLabel,new Insets(5,15,5,0));
            GridPane.setMargin(consultationLabel,new Insets(5,15,5,0));
            GridPane.setMargin(observationsLabel,new Insets(5,15,5,0));
            tableLabel.textProperty().setValue("Rendez vous à venir");

        });

        observationsLabel.setOnMouseClicked(mouseEvent -> {
            tableRendezVous.setVisible(false);
            tableResultat.setVisible(false);
            tableObservation.setVisible(true);
            tablePatient.setVisible(false);
            GridPane.setMargin(observationsLabel,new Insets(5,0,5,15));
            GridPane.setMargin(resultatLabel,new Insets(5,15,5,0));
            GridPane.setMargin(rendezVousLabel,new Insets(5,15,5,0));
            GridPane.setMargin(consultationLabel,new Insets(5,15,5,0));
            tableLabel.textProperty().setValue("Patients en observation");

        });

        deconnexionButton.setOnMouseClicked(mouseEvent -> {
            Router.toConnexion();
        });

        
    }
}