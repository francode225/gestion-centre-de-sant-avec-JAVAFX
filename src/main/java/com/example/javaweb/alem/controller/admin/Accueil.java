package com.example.javaweb.alem.controller.admin;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.core.MedecinCrudButton;
import com.example.javaweb.alem.core.SoinsButton;
import com.example.javaweb.alem.model.admin.PersonnelModel;
import com.example.javaweb.alem.model.admin.PersonnelModel;
import com.example.javaweb.alem.model.infirmerie.PatientInfirmerie;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Date;

public class Accueil {

    @FXML
    private ImageView dashboard;

    @FXML
    private ImageView deconnecter;

    @FXML
    private PieChart diagramme1;

    @FXML
    private ImageView infirmier;

    @FXML
    private ImageView laboratoire;

    @FXML
    private ImageView medecin;

    @FXML
    private ImageView parametre;

    @FXML
    private ImageView patient;

    @FXML
    private GridPane view_infirmier;

    @FXML
    private GridPane view_laboratoire;

    @FXML
    private GridPane view_medecin;

    @FXML
    private GridPane view_patient;

    @FXML
    private GridPane view_principal;

    @FXML
    private AnchorPane view_principale1;

    @FXML
    private Pane texthover;

    @FXML
    private Pane texthover1;

    @FXML
    private Pane texthover11;

    @FXML
    private Pane texthover111;

    @FXML
    private Pane texthover112;

    @FXML
    private TableView<PersonnelModel> tableauMedecin;

    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        // Créer des données pour le PieChart
        PieChart.Data slice1 = new PieChart.Data("Médécin", 50);
        PieChart.Data slice2 = new PieChart.Data("Infirmier", 30);
        PieChart.Data slice3 = new PieChart.Data("Tech de labo", 20);

        // Ajouter les données au PieChart
        diagramme1.getData().add(slice1);
        diagramme1.getData().add(slice2);
        diagramme1.getData().add(slice3);

        // Optionnel: Configurer d'autres propriétés du PieChart
        diagramme1.setTitle("Répartition des employés par poste");
        diagramme1.setLabelsVisible(true);

        diagramme1.getData().forEach(data -> {
            switch (data.getName()) {
                case "Médécin":
                    data.getNode().setStyle("-fx-pie-color: #4387CE ;");
                    break;
                case "Infirmier":
                    data.getNode().setStyle("-fx-pie-color: #70FFA5 ;");
                    break;
                case "Tech de labo":
                    data.getNode().setStyle("-fx-pie-color: #FFFFFF;");
                    break;
            }
        });

        dashboard.setOnMouseClicked(mouseEvent -> {
            view_principal.setVisible(true);
            view_infirmier.setVisible(false);
            view_medecin.setVisible(false);
            view_patient.setVisible(false);
            view_laboratoire.setVisible(false);
        });

        dashboard.setOnMouseEntered(event -> {
            texthover.setVisible(true);
        });

        dashboard.setOnMouseExited(event -> {
            texthover.setVisible(false);
        });

        medecin.setOnMouseClicked(mouseEvent -> {
            view_principal.setVisible(false);
            view_infirmier.setVisible(false);
            view_medecin.setVisible(true);
            view_patient.setVisible(false);
            view_laboratoire.setVisible(false);
        });

        medecin.setOnMouseEntered(event -> {
            texthover1.setVisible(true);
        });

        medecin.setOnMouseExited(event -> {
            texthover1.setVisible(false);
        });

        infirmier.setOnMouseClicked(mouseEvent -> {
            view_principal.setVisible(false);
            view_infirmier.setVisible(true);
            view_medecin.setVisible(false);
            view_patient.setVisible(false);
            view_laboratoire.setVisible(false);
        });

        infirmier.setOnMouseEntered(event -> {
            texthover11.setVisible(true);
        });

        infirmier.setOnMouseExited(event -> {
            texthover11.setVisible(false);
        });

        patient.setOnMouseClicked(mouseEvent -> {
            view_principal.setVisible(false);
            view_infirmier.setVisible(false);
            view_medecin.setVisible(false);
            view_patient.setVisible(true);
            view_laboratoire.setVisible(false);
        });

        patient.setOnMouseEntered(event -> {
            texthover111.setVisible(true);
        });

        patient.setOnMouseExited(event -> {
            texthover111.setVisible(false);
        });

        laboratoire.setOnMouseClicked(mouseEvent -> {
            view_principal.setVisible(false);
            view_infirmier.setVisible(false);
            view_medecin.setVisible(false);
            view_patient.setVisible(false);
            view_laboratoire.setVisible(true);
        });

        laboratoire.setOnMouseEntered(event -> {
            texthover112.setVisible(true);
        });

        laboratoire.setOnMouseExited(event -> {
            texthover112.setVisible(false);
        });

        // button ajouter //

        addButton.setOnMouseClicked(mouseEvent -> {
            try {
                Help.doorPen("/com/example/javaweb/yeoviews/form_personnel_add.fxml","Formulaire");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });

        //Tableau medecin//

        tableauMedecin.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nomPrenoms"));
        tableauMedecin.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));
        tableauMedecin.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dateCreationPersonnel"));

        //Les boutons sont configurés dans le dossier core

        TableColumn<PersonnelModel, Void> buttonModifier = new TableColumn<>("Modifier");
        tableauMedecin.getColumns().set(3, buttonModifier);
        tableauMedecin.getColumns().get(3).setMaxWidth(500.0);
        tableauMedecin.getColumns().get(3).setPrefWidth(500.0);
        tableauMedecin.getColumns().get(3).setMinWidth(200.0);

        buttonModifier.setCellFactory(patientAccueilVoidTableColumn -> {
            return new MedecinCrudButton(MedecinCrudButton.Action.SET);
        });

        TableColumn<PersonnelModel, Void> buttonSupprimer = new TableColumn<>("Supprimer");
        tableauMedecin.getColumns().set(4, buttonSupprimer);
        tableauMedecin.getColumns().get(4).setMaxWidth(500.0);
        tableauMedecin.getColumns().get(4).setPrefWidth(500.0);
        tableauMedecin.getColumns().get(4).setMinWidth(200.0);

        buttonSupprimer.setCellFactory(patientAccueilVoidTableColumn -> {
            return new MedecinCrudButton(MedecinCrudButton.Action.DEL);
        });

        tableauMedecin.setItems(PersonnelModel.getListeMedecin());

        // FIN TABLEAU MEDECIN //



    }

}
