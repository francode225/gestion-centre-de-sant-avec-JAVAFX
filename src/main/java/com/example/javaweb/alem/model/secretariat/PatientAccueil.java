package com.example.javaweb.alem.model.secretariat;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PatientAccueil {

    private final StringProperty idPatient = new SimpleStringProperty();
    private final StringProperty sexePatient = new SimpleStringProperty();

    private final StringProperty nomPatient = new SimpleStringProperty();
    private final StringProperty dernierRdv = new SimpleStringProperty();

    private static final ListProperty<PatientAccueil> listeDePatient = new SimpleListProperty<>(FXCollections.observableArrayList());

    private PatientAccueil(String id, String nom, String lastRdv,String sexe) {
        this.idPatient.set(id);
        this.nomPatient.set(nom);
        this.dernierRdv.set(lastRdv);
        this.sexePatient.set(sexe);

    }


    public static PatientAccueil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PatientAccueil INSTANCE = new PatientAccueil("", "", "","");
    }

    public ObservableList<PatientAccueil> getAllPatients(String nom) {
        loadListDePatient(nom);
        return listeDePatient.get();
    }

    private static void loadListDePatient(String nom) {
        String query = "SELECT * FROM carnet WHERE nom_prenom LIKE ?";
        ObservableList<PatientAccueil> listPatient = new SimpleListProperty<>(FXCollections.observableArrayList());

        try (PreparedStatement statement = LoginDB.getConnection().prepareStatement(query);){
            statement.setString(1, "%" + nom + "%");
            System.out.println(nom);
            ResultSet resultSet = statement.executeQuery();
            String sexe = "";
            while (resultSet.next()) {
                sexe = "M";
                if(resultSet.getString("id_sexe").equals("F")){
                    sexe = "F";

                }
                listPatient.add(new PatientAccueil(resultSet.getString("carnet.id_carnet"), resultSet.getString("nom_prenom"), resultSet.getString("carnet.date_modification_carnet"),sexe));
            }
            listeDePatient.bindContent(listPatient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette fonction enregistre une nouvelle prise de constante
     */
    public void enregistrerNouveauPatient(){

    }

    public void enregistrerNouvellesConstantes(){
        String query = "UPDATE carnet SET statut_constante = 0 WHERE id_carnet = ?";

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);){
            statement.setString(1, getIdPatient());
            statement.executeUpdate();

            //Recharger la liste des patients
            loadListDePatient("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO constantes_patient (id_carnet) VALUES (?)";

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);){
            statement.setString(1, getIdPatient());
            statement.executeUpdate();

            //Recharger la liste des patients
            loadListDePatient("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NotificationSuccesModel.getInstance().setMessage("Patient prêt pour constante");
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
}
