package com.example.javaweb.alem.core;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;

public class ExamenButton extends TableCell<ExamenModel, Void> {
    private final Button button = new Button("Send");


    public ExamenButton(){
        this.button.setOnMouseClicked(mouseEvent -> {
            try {
                ExamenModel.getInstance().copy(getTableView().getItems().get(getIndex()));
                Help.doorPen("/com/example/javaweb/yeoviews/form_laboratoire.fxml","Analyse Labo");
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
