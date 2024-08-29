package com.example.javaweb.alem.model.medecine;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.model.LoginDB;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class PatientObservation {

    private final StringProperty idPatient = new SimpleStringProperty();
    private final StringProperty sexePatient = new SimpleStringProperty();

    private final StringProperty nomPatient = new SimpleStringProperty();
    private final StringProperty dernierRdv = new SimpleStringProperty();

    private final StringProperty diagnostic = new SimpleStringProperty();

    private final StringProperty examens = new SimpleStringProperty();

    private final StringProperty acteMedical = new SimpleStringProperty();

    private final StringProperty prescription = new SimpleStringProperty();

    private final IntegerProperty observation = new SimpleIntegerProperty();

    private static final StringProperty time = new SimpleStringProperty("");

    private static final StringProperty patientTime = new SimpleStringProperty();





    static {
        String query = "SELECT date_sortie_en_observation,nom_prenom FROM observations JOIN carnet ON carnet.id_carnet = observations.id_carnet WHERE date_sortie_en_observation > NOW() ORDER BY date_sortie_en_observation LIMIT 1";
        ResultSet resultSet;
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                time.setValue(resultSet.getString("date_sortie_en_observation"));
                patientTime.setValue(resultSet.getString("nom_prenom"));

                System.out.println(time.get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private static final ListProperty<PatientObservation> listeDePatient = new SimpleListProperty<>(FXCollections.observableArrayList());

    private PatientObservation(String id, String nom, String lastRdv, String sexe) {
        super();
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.dernierRdv.set(lastRdv);
        this.sexePatient.set(sexe);

    }


    public static PatientObservation getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PatientObservation INSTANCE = new PatientObservation("", "", "", "");
    }


    public ObservableList<PatientObservation> getAllPatients() {
        loadListDePatient();
        return listeDePatient.get();
    }

    private static void loadListDePatient() {
        String query = "SELECT * FROM carnet JOIN observations ON carnet.id_carnet = observations.id_carnet WHERE `date_sortie_en_observation` >= ?";
        ObservableList<PatientObservation> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            statement.setTimestamp(1, Help.timestamp());
            ResultSet resultSet = statement.executeQuery();
            String sexe;
            while (resultSet.next()) {
                sexe = "M";
                if (resultSet.getString("id_sexe").equals("F")) {
                    sexe = "F";

                }
                listPatient.add(new PatientObservation(resultSet.getString("carnet.id_carnet"), resultSet.getString("nom_prenom"), resultSet.getString("carnet.date_modification_carnet"), sexe));
            }
            statement.close();
            listeDePatient.bindContent(listPatient);
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new IllegalStateException();
        }
    }


    public void enregistrerNouvellesConsultation() {

        String query = "INSERT INTO `acte_medical`(`id_carnet`, `acte_medical`) VALUES (?,?)";
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            statement.setString(1, getIdPatient());
            statement.setString(2, getActeMedical());

            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException(sqle);
        }

        query = "INSERT INTO `diagnostique`(`id_carnet`, `diagnostique`) VALUES (?,?)";
        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query)) {
            statement.setString(1, getIdPatient());
            statement.setString(2, getDiagnostic());

            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException(sqle);
        }


        query = "INSERT INTO `consultations`(`id_carnet`, `id_personnel`, `date_consultation`, `aller_en_observation`, `examens`, `statut_consultation`, `id_diagnostique`, `id_acte_medical`) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query)) {


            //ENREGISTREMENT CONSULTATION
            statement.setString(1, getIdPatient());
            statement.setString(2, SessionUser.getInstance().getId());
            statement.setTimestamp(3, Help.timestamp());
            statement.setInt(4, getObservation());
            statement.setString(5, getExamens());
            statement.setInt(6, 1);
            statement.setString(7, Help.lastInsert("diagnostique", "id_diagnostique"));
            statement.setString(8, Help.lastInsert("acte_medical", "id_acte_medical"));

            statement.executeUpdate();

            //Recharger la liste des patients
            loadListDePatient();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        query = "INSERT INTO `prescriptions`(`id_consultation`, `prescriptions_medicaments`, `prescriptions_soins`, `heure_prescriptiion`, `statut_prescription_soins`) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query)) {
            statement.setString(1, Help.lastInsert("consultations", "id_consultation"));
            statement.setString(2, getPrescription());
            statement.setString(3, "null");
            statement.setTimestamp(4, Help.timestamp());
            statement.setInt(5, 1);

            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException(sqle);
        }

        query = "UPDATE carnet SET statut_constante = 0 WHERE id_carnet = ?";
        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query)) {
            statement.setString(1, getIdPatient());

            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException(sqle);
        }
    }

    public static StringProperty getTimeElapsed() throws Exception {




        LocalDateTime dateBD = !time.get().isEmpty() ? LocalDateTime.ofInstant(Timestamp.valueOf(time.get()).toInstant(), ZoneId.systemDefault()): LocalDateTime.now();

        // Obtenez la date et l'heure actuelles
        LocalDateTime now = LocalDateTime.now();

        // Calculer la différence entre la date actuelle et la date de la base de données
        Duration duration = Duration.between(now, dateBD);

        // Calculer les jours, heures, minutes et secondes
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        // Construire la chaîne de décompte
        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append(" j").append(" ");
        }
        if (hours > 0 || days > 0) {
            result.append(hours).append(" h").append(" ");
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            result.append(minutes).append(" m").append(" ");
        }
        result.append(seconds).append(" s");

        return new SimpleStringProperty(result.toString());
    }


    // Getters and Setters pour pouvoirs afficher les données de la bdd et récupérer des cellules
    public String getIdPatient() {
        return idPatient.get();
    }

    public StringProperty idPatientProperty() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient.set(idPatient);
    }

    public String getSexePatient() {
        return sexePatient.get();
    }

    public StringProperty sexeProperty() {
        return sexePatient;
    }

    public void setSexePatient(String idPatient) {
        this.sexePatient.set(idPatient);
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

    public String getDernierRdv() {
        return dernierRdv.get();
    }

    public StringProperty dernierRdvProperty() {
        return dernierRdv;
    }

    public void setDernierRdv(String dernierRdv) {
        this.dernierRdv.set(dernierRdv);
    }

    public String getExamens() {
        return this.examens.get();
    }

    public void setExamens(String examens) {
        this.examens.set(examens);
    }

    public StringProperty examensProperty() {
        return this.examens;
    }

    public String getDiagnostic() {
        return this.diagnostic.get();
    }

    public void setDisagnotic(String diagnostique) {
        this.diagnostic.set(diagnostique);
    }

    public StringProperty diagnosticProperty() {
        return this.diagnostic;
    }

    public String getPrescription() {
        return this.prescription.get();
    }

    public void setPrescription(String prescription) {
        this.prescription.set(prescription);
    }

    public StringProperty prescriptionProperty() {
        return this.prescription;
    }

    public String getActeMedical() {
        return this.acteMedical.get();
    }

    public void setActeMedical(String acteMedical) {
        this.acteMedical.set(acteMedical);
    }

    public StringProperty acteMedicalProperty() {
        return this.acteMedical;
    }

    public int getObservation() {
        return this.observation.get();
    }

    public StringProperty getPatientTime() {
        return patientTime;
    }

    public void setObservation(int observation) {
        this.observation.set(observation);
    }

    public IntegerProperty observationProperty() {
        return this.observation;
    }


}
