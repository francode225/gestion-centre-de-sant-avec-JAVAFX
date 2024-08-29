package com.example.javaweb.alem.core;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;

public class ConsultationButton extends TableCell<PatientConsultation, Void> {
    private final Button button = new Button("Consulter");


    public ConsultationButton(){
        this.button.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        this.button.setPrefWidth(150);
        this.button.setOnMouseClicked(mouseEvent -> {
            PatientConsultation person = getTableView().getItems().get(getIndex());
            PatientConsultation.getInstance().setIdPatient(person.getIdPatient());
            PatientConsultation.getInstance().setIdConsultation(person.getIdConsultation());
            try {
                Help.doorPen("/com/example/javaweb/yeoviews/form_consultation.fxml","Consultation");
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
