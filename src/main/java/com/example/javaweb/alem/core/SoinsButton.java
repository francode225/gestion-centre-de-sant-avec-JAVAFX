package com.example.javaweb.alem.core;

import com.example.javaweb.alem.model.infirmerie.PatientInfirmerie;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;

public class SoinsButton extends TableCell<PatientInfirmerie, Void> {
    private final Button button = new Button("Fournir soins");


    public SoinsButton(){
        this.button.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        this.button.setPrefWidth(150);
        this.button.setOnMouseClicked(mouseEvent -> {
            PatientInfirmerie patientInfirmerie = getTableView().getItems().get(getIndex());
            PatientInfirmerie.getInstance().setSoins(patientInfirmerie.getSoins());
            PatientInfirmerie.getInstance().setIdPrescription(patientInfirmerie.getIdPrescription());
            PatientInfirmerie.getInstance().setIdPatient(patientInfirmerie.getIdPatient());
            PatientInfirmerie.getInstance().setNomPatient(patientInfirmerie.getNomPatient());
            PatientInfirmerie.getInstance().setDernierRdv(patientInfirmerie.getDernierRdv());
            PatientInfirmerie.getInstance().setStatutConstante(patientInfirmerie.getStatutConstante());
            PatientInfirmerie.getInstance().setSexePatient(patientInfirmerie.getSexePatient());
            try {
                Help.doorPen("/com/example/javaweb/yeoviews/form_Soins.fxml","Consultation");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}
