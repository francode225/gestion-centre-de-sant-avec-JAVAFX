package com.example.javaweb.alem.core;

import com.example.javaweb.HelloApplication;
import com.example.javaweb.alem.model.LoginDB;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Help {

    private static final LoginDB loginDB = LoginDB.getInstance();
    private static Scene scene = null;
    private static Stage stage = new Stage();

    public static StringProperty message = new SimpleStringProperty();


    //Groupe Sanguin

    public static ObservableList<String> groupeSanguinListe() {
        ListProperty<String> list = new SimpleListProperty<>(FXCollections.observableArrayList());
        String query = "SELECT * FROM groupe_sanguin";

        try (PreparedStatement statement = loginDB.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("id_groupe_sanguin"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static ObservableList<String> comboListe(String table, String data) {
        ListProperty<String> list = new SimpleListProperty<>(FXCollections.observableArrayList());
        String query = "SELECT * FROM "+table;

        try (PreparedStatement statement = loginDB.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString(data));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }



    /**
     * @param dateToConvert
     * @return La date sous une forme adapté à sql
     */
    public static String dateFormater(String dateToConvert) {
        LocalDate date = LocalDate.parse(dateToConvert);

        // Formatter la date dans un autre format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }

    public static Timestamp timestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp timestamp(int day) {
        return new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(day));
    }

    public static String lastInsert(String table, String column) {
        String idColumn = "";
        //Récupeération de la nouvelle ligne d'antécédents ajouté
        try (PreparedStatement preparedStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement("select * from " + table + "  ORDER BY " + column + " desc LIMIT 1");) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idColumn = resultSet.getString(column);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return idColumn;
    }

    public static String checkNull(String s) {
        return s == null ? "null" : s;
    }

    public static void doorPen(String path, String legende) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path));
        Scene scene = new Scene(fxmlLoader.load(), 1315, 810);
        Stage stage = new Stage();
        stage.setTitle(legende);
        stage.setScene(scene);
        stage.show();
    }

    public static String getElementsOfTable(String table, String columnVoulu, String column, String value) {

        try (PreparedStatement preparedStatement = Objects.requireNonNull(LoginDB.getConnection()).prepareStatement("select " + columnVoulu + " from " + table + " WHERE " + column + " = " + value + " ORDER BY " + column + " desc LIMIT 1");) {
            System.out.println(preparedStatement
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(columnVoulu);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return "";

    }

    public static void enregistrementOk() {
        Parent root = null;
        try {
            root = FXMLLoader.load(Help.class.getResource("/com/example/javaweb/notifs/EnregistrementOk.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        stage.close();
    }

    public static boolean controle(String value) {
        if (value == null || value.isEmpty()) {
            message.set("Erreur l'un des champs est vide");
            return false;
        }
        return true;
    }

    public static void consume(TextField value, int len) {
        value.setOnKeyPressed(keyEvent -> {
            if (value.getText().length() > len) {
                value.setText(value.getText().substring(0, value.getText().length() - 1));
                value.positionCaret(value.getText().length());
            }
        });


    }

    public static void nan(TextField value) {
        value.setOnKeyPressed(keyEvent -> {
            value.setText(value.getText().replaceAll("[^0-9]", ""));
            value.positionCaret(value.getText().length());
        });
    }

    public static void dateMax(DatePicker datenaissPicker) {
        datenaissPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(LocalDate.now())) {
                    setDisable(true); // Désactiver les dates après la limite maximale
                }
            }
        });

        // Gestionnaire d'événements pour surveiller la sélection de date
        datenaissPicker.setOnAction(event -> {
            LocalDate selectedDate = datenaissPicker.getValue();
            if (selectedDate != null && selectedDate.isAfter(LocalDate.now())) {
                datenaissPicker.setValue(LocalDate.now()); // Réinitialiser la date sélectionnée à la limite maximale
            }
        });
    }

    public static void fillField(String text, TextField field) {
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            field.setText(text);
        }
    }

    public static void fillField(String text, ComboBox<String> field) {
        if (text != null && !text.isEmpty() && !text.isBlank()) field.setValue(text);
    }

    public static void fillField(String text, RadioButton... buttons) {
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            ToggleGroup toggle = new ToggleGroup();
            for (RadioButton button : buttons) {
                button.setToggleGroup(toggle);
                if (text.equals(button.getText())) {
                    button.setSelected(true);
                }
            }
        }

    }

    public static void fillField(String text, DatePicker datePicker) {

        if (text != null && !text.isEmpty() && !text.isBlank()) {
            LocalDate date = LocalDate.parse(text);
            datePicker.setValue(date);
        }
    }

    public static String findId(String id, String table,String column, String lib){
        String query = "SELECT "+id+" FROM "+ table +" WHERE "+column+" = '"+lib+"' ORDER BY "+id+" DESC LIMIT 1";
        System.out.println(query);
        try (PreparedStatement statement = loginDB.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
