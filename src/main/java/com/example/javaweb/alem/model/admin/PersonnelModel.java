package com.example.javaweb.alem.model.admin;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.LoginDB;
import com.example.javaweb.alem.model.NotificationSuccesModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PersonnelModel {
    private final IntegerProperty idPersonnel = new SimpleIntegerProperty();
    private final StringProperty nomPrenoms = new SimpleStringProperty();
    private final StringProperty typePersonnel = new SimpleStringProperty();
    private final StringProperty specialite = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty numeroTelephone = new SimpleStringProperty();
    private final StringProperty sexe = new SimpleStringProperty();
    private final StringProperty motDePasse = new SimpleStringProperty();
    private final StringProperty gradeMedical = new SimpleStringProperty();
    private final StringProperty dateCreationPersonnel = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty login = new SimpleStringProperty();
    private final StringProperty groupeSanguin = new SimpleStringProperty();
    private final StringProperty dateNaissance = new SimpleStringProperty();


    private static final ObservableList<PersonnelModel> listeMedecin = new SimpleListProperty<>(FXCollections.observableArrayList());

    private static final ObservableList<PersonnelModel> listeInfirmier = new SimpleListProperty<>(FXCollections.observableArrayList());

    private static final ObservableList<PersonnelModel> listeSecretaire = new SimpleListProperty<>(FXCollections.observableArrayList());

    private static final ObservableList<PersonnelModel> listeTechnicien = new SimpleListProperty<>(FXCollections.observableArrayList());


    public static PersonnelModel getInstance() {
        return HOLDER.INSTANCE;
    }

    private static class HOLDER {
        private static final PersonnelModel INSTANCE = new PersonnelModel();
    }

    PersonnelModel() {

    }


    // Ajoutez d'autres propriétés si nécessaire

    public PersonnelModel(String nom, String num, String date, int id) {
        this.nomPrenoms.set(nom);
        this.numeroTelephone.set(num);
        this.dateCreationPersonnel.set(date);
        this.idPersonnel.set(id);
    }

    public void copy(PersonnelModel p) {
        setIdPersonnel(p.getIdPersonnel());
        setAdresse(p.getAdresse());
        setEmail(p.getEmail());
        setLogin(p.getLogin());
        setTypePersonnel(p.getTypePersonnel());
        setDateCreationPersonnel(p.getDateCreationPersonnel());
        setGradeMedical(p.getGradeMedical());
        setMotDePasse(p.getMotDePasse());
        setNomPrenoms(p.getNomPrenoms());
        setNumeroTelephone(p.getNumeroTelephone());
        setSexe(p.getSexe());
        setSpecialite(p.getSpecialite());
        setGroupeSanguin(p.getGroupeSanguin());
        setDateNaissance(p.getDateNaissance());
    }

    /**
     * @return la liste observale des medecins
     */
    public static ObservableList<PersonnelModel> getListeMedecin() {
        loadListePersonnel();
        return listeMedecin;
    }

    public void loadPersonnel() {
        //requete
        String query = "SELECT * FROM personnel where id_personnel = ?";

        //liste de copie
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            //execution de la requete
            statement.setInt(1,getIdPersonnel());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                setIdPersonnel(resultSet.getInt("id_personnel"));
                setNomPrenoms(resultSet.getString("nom_prenoms"));
                setTypePersonnel(Help.findId("libelle_type_personnel","type_personnel","id_type_personnel",resultSet.getString("type_personnel")));
                setSpecialite(Help.findId("libelle","specialite","id_spe",resultSet.getString("specialite")));
                setAdresse(resultSet.getString("adresse"));
                setNumeroTelephone(resultSet.getString("numero_telephone"));
                setSexe(resultSet.getString("sexe"));
                setMotDePasse(resultSet.getString("mot_de_passe"));
                setGradeMedical(Help.findId("lib_grade_medical","grade_medical","id_grade_medical",resultSet.getString("id_grade_medical")));
                setDateCreationPersonnel(resultSet.getString("date_creation_personnel"));
                setEmail(resultSet.getString("email"));
                setLogin(resultSet.getString("login"));
                setDateNaissance(resultSet.getString("date_naiss"));
                setGroupeSanguin(resultSet.getString("groupe_sanguin"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void loadListePersonnel() {
        //requete
        String query = "SELECT * FROM personnel";

        //liste de copie
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            //execution de la requete
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                if (resultSet.getString("type_personnel").equals("SEC")) {
                    listeSecretaire.add(new PersonnelModel(resultSet.getString("nom_prenoms"), resultSet.getString("numero_telephone"), resultSet.getString("date_creation_personnel"), resultSet.getInt("id_personnel")));
                } else if (resultSet.getString("type_personnel").equals("MED")) {
                    listeMedecin.add(new PersonnelModel(resultSet.getString("nom_prenoms"), resultSet.getString("numero_telephone"), resultSet.getString("date_creation_personnel"), resultSet.getInt("id_personnel")));
                } else if (resultSet.getString("type_personnel").equals("TEC")) {
                    listeTechnicien.add(new PersonnelModel(resultSet.getString("nom_prenoms"), resultSet.getString("numero_telephone"), resultSet.getString("date_creation_personnel"), resultSet.getInt("id_personnel")));
                } else if (resultSet.getString("type_personnel").equals("INF")) {
                    listeInfirmier.add(new PersonnelModel(resultSet.getString("nom_prenoms"), resultSet.getString("numero_telephone"), resultSet.getString("date_creation_personnel"), resultSet.getInt("id_personnel")));
                }
            }

            LoginDB.getConnection().close();
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new IllegalStateException(e);
        }
    }

    public void storePersonel() {
        //requete
        String query = "INSERT INTO `personnel`(`nom_prenoms`, `type_personnel`, `specialite`, `adresse`, `numero_telephone`, `sexe`, `mot_de_passe`, `id_grade_medical`, `date_creation_personnel`, `email`, `login`, `groupe_sanguin`, `date_naiss`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //liste de copie
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            //execution de la requete
            statement.setString(1, getNomPrenoms());
            statement.setString(2, Help.findId("id_type_personnel","type_personnel","libelle_type_personnel",getTypePersonnel()));
            statement.setString(3, Help.findId("id_spe","specialite","libelle",getSpecialite()));
            statement.setString(4, getAdresse());
            statement.setString(5, getNumeroTelephone());
            statement.setString(6, getSexe());
            statement.setString(7, getMotDePasse());
            statement.setString(8, Help.findId("id_grade_medical","grade_medical","lib_grade_medical",getGradeMedical()));
            statement.setTimestamp(9, Help.timestamp());
            statement.setString(10, getEmail());
            statement.setString(11, getLogin());
            statement.setString(12, getGroupeSanguin());
            statement.setString(13, getDateNaissance());
            System.out.println(statement);

            statement.executeUpdate();

            NotificationSuccesModel.getInstance().setMessage("Enregistrement de " + getNomPrenoms() + " réussi");
            Help.enregistrementOk();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }


    }

    public void setPersonnel() {
        //requete
        String query = "UPDATE `personnel` SET `nom_prenoms` = ?, `type_personnel` = ?, `specialite` = ?, `adresse` = ?, `numero_telephone` = ?, `sexe` = ?, `mot_de_passe` = ?, `id_grade_medical` = ?, `date_creation_personnel` = ?, `email` = ?, `login` = ?, `groupe_sanguin` = ?, `date_naiss` = ? WHERE idPersonnel = ?";

        //liste de copie
        try (PreparedStatement statement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement(query)) {
            //execution de la requete
            statement.setString(1, getNomPrenoms());
            statement.setString(2, Help.findId("id_type_personnel","type_personnel","libelle_type_personnel",getTypePersonnel()));
            statement.setString(3, Help.findId("id_spe","specialite","libelle",getSpecialite()));
            statement.setString(4, getAdresse());
            statement.setString(5, getNumeroTelephone());
            statement.setString(6, getSexe());
            statement.setString(7, getMotDePasse());
            statement.setString(8, Help.findId("id_grade_medical","grade_medical","lib_grade_medical",getGradeMedical()));
            statement.setTimestamp(9, Help.timestamp());
            statement.setString(10, getEmail());
            statement.setString(11, getLogin());
            statement.setString(12, getGroupeSanguin());
            statement.setString(13, getDateNaissance());
            statement.setInt(14, getIdPersonnel());

            System.out.println(statement);

            statement.executeUpdate();

            NotificationSuccesModel.getInstance().setMessage("Modification de " + getNomPrenoms() + " réussi");
            Help.enregistrementOk();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }


    // Getter et setter pour idPersonnel
    public int getIdPersonnel() {
        return idPersonnel.get();
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel.set(idPersonnel);
    }

    public IntegerProperty idPersonnelProperty() {
        return idPersonnel;
    }

    // Getter et setter pour nomPrenoms
    public String getNomPrenoms() {
        return nomPrenoms.get();
    }

    public void setNomPrenoms(String nomPrenoms) {
        this.nomPrenoms.set(nomPrenoms);
    }

    public StringProperty nomPrenomsProperty() {
        return nomPrenoms;
    }

    // Getter et setter pour typePersonnel
    public String getTypePersonnel() {
        return typePersonnel.get();
    }

    public void setTypePersonnel(String typePersonnel) {
        this.typePersonnel.set(typePersonnel);
    }

    public StringProperty typePersonnelProperty() {
        return typePersonnel;
    }

    // Getter et setter pour specialite
    public String getSpecialite() {
        return specialite.get();
    }

    public void setSpecialite(String specialite) {
        this.specialite.set(specialite);
    }

    public StringProperty specialiteProperty() {
        return specialite;
    }

    // Getter et setter pour adresse
    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    // Getter et setter pour numeroTelephone
    public String getNumeroTelephone() {
        return numeroTelephone.get();
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone.set(numeroTelephone);
    }

    public StringProperty numeroTelephoneProperty() {
        return numeroTelephone;
    }

    // Getter et setter pour sexe
    public String getSexe() {
        return sexe.get();
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    public StringProperty sexeProperty() {
        return sexe;
    }

    // Getter et setter pour motDePasse
    public String getMotDePasse() {
        return motDePasse.get();
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }

    public StringProperty motDePasseProperty() {
        return motDePasse;
    }

    // Getter et setter pour gradeMedical
    public String getGradeMedical() {
        return gradeMedical.get();
    }

    public void setGradeMedical(String gradeMedical) {
        this.gradeMedical.set(gradeMedical);
    }

    public StringProperty gradeMedicalProperty() {
        return gradeMedical;
    }

    // Getter et setter pour dateCreationMedecin
    public String getDateCreationPersonnel() {
        return dateCreationPersonnel.get();
    }

    public void setDateCreationPersonnel(String dateCreationPersonnel) {
        this.dateCreationPersonnel.set(dateCreationPersonnel);
    }

    public StringProperty dateCreationPersonnelProperty() {
        return dateCreationPersonnel;
    }

    // Getter et setter pour email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getter et setter pour login
    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public StringProperty loginProperty() {
        return login;
    }

    // Getters et setters pour groupeSanguin
    public String getGroupeSanguin() {
        return groupeSanguin.get();
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin.set(groupeSanguin);
    }

    public StringProperty groupeSanguinProperty() {
        return groupeSanguin;
    }

    // Getters et setters pour dateNaissance
    public String getDateNaissance() {
        return dateNaissance.get();
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance.set(dateNaissance);
    }

    public StringProperty dateNaissanceProperty() {
        return dateNaissance;
    }
}
