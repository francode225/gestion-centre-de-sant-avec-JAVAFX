package com.example.javaweb.alem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NotificationSuccesModel {

    public static NotificationSuccesModel getInstance() {
        return NotificationSuccesModel.Holder.INSTANCE;
    }

    private static class Holder {
        private static final NotificationSuccesModel INSTANCE = new NotificationSuccesModel("");
    }


    private StringProperty message;


    public NotificationSuccesModel(String defaultMessage) {
        this.message = new SimpleStringProperty(defaultMessage);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }
}
