package com.example.javaweb.alem.model.infirmerie;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PatientInfirmerie {

    private final StringProperty idPrescription = new SimpleStringProperty();
    private final StringProperty idPatient = new SimpleStringProperty();
    private final StringProperty nomPatient = new SimpleStringProperty();
    private final StringProperty sexePatient = new SimpleStringProperty();
    private final StringProperty dernierRdv = new SimpleStringProperty();
    private final StringProperty statutConstante = new SimpleStringProperty();

    private final StringProperty soins = new SimpleStringProperty();

    private static final ListProperty<PatientInfirmerie> listeDePatients = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static final ListProperty<PatientInfirmerie> listeDeSoins = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static PatientInfirmerie getInstance() {

        return PatientInfirmerie.HOLDER.INSTANCE;
    }

    private static class HOLDER {
        private static final PatientInfirmerie INSTANCE = new PatientInfirmerie();
    }

    PatientInfirmerie(){

    }

    public PatientInfirmerie(String id, String nom, String sexe, String dernierRdv, String statutConstante) {
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.sexePatient.set(sexe);
        this.dernierRdv.set(dernierRdv);
        this.statutConstante.set(statutConstante);
    }

    public PatientInfirmerie(String id, String nom,String soins,String idPrescription) {
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.idPrescription.set(idPrescription);
        this.soins.set(soins);
    }

    public void storeSoins(){
        String query = "UPDATE prescriptions SET statut_prescription_soins = 1 WHERE id_prescription = "+PatientInfirmerie.getInstance().getIdPrescription();
        try(PreparedStatement preparedStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)){
            System.out.println(preparedStatement
            );
            preparedStatement.executeUpdate();
            NotificationSuccesModel.getInstance().setMessage("Soins terminés pour le patient\n"+getNomPatient());
            Help.enregistrementOk();
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            throw new IllegalStateException(e);
        }
    }

    public ObservableList<PatientInfirmerie> getPatientsWithStatutConstanteZero() {
        loadPatientsWithStatutConstanteZero();
        return listeDePatients.get();
    }

    public ObservableList<PatientInfirmerie> getPatientsSoins() {
        loadPatientsASoigner();
        return listeDeSoins.get();
    }

    public static IntegerProperty nombreConstantes(){
        return new SimpleIntegerProperty(listeDePatients.size());
    }
    public static IntegerProperty nombreSoins(){
        return new SimpleIntegerProperty(listeDeSoins.size());
    }

    private static void loadPatientsWithStatutConstanteZero() {
        String query = "SELECT * FROM carnet JOIN constantes_patient ON carnet.id_carnet = constantes_patient.id_carnet WHERE statut_constante = 0 AND date_prise_constantes IS NULL";
        ObservableList<PatientInfirmerie> listPatients = FXCollections.observableArrayList();

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String sexe = resultSet.getString("id_sexe").equals("F") ? "F" : "M";
                listPatients.add(new PatientInfirmerie(
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

    private static void loadPatientsASoigner() {
        String query = "SELECT * FROM carnet JOIN consultations ON carnet.id_carnet = consultations.id_carnet JOIN prescriptions ON prescriptions.id_consultation = consultations.id_consultation  WHERE prescriptions_soins != '' AND statut_prescription_soins = 0";
        ObservableList<PatientInfirmerie> listS = FXCollections.observableArrayList();

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                listS.add(new PatientInfirmerie(
                        resultSet.getString("id_carnet"),
                        resultSet.getString("nom_prenom"),
                        resultSet.getString("prescriptions_soins"),
                        resultSet.getString("id_prescription")
                ));
            }
            listeDeSoins.setAll(listS);
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

    public String getIdPrescription() {
        return idPrescription.get();
    }

    public StringProperty idPrescriptionProperty() {
        return idPrescription;
    }

    public void setIdPrescription(String idPrescription) {
        this.idPrescription.set(idPrescription);
    }

    public String getSoins() {
        return this.soins.get();
    }

    public StringProperty soinsProperty() {
        return this.soins;
    }

    public void setSoins(String soins) {
        this.soins.set(soins);
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
