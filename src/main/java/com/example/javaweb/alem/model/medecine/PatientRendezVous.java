package com.example.javaweb.alem.model.medecine;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.core.SessionUser;
import com.example.javaweb.alem.model.LoginDB;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PatientRendezVous {

    private final StringProperty idCarnet = new SimpleStringProperty();

    private final StringProperty id_rdv = new SimpleStringProperty();

    private final StringProperty dateRdv = new SimpleStringProperty();

    private final StringProperty idConsultation = new SimpleStringProperty();


    public static final ListProperty<PatientRendezVous> listeDeRdv = new SimpleListProperty<>(FXCollections.observableArrayList());

    private PatientRendezVous(String id, String idCar, String date, String cons) {
        this.id_rdv.set(id);
        this.idCarnet.set(idCar);
        this.dateRdv.set(date);
        this.idConsultation.set(cons);

    }


    public static PatientRendezVous getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PatientRendezVous INSTANCE = new PatientRendezVous("", "", "","");
    }


    public ObservableList<PatientRendezVous> getAllRdv() {
        loadListDeRdv();
        return listeDeRdv.get();
    }

    private static void loadListDeRdv() {
        String query = "SELECT * FROM rendezvous WHERE 'dateRdv' >= '"+Help.timestamp().toString().substring(0,10)+"'";
        ObservableList<PatientRendezVous> listRd = new SimpleListProperty<>(FXCollections.observableArrayList());
        System.out.println(query);

        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query);){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listRd.add(new PatientRendezVous(resultSet.getString("id_rdv"),resultSet.getString("id_carnet"), resultSet.getString("date_rdv"), resultSet.getString("id_consultation")));
            }
            listeDeRdv.bindContent(listRd);
            statement.close();
            System.out.println(listRd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Getters and Setters pour pouvoirs afficher les données de la bdd et récupérer des cellules
    public String getIdCarnet() {
        return idCarnet.get();
    }

    public StringProperty idCarnetProperty() {
        return idCarnet;
    }

    public void setIdCarnet(String idCarnet) {
        this.idCarnet.set(idCarnet);
    }

    public String getId_rdv() {
        return id_rdv.get();
    }

    public StringProperty id_rdvProperty() {
        return id_rdv;
    }

    public void setId_rdv(String id_rdv) {
        this.id_rdv.set(id_rdv);
    }

    public String getIdConsultation(){
        return this.idConsultation.get();
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation.set(idConsultation);
    }

    public StringProperty idConsultationProperty() {return this.idConsultation;}

    public String getDateRdv(){
        return this.dateRdv.get();
    }

    public void setDisagnotic(String diagnostique) {
        this.dateRdv.set(diagnostique);
    }

    public StringProperty dateRdvProperty() {return this.dateRdv;}


}
