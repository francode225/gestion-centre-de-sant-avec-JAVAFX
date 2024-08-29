package com.example.javaweb.alem.controller.laboratoire;

import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class AnalyseController implements Initializable {

    @FXML
    private TextArea analyse;

    @FXML
    private TextArea observationArea;

    @FXML
    private TextArea typeAnalyseArea;

    @FXML
    private Button storeAnalyseButton;

    @FXML
    private static final ExamenModel examenModel = ExamenModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        storeAnalyseButton.setOnMouseClicked(mouseEvent -> {
            examenModel.setResultatExamen(analyse.getText());
            examenModel.setObservationExamen(observationArea.getText());
            examenModel.setTypeExamen(typeAnalyseArea.getText());
            examenModel.storeExamen();
        });
    }
}
