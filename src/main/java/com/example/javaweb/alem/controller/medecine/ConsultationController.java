package com.example.javaweb.alem.controller.medecine;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import com.example.javaweb.alem.model.medecine.PatientObservation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsultationController implements Initializable {
    @FXML
    private TextArea acteMedTextArea;

    @FXML
    private DatePicker dateRdv;

    @FXML
    private TextArea diagnostiqueTextArea;

    @FXML
    private TextArea examenArea;

    @FXML
    private CheckBox examenBox;

    @FXML
    private TextArea observationArea;

    @FXML
    private CheckBox observationBox;


    @FXML
    private TextArea prescriptionSoinsTextArea;

    @FXML
    private TextArea prescriptionTextArea;

    @FXML
    private Button storeConsultationButton;
    private static final PatientConsultation patientConsultation = PatientConsultation.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        examenArea.setDisable(true);
        //SI ON CHOISI DE FAIRE EXAMEN ON DESACTIVE LE RESTE
        examenBox.setOnMouseClicked(mouseEvent -> {
            prescriptionTextArea.setDisable(examenBox.isSelected());
            diagnostiqueTextArea.setDisable(examenBox.isSelected());
            examenArea.setDisable(!examenBox.isSelected());
            observationBox.setDisable(examenBox.isSelected());
            acteMedTextArea.setDisable(examenBox.isSelected());
            dateRdv.setDisable(examenBox.isSelected());
            prescriptionSoinsTextArea.setDisable(examenBox.isSelected());

        });

        storeConsultationButton.setOnMouseClicked(mouseEvent -> {
            patientConsultation.setExamens(Help.checkNull(examenArea.getText()));
            patientConsultation.setActeMedical(Help.checkNull(acteMedTextArea.getText()));
            patientConsultation.setDisagnotic(Help.checkNull(diagnostiqueTextArea.getText()));
            patientConsultation.setPrescription(Help.checkNull(prescriptionTextArea.getText()));
            patientConsultation.setObservation(observationBox.isSelected() ? 1 : 0);
            patientConsultation.setPrescriptionSoins(prescriptionSoinsTextArea.getText());
            patientConsultation.enregistrerNouvellesConsultation();
        });
    }
}
