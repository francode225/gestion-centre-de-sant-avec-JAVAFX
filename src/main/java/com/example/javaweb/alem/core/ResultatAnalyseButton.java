package com.example.javaweb.alem.core;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.io.IOException;

public class ResultatAnalyseButton extends TableCell<ExamenModel, Void> {
    private final Button button = new Button("Voir");


    public ResultatAnalyseButton(){
        this.button.setAlignment(Pos.CENTER);
        this.button.setOnMouseClicked(mouseEvent -> {
            try {
                ExamenModel.getInstance().copy(getTableView().getItems().get(getIndex()));
                PatientConsultation.getInstance().setIdPatient(String.valueOf(ExamenModel.getInstance().getIdCarnet()));
                Help.doorPen("/com/example/javaweb/yeoviews/form_apres_analyse.fxml","Resultat Analyse");
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
