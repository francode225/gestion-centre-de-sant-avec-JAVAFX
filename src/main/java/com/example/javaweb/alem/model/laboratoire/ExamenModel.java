package com.example.javaweb.alem.model.laboratoire;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import com.example.javaweb.alem.model.medecine.PatientConsultation;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ExamenModel {


    private final IntegerProperty idCarnet = new SimpleIntegerProperty();

    private final StringProperty nomPrenom = new SimpleStringProperty();

    private final StringProperty examen = new SimpleStringProperty();

    private final StringProperty idExamen = new SimpleStringProperty();
    private final IntegerProperty idConsultation = new SimpleIntegerProperty();

    private final StringProperty heureExamen = new SimpleStringProperty();

    private final StringProperty resultatExamen = new SimpleStringProperty();

    private final StringProperty statutExamen = new SimpleStringProperty();

    private final StringProperty typeExamen = new SimpleStringProperty();

    private final StringProperty observationExamen = new SimpleStringProperty();

    private PatientConsultation patientConsultation;


    private static final ListProperty<ExamenModel> listeExamen = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static final ListProperty<ExamenModel> listeExamenFini = new SimpleListProperty<>(FXCollections.observableArrayList());


    public static ExamenModel getInstance() {
        return HOLDER.INSTANCE;
    }

    private static class HOLDER {
        private static final ExamenModel INSTANCE = new ExamenModel();
    }

    ExamenModel() {
    }

    public ObservableList<ExamenModel> getAllExamens() {
        loadListDeCarnetAttendantsAnalyse();
        return listeExamen.get();
    }


    public ObservableList<ExamenModel> getAllEndedExamens() {
        loadListCarnetFiniAnalyse();
        return listeExamenFini.get();
    }

    public static void loadListDeCarnetAttendantsAnalyse() {
        String query = "SELECT * FROM carnet JOIN consultations ON carnet.id_carnet = consultations.id_carnet JOIN examens ON consultations.id_consultation = examens.id_consultation WHERE consultations.examens != '' AND statut_examen = 0 AND statut_consultation = 1";
        ObservableList<ExamenModel> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
            System.out.println(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                listPatient.add(new ExamenModel(resultSet.getInt("carnet.id_carnet"), resultSet.getString("nom_prenom"), resultSet.getString("examens"), resultSet.getInt("id_consultation")));
            }
            listeExamen.bindContent(listPatient);
            System.out.println(listeExamen);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    ExamenModel(int id, String nomPrenom, String exam, int idCons) {
        this.idCarnet.set(id);
        this.nomPrenom.set(nomPrenom);
        this.examen.set(exam);
        this.idConsultation.set(idCons);

    }

    ExamenModel(int id, String nomPrenom, String exam, int idCons, String resultats, String typeExamen, String observations, PatientConsultation p) {
        this.idCarnet.set(id);
        this.nomPrenom.set(nomPrenom);
        this.examen.set(exam);
        this.idConsultation.set(idCons);
        this.resultatExamen.set(resultats);
        this.typeExamen.set(typeExamen);
        this.observationExamen.set(observations);
        this.patientConsultation = p;

    }

    public void copy(ExamenModel e) {
        setIdCarnet(e.getIdCarnet());
        setNomPrenom(e.getNomPrenom());
        setExamen(e.getExamen());
        this.patientConsultation = e.patientConsultation;
        setObservationExamen(e.getObservationExamen());
        setTypeExamen(e.getTypeExamen());
        setResultatExamen(e.getResultatExamen());
        setStatutExamen(e.getStatutExamen());
        setIdConsultation(e.getIdConsultation());
    }


    public static void loadListCarnetFiniAnalyse() {
        String query = "SELECT * FROM examens JOIN consultations ON examens.id_consultation = consultations.id_consultation JOIN carnet ON carnet.id_carnet = consultations.id_carnet WHERE statut_examen = 1 AND statut_consultation = 0 ";
        ObservableList<ExamenModel> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PatientConsultation patient = new PatientConsultation();
                patient.findOneConsultation(resultSet.getInt("id_consultation"));
                listPatient.add(
                        new ExamenModel(resultSet.getInt("carnet.id_carnet"),
                                resultSet.getString("nom_prenom"),
                                resultSet.getString("examens"),
                                resultSet.getInt("id_consultation"),
                                resultSet.getString("resultat_examen"),
                                resultSet.getString("type_examen"),
                                resultSet.getString("observation_examen"),
                                patient));
            }
            listeExamenFini.bindContent(listPatient);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    //FONCTION D'INSERTION DE D'ANALYSE
    public void storeExamen() {

        try (PreparedStatement indexStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement("UPDATE `examens` SET `heure_examen` = ?, `resultat_examen` = ?, `statut_examen` = ?, `type_examen` = ?, `observation_examen` = ? WHERE id_consultation = ?")) {
            indexStatement.setTimestamp(1, Help.timestamp());
            indexStatement.setString(2, getResultatExamen());
            indexStatement.setInt(3, 1);
            indexStatement.setString(4, getTypeExamen());
            indexStatement.setString(5, getObservationExamen());
            indexStatement.setInt(6, getIdConsultation());

            indexStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        try (PreparedStatement indexStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement("UPDATE `consultations` SET `statut_consultation` = 0 WHERE id_consultation = ? ");) {
            indexStatement.setInt(1, getIdConsultation());
            indexStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        NotificationSuccesModel.getInstance().setMessage("Analyse terminé "+ SessionUser.getInstance().getNomPrenom());
        Help.enregistrementOk();
    }

    /*public void findOneAnalyse() {
        String query = "SELECT * FROM carnet JOIN consultations ON carnet.id_carnet = consultations.id_carnet JOIN examens ON examens.id_consultation = consultations.id_consultation  WHERE id_consultations = ?";
        ObservableList<ExamenModel> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
            statement.setInt(1, getIdConsultation());
            ResultSet resultSet = statement.executeQuery();
            String sexe = "";
            while (resultSet.next()) {
                sexe = "M";
                if (resultSet.getString("id_sexe").equals("F")) {
                    sexe = "F";

                }
                listPatient.add(new ExamenModel(resultSet.getInt("carnet.id_carnet"), resultSet.getString("nom_prenom"), resultSet.getString("examens"), resultSet.getInt("id_consultation")));
            }
            listeExamen.bindContent(listPatient);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }*/

    public void setIdCarnet(int id) {
        this.idCarnet.set(id);
    }

    public int getIdCarnet() {
        return idCarnet.get();
    }

    public IntegerProperty idCarnetProperty() {
        return idCarnet;
    }


    // Setter et getter pour nomPrenom
    public void setNomPrenom(String nom) {
        this.nomPrenom.set(nom);
    }

    public String getNomPrenom() {
        return nomPrenom.get();
    }

    public StringProperty nomPrenomProperty() {
        return nomPrenom;
    }

    // Setter et getter pour examen
    public void setExamen(String examen) {
        this.examen.set(examen);
    }

    public String getExamen() {
        return examen.get();
    }

    public StringProperty examenProperty() {
        return examen;
    }


    // Setter et getter pour nom
    public void setIdExamen(String idExamen) {
        this.idExamen.set(idExamen);
    }

    public String getIdExamen() {
        return idExamen.get();
    }

    public StringProperty idExamenProperty() {
        return idExamen;
    }

    // Setter et getter pour prenom
    public void setIdConsultation(int idConsultation) {
        this.idConsultation.set(idConsultation);
    }

    public int getIdConsultation() {
        return idConsultation.get();
    }

    public IntegerProperty idConsultationProperty() {
        return idConsultation;
    }

    // Setter et getter pour email
    public void setHeureExamen(String heureExamen) {
        this.heureExamen.set(heureExamen);
    }

    public String getHeureExamen() {
        return heureExamen.get();
    }

    public StringProperty heureExamenProperty() {
        return heureExamen;
    }

    // Setter et getter pour adresse
    public void setResultatExamen(String resultatExamen) {
        this.resultatExamen.set(resultatExamen);
    }

    public String getResultatExamen() {
        return resultatExamen.get();
    }

    public StringProperty resultatExamenProperty() {
        return resultatExamen;
    }

    // Setter et getter pour numero
    public void setStatutExamen(String statutExamen) {
        this.statutExamen.set(statutExamen);
    }

    public String getStatutExamen() {
        return statutExamen.get();
    }

    public StringProperty statutExamenProperty() {
        return statutExamen;
    }

    // Setter et getter pour lieuNaissance
    public void setTypeExamen(String typeExamen) {
        this.typeExamen.set(typeExamen);
    }

    public String getTypeExamen() {
        return typeExamen.get();
    }

    public StringProperty typeExamenProperty() {
        return typeExamen;
    }

    // Setter et getter pour groupeSanguin
    public void setObservationExamen(String observationExamen) {
        this.observationExamen.set(observationExamen);
    }

    public String getObservationExamen() {
        return observationExamen.get();
    }

    public StringProperty observationExamenProperty() {
        return observationExamen;
    }

    public PatientConsultation getPatientConsultation() {
        return patientConsultation;
    }


}
