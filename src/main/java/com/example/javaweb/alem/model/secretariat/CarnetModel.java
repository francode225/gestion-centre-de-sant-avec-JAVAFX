package com.example.javaweb.alem.model.secretariat;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarnetModel {


    private final IntegerProperty idCarnet = new SimpleIntegerProperty();

    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();

    private final StringProperty email = new SimpleStringProperty();

    private final StringProperty adresse = new SimpleStringProperty();

    private final StringProperty numero = new SimpleStringProperty();

    private final StringProperty lieuNaissance = new SimpleStringProperty();

    private final StringProperty groupeSanguin = new SimpleStringProperty();

    private final StringProperty dateNaissance = new SimpleStringProperty();
    private final StringProperty sexe = new SimpleStringProperty();

    private final StringProperty typePatient = new SimpleStringProperty();

    private final StringProperty profession = new SimpleStringProperty();
    private final StringProperty antecedents = new SimpleStringProperty();

    private final StringProperty message = new SimpleStringProperty();

    private static final ListProperty<CarnetModel> listeDeCarnet = new SimpleListProperty<>(FXCollections.observableArrayList());


    public CarnetModel(){

    }


    //FONCTION D'INSERTION DE CARNET
    public void storeCarnet() throws SQLException {


        //récupération des variables a utiliser
        int idAntecedents = 1;
        //Récupeération de la nouvelle ligne d'antécédents ajouté
        PreparedStatement indexStatement = null;
        try {
            indexStatement = LoginDB.getConnection().prepareStatement("select * from antecedents ORDER BY id_antecedents desc LIMIT 1");
            ResultSet resultSet = indexStatement.executeQuery();
            if(resultSet.next()) {
                idAntecedents = Integer.parseInt(resultSet.getString("id_antecedents"));
                idAntecedents++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            indexStatement.close();
        }


        ///ENREGISTREMENT PRELIMINAIRES

        System.out.println("idAntecedents: " + idAntecedents);

        String sql = "INSERT INTO carnet (date_creation_carnet,date_modification_carnet,localisation,id_sexe,profession,telephone,email,id_groupe_sanguin, date_naissance, lieu_naissance,id_type_patient,nom_prenom,statut_constante) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = LoginDB.getConnection().prepareStatement(sql)) {


            preparedStatement.setTimestamp(1, Help.timestamp());
            preparedStatement.setTimestamp(2, Help.timestamp());
            preparedStatement.setString(3, getAdresse());
            preparedStatement.setString(4, getSexe());
            preparedStatement.setString(5, getProfession());
            preparedStatement.setString(6, getNumero());
            preparedStatement.setString(7, getEmail());
            preparedStatement.setString(8, getGroupeSanguin());
            preparedStatement.setString(9, Help.dateFormater(getDateNaissance()));
            preparedStatement.setString(10, getLieuNaissance());
            preparedStatement.setString(11, getTypePatient());
            preparedStatement.setString(12, getNom() +" "+ getPrenom());
            preparedStatement.setInt(13, 0);

            preparedStatement.executeUpdate();


            //ENREGISTRER LES ANTECEDENTS

            //Récupeération de l'id du nouveaau carnet créé pour creer ses antécédents

            String id = Help.lastInsert("carnet","id_carnet");
            if(id.isEmpty()) {
                id = "1";
            }


            indexStatement = LoginDB.getConnection().prepareStatement("insert into antecedents (id_carnet,antecedents) values (?,?)");

            //Enregistrement de la nouvelle ligne d'antécédents pour ke nouveau patient

            indexStatement.setInt(1, Integer.parseInt(id));
            indexStatement.setString(2, getAntecedents());
            System.out.println(indexStatement);

            indexStatement.executeUpdate();

            //
            String query = "INSERT INTO constantes_patient (id_carnet) VALUES (?)";

            indexStatement = LoginDB.getConnection().prepareStatement(query);
            indexStatement.setInt(1, Integer.parseInt(id));
            System.out.println(indexStatement);

            indexStatement.executeUpdate();

                //Recharger la liste des patients
            NotificationSuccesModel.getInstance().setMessage("Patient Enregistré et \nprêt pour constante");
            Help.enregistrementOk();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw new IllegalStateException(e);
        }finally {
            indexStatement.close();
        }

    }


    public void setIdCarnet(int id) {
        this.idCarnet.set(id);
    }

    public int getIdCarnet() {
        return idCarnet.get();
    }

    public IntegerProperty idCarnetProperty() {
        return idCarnet;
    }


    // Setter et getter pour nom
    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    // Setter et getter pour prenom
    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    // Setter et getter pour email
    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Setter et getter pour adresse
    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    // Setter et getter pour numero
    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public String getNumero() {
        return numero.get();
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    // Setter et getter pour lieuNaissance
    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance.set(lieuNaissance);
    }

    public String getLieuNaissance() {
        return lieuNaissance.get();
    }

    public StringProperty lieuNaissanceProperty() {
        return lieuNaissance;
    }

    // Setter et getter pour groupeSanguin
    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin.set(groupeSanguin);
    }

    public String getGroupeSanguin() {
        return groupeSanguin.get();
    }

    public StringProperty groupeSanguinProperty() {
        return groupeSanguin;
    }

    // Setter et getter pour dateNaissance
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance.set(dateNaissance);
    }

    public String getDateNaissance() {
        return dateNaissance.get();
    }

    public StringProperty dateNaissanceProperty() {
        return dateNaissance;
    }

    // Setter et getter pour sexe
    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    public String getSexe() {
        return sexe.get();
    }

    public StringProperty sexeProperty() {
        return sexe;
    }

    // Setter et getter pour typePatient
    public void setTypePatient(String typePatient) {
        this.typePatient.set(typePatient);
    }

    public String getTypePatient() {
        return typePatient.get();
    }

    public StringProperty typePatientProperty() {
        return typePatient;
    }

    // Setter et getter pour profession
    public void setProfession(String profession) {
        this.profession.set(profession);
    }

    public String getProfession() {
        return profession.get();
    }

    public StringProperty professionProperty() {
        return profession;
    }

    // Setter et getter pour Antecedents
    public void setAntecedents(String antecedents) {
        this.antecedents.set(antecedents);
    }

    public String getAntecedents() {
        return antecedents.get();
    }

    public StringProperty antecedentsProperty() {
        return antecedents;
    }

    // Setter et getter pour message
    public void setMessage(String message) {
        this.antecedents.set(message);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }


}
