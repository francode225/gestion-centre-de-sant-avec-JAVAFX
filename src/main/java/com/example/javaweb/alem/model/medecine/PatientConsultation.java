package com.example.javaweb.alem.model.medecine;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import com.example.javaweb.alem.model.laboratoire.ExamenModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class PatientConsultation {

    private final StringProperty idConsultation = new SimpleStringProperty();
    private final StringProperty idPatient = new SimpleStringProperty();
    private final StringProperty sexePatient = new SimpleStringProperty();

    private final StringProperty nomPatient = new SimpleStringProperty();
    private final StringProperty dernierRdv = new SimpleStringProperty();

    private final IntegerProperty typePatient = new SimpleIntegerProperty();

    private final StringProperty diagnostic = new SimpleStringProperty();

    private final StringProperty examens = new SimpleStringProperty();

    private final StringProperty acteMedical = new SimpleStringProperty();

    private final StringProperty prescription = new SimpleStringProperty();

    private final StringProperty prescriptionSoins = new SimpleStringProperty();


    private final IntegerProperty observation = new SimpleIntegerProperty();

    private final StringProperty rendezVous = new SimpleStringProperty();


    private static final ListProperty<PatientConsultation> listeDePatient = new SimpleListProperty<>(FXCollections.observableArrayList());


    private PatientConsultation(String id, String nom, String lastRdv, String sexe, int typePatient,String idConsultation) {
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.dernierRdv.set(lastRdv);
        this.sexePatient.set(sexe);
        this.typePatient.set(typePatient);
        this.idConsultation.set(idConsultation);
    }

    public PatientConsultation() {

    }


    public static PatientConsultation getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PatientConsultation INSTANCE = new PatientConsultation("", "", "", "", 0,"");
    }

    public ObservableList<PatientConsultation> getAllPatients() {
        loadListDePatient();
        return listeDePatient.get();
    }

    private static void loadListDePatient() {
        String query = "SELECT * FROM carnet JOIN consultations ON carnet.id_carnet = consultations.id_carnet  WHERE statut_constante = ? AND statut_consultation = ? AND date_consultation IS NULL";
        ObservableList<PatientConsultation> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            statement.setInt(1, 1);
            statement.setInt(2, 0);
            ResultSet resultSet = statement.executeQuery();

            String sexe = "";
            while (resultSet.next()) {
                sexe = "M";
                if (resultSet.getString("id_sexe").equals("F")) {
                    sexe = "F";
                }
                listPatient.add(new PatientConsultation(resultSet.getString("carnet.id_carnet"), resultSet.getString("nom_prenom"), resultSet.getString("carnet.date_modification_carnet"), sexe, resultSet.getInt("id_type_patient"),resultSet.getString("id_consultation")));
            }
            listeDePatient.bindContent(listPatient);
            statement.close();
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new IllegalStateException(e);
        }
    }

    /**
     * Cette fonction enregistre une nouvelle prise de constante
     */

    public void findOneConsultation(int id) {
        String query = "SELECT * FROM consultations WHERE id_consultation = " + id;

        try (PreparedStatement preparedStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                setExamens(resultSet.getString("examens"));
                setPrescription(Help.getElementsOfTable("prescriptions", "prescriptions_medicaments", "id_consultation", Integer.toString(id)));
                setDisagnotic(Help.getElementsOfTable("diagnostique", "diagnostique", "id_consultation", Integer.toString(id)));
                setActeMedical(Help.getElementsOfTable("acte_medical", "acte_medical", "id_consultation", Integer.toString(id)));
                setObservation(resultSet.getInt("aller_en_observation"));

            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * La fonction est utilisée pour enregistrer de nouvelles Consultation
     */

    public void enregistrerNouvellesConsultation() {

        String query = "";
        if (!getActeMedical().isEmpty()) {
            query = "INSERT INTO `acte_medical`(`id_carnet`, `acte_medical`,id_consultation) VALUES (?,?,?)";
            try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(2, getActeMedical());
                statement.setString(3, getIdConsultation());

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }

        if (!getDiagnostic().isEmpty()) {
            query = "INSERT INTO `diagnostique`(`id_carnet`, `diagnostique`,id_consultation) VALUES (?,?,?)";
            try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(2, getDiagnostic());
                statement.setString(3, getIdConsultation());


                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }

        }

        if(getRendezVous()!=null){
            query = "INSERT INTO `rendezvous`(`date_rdv`, `id_carnet`,`id_consultation`) VALUES (?,?,?)";
            try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
                statement.setString(1, getRendezVous());
                statement.setString(2, getIdPatient());
                statement.setString(3, getIdConsultation());

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }


        query = "UPDATE `consultations` SET `id_personnel` = ?, `date_consultation` = ?, `aller_en_observation` = ?, `examens` = ?, `statut_consultation` = ?, `id_diagnostique` = ?, `id_acte_medical` = ? WHERE id_carnet = ? AND date_consultation IS NULL ";
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {


            //ENREGISTREMENT CONSULTATION
            statement.setString(1, SessionUser.getInstance().getId());
            statement.setTimestamp(2, Help.timestamp());
            statement.setInt(3, getObservation());
            statement.setString(4, getExamens());
            statement.setInt(5, 1);
            statement.setString(6, getDiagnostic().isEmpty() ? null : Help.lastInsert("diagnostique", "id_diagnostique"));
            statement.setString(7, getActeMedical().isEmpty() ? null : Help.lastInsert("acte_medical", "id_acte_medical"));
            statement.setString(8, getIdPatient());

            System.out.println(statement);
            statement.executeUpdate();

            //Recharger la liste des patients
            loadListDePatient();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            throw new IllegalStateException(e);
        }

        if (!getPrescription().isEmpty()) {
            query = "INSERT INTO `prescriptions`(`id_consultation`, `prescriptions_medicaments`, `prescriptions_soins`, `heure_prescriptiion`, `statut_prescription_soins`) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, getIdConsultation());
                statement.setString(2, getPrescription());
                statement.setString(3, getPrescriptionSoins());
                statement.setTimestamp(4, Help.timestamp());
                statement.setInt(5, 1);

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }

        if (getObservation() == 1) {
            query = "INSERT INTO `observations`(`id_carnet`, `id_consultation`, `date_mise_en_observation`, `date_sortie_en_observation`) VALUES (?,?,?,?)";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(3, getIdConsultation());
                statement.setTimestamp(3, Help.timestamp());

                if (getTypePatient() == 0) {
                    statement.setTimestamp(4, Help.timestamp(1));
                } else {
                    statement.setTimestamp(4, Help.timestamp(getObservation()));
                }

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }

        }



        query = "UPDATE carnet SET statut_constante = 0 WHERE id_carnet = ?";
        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
            statement.setString(1, getIdPatient());

            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException(sqle);
        }

        if(!getExamens().isEmpty()) {
            query = "INSERT INTO examens (id_consultation) VALUES (?) ";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, getIdConsultation());
                statement.executeUpdate();
                NotificationSuccesModel.getInstance().setMessage("Examen pour la consultation n°"+Help.lastInsert("consultations", "id_consultation")+"\nProgrammé");
                Help.enregistrementOk();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }

        NotificationSuccesModel.getInstance().setMessage("Consultation terminée");
        Help.enregistrementOk();


    }



    public void enregistrerAncienneConsultation() {

        String query = "";
        if (!getActeMedical().isEmpty()) {
            query = "INSERT INTO `acte_medical`(`id_carnet`, `acte_medical`,id_consultation) VALUES (?,?,?)";
            try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(2, getActeMedical());
                statement.setString(3, String.valueOf(ExamenModel.getInstance().getIdConsultation()));


                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }

        if (!getDiagnostic().isEmpty()) {
            query = "INSERT INTO `diagnostique`(`id_carnet`, `diagnostique`,id_consultation) VALUES (?,?,?)";
            try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(2, getDiagnostic());
                statement.setString(3, String.valueOf(ExamenModel.getInstance().getIdConsultation()));

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }

        }




        query = "UPDATE `consultations` SET `id_personnel` = ?, `date_consultation` = ?, `aller_en_observation` = ?, `examens` = ?, `statut_consultation` = ?, `id_diagnostique` = ?, `id_acte_medical` = ? WHERE id_carnet = ? AND date_consultation IS NULL ";
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);) {


            //ENREGISTREMENT CONSULTATION
            statement.setString(1, SessionUser.getInstance().getId());
            statement.setTimestamp(2, Help.timestamp());
            statement.setInt(3, getObservation());
            statement.setString(4, getExamens());
            statement.setInt(5, 1);
            statement.setString(6, Help.lastInsert("diagnostique", "id_diagnostique"));
            statement.setString(7, Help.lastInsert("acte_medical", "id_acte_medical"));
            statement.setString(8, getIdPatient());

            System.out.println(statement);
            statement.executeUpdate();

            //Recharger la liste des patients
            loadListDePatient();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        if (!getPrescription().isEmpty()) {
            query = "INSERT INTO `prescriptions`(`id_consultation`, `prescriptions_medicaments`, `prescriptions_soins`, `heure_prescriptiion`, `statut_prescription_soins`) VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, String.valueOf(ExamenModel.getInstance().getIdConsultation()));
                statement.setString(2, getPrescription());
                statement.setString(3, getPrescriptionSoins());
                statement.setTimestamp(4, Help.timestamp());
                statement.setInt(5, 1);

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }
        }

        if (getObservation() == 1) {
            query = "INSERT INTO `observations`(`id_carnet`, `id_consultation`, `date_mise_en_observation`, `date_sortie_en_observation`) VALUES (?,?,?,?)";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, getIdPatient());
                statement.setString(2, String.valueOf(ExamenModel.getInstance().getIdConsultation()));
                statement.setTimestamp(3, Help.timestamp());

                Timestamp timestamp ;
                if (getTypePatient() == 0) {
                    statement.setTimestamp(4, Help.timestamp(1));
                    timestamp = Help.timestamp(1);
                } else {
                    statement.setTimestamp(4, Help.timestamp(2));
                    timestamp = Help.timestamp(2);

                }

                statement.executeUpdate();

                NotificationSuccesModel.getInstance().setMessage("Patient Envoyé En Observation jusqu'au\n"+timestamp);
                Help.enregistrementOk();

            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }

            query = "UPDATE consultations SET statut_consultation = 1 WHERE id_consultation = ?";
            try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);) {
                statement.setString(1, PatientConsultation.getInstance().getIdConsultation());

                statement.executeUpdate();
            } catch (SQLException sqle) {
                throw new IllegalStateException(sqle);
            }

        }

        NotificationSuccesModel.getInstance().setMessage("Consultation Après Examen Terminée ");
        Help.enregistrementOk();

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

    public String getIdConsultation() {
        return idConsultation.get();
    }

    public StringProperty idConsultationProperty() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation.set(idConsultation);
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

    public String getPrescriptionSoins() {
        return this.prescriptionSoins.get();
    }

    public void setPrescriptionSoins(String prescription) {
        this.prescriptionSoins.set(prescription);
    }

    public StringProperty prescriptionSoinsProperty() {
        return this.prescriptionSoins;
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

    public void setObservation(int observation) {
        this.observation.set(observation);
    }

    public IntegerProperty observationProperty() {
        return this.observation;
    }

    public int getTypePatient() {
        return this.typePatient.get();
    }

    public void setTypePatient(int typePatient) {
        this.typePatient.set(typePatient);
    }

    public IntegerProperty typePatientProperty() {
        return this.typePatient;
    }

    public String getRendezVous() {
        return this.rendezVous.get();
    }

    public void setRendezVous(String rdv) {
        this.rendezVous.set(rdv);
    }

    public StringProperty rendezVousProperty() {
        return this.rendezVous;
    }

}
