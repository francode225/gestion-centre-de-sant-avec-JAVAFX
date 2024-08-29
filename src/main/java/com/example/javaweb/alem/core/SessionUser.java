package com.example.javaweb.alem.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SessionUser {

    private final StringProperty id = new SimpleStringProperty();

    private final StringProperty login = new SimpleStringProperty();

    private final StringProperty nomPrenom = new SimpleStringProperty();

    private final StringProperty sexe = new SimpleStringProperty();

    public static SessionUser getInstance() {
        return SessionUser.Holder.INSTANCE;
    }

    private static class Holder {
        private static final SessionUser INSTANCE = new SessionUser();
    }

    public String getId() {
        return id.get();
    }

    public  void setId(String id) {
        this.id.set(id);
    }

    public String getLogin() {
        return login.get();
    }

    public  void setLogin(String login) {
        this.login.set(login);
    }

    public String getNomPrenom() {
        return getSexe().equals("M") ? "Mr "+this.nomPrenom.get() : "Mme "+this.nomPrenom.get();
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom.set(nomPrenom);
    }

    public String getSexe() {
        return this.sexe.get();
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }



}
