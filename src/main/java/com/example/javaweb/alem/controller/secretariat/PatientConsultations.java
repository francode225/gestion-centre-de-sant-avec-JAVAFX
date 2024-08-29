package com.example.javaweb.alem.controller.secretariat;

import com.example.javaweb.alem.model.LoginDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientConsultations {

    private final StringProperty idPatient = new SimpleStringProperty();
    private final StringProperty nomPatient = new SimpleStringProperty();
    private final StringProperty sexePatient = new SimpleStringProperty();
    private final StringProperty dernierRdv = new SimpleStringProperty();
    private final StringProperty statutConstante = new SimpleStringProperty();

    private static final ObservableList<PatientConsultations> listeDePatients = FXCollections.observableArrayList();

    public PatientConsultations(String id, String nom, String sexe, String dernierRdv, String statutConstante) {
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.sexePatient.set(sexe);
        this.dernierRdv.set(dernierRdv);
        this.statutConstante.set(statutConstante);
    }

    public static ObservableList<PatientConsultations> getPatientsWithStatutConstanteUn() {
        loadPatientsWithStatutConstanteUn();
        return listeDePatients;
    }

    private static void loadPatientsWithStatutConstanteUn() {
        String query = "SELECT * FROM carnet WHERE statut_constante = 1";
        ObservableList<PatientConsultations> listPatients = FXCollections.observableArrayList();

        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String sexe = resultSet.getString("id_sexe").equals("F") ? "F" : "M";
                listPatients.add(new PatientConsultations(
                        resultSet.getString("id_carnet"),
                        resultSet.getString("nom_prenom"),
                        sexe,
                        resultSet.getString("date_modification_carnet"),
                        resultSet.getString("statut_constante")
                ));
            }
            listeDePatients.setAll(listPatients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Getters and Setters
    public String getIdPatient() {
        return idPatient.get();
    }

    public StringProperty idPatientProperty() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient.set(idPatient);
    }

    public String getNomPatient() {
        return nomPatient.get();
    }

    public StringProperty nomPatientProperty() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient.set(nomPatient);
    }

    public String getSexePatient() {
        return sexePatient.get();
    }

    public StringProperty sexePatientProperty() {
        return sexePatient;
    }

    public void setSexePatient(String sexePatient) {
        this.sexePatient.set(sexePatient);
    }

    public String getDernierRdv() {
        return dernierRdv.get();
    }

    public StringProperty dernierRdvProperty() {
        return dernierRdv;
    }

    public void setDernierRdv(String dernierRdv) {
        this.dernierRdv.set(dernierRdv);
    }

    public String getStatutConstante() {
        return statutConstante.get();
    }

    public StringProperty statutConstanteProperty() {
        return statutConstante;
    }

    public void setStatutConstante(String statutConstante) {
        this.statutConstante.set(statutConstante);
    }
}
