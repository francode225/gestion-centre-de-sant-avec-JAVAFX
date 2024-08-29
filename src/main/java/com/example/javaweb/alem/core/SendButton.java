package com.example.javaweb.alem.core;

import com.example.javaweb.alem.model.secretariat.PatientAccueil;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class SendButton extends TableCell<PatientAccueil, Void> {
    private final Button button = new Button("Send");


    public SendButton(){
        this.button.setOnMouseClicked(mouseEvent -> {
            PatientAccueil person = getTableView().getItems().get(getIndex());
            person.enregistrerNouvellesConstantes();
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
