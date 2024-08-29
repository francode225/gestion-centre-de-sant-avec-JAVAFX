package com.example.javaweb.alem.controller.medecine;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class AfterAnalyseController implements Initializable {

    @FXML
    private TextArea acteMedTextArea;

    @FXML
    private TextArea diagnostiqueTextArea;

    @FXML
    private TextArea examenArea;

    @FXML
    private Label nomPrenomLabel;

    @FXML
    private CheckBox observationBox;

    @FXML
    private TextArea observationTextArea;

    @FXML
    private TextArea prescriptionTextArea;

    @FXML
    private TextArea prescriptionSoinsTextArea;

    @FXML
    private TextArea resultatTextArea;

    @FXML
    private Button storeConsultationButton;

    private static ExamenModel examenModel = ExamenModel.getInstance();

    private static PatientConsultation patientConsultation = PatientConsultation.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acteMedTextArea.setText(examenModel.getPatientConsultation().getActeMedical());
        diagnostiqueTextArea.setText(examenModel.getPatientConsultation().getDiagnostic());
        examenArea.setText(examenModel.getResultatExamen());
        nomPrenomLabel.setText(examenModel.getPatientConsultation().getNomPatient());
        observationBox.setSelected(examenModel.getObservationExamen().equals("1"));
        observationTextArea.setText(examenModel.getObservationExamen());
        prescriptionTextArea.setText(examenModel.getPatientConsultation().getPrescription());
        prescriptionSoinsTextArea.setText(examenModel.getPatientConsultation().getPrescriptionSoins());
        resultatTextArea.setText(examenModel.getResultatExamen());

        storeConsultationButton.setOnMouseClicked(mouseEvent -> {
            patientConsultation.setExamens(Help.checkNull(examenArea.getText()));
            patientConsultation.setActeMedical(Help.checkNull(acteMedTextArea.getText()));
            patientConsultation.setDisagnotic(Help.checkNull(diagnostiqueTextArea.getText()));
            patientConsultation.setPrescription(Help.checkNull(prescriptionTextArea.getText()));
            patientConsultation.setObservation(observationBox.isSelected() ? 1 : 0);
            patientConsultation.setPrescriptionSoins(prescriptionSoinsTextArea.getText());
            patientConsultation.enregistrerAncienneConsultation();
        });
    }
}
