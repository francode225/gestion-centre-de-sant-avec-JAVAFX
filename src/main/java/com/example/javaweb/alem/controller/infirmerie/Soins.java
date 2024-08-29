package com.example.javaweb.alem.controller.infirmerie;

import com.example.javaweb.alem.model.infirmerie.PatientInfirmerie;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Soins implements Initializable {

    @FXML
    private TextArea soinsArea;


    @FXML
    private Button storeConstanteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        soinsArea.setEditable(true);
        soinsArea.textProperty().setValue(PatientInfirmerie.getInstance().getSoins());
        soinsArea.setEditable(false);

        storeConstanteButton.setOnMouseClicked(mouseEvent -> {
            PatientInfirmerie.getInstance().storeSoins();
        });
    }
}
