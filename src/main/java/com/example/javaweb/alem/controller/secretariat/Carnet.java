package com.example.javaweb.alem.controller.secretariat;

import com.example.javaweb.alem.Router;
import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.secretariat.CarnetModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Carnet implements Initializable {

    @FXML
    private TextArea antecedentsTextArea;

    @FXML
    private Button buttonFinAntecedents;

    @FXML
    private Button buttonMakeAntecedents;

    @FXML
    private DatePicker datenaissPicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> groupeSanguinBox;

    @FXML
    private TextField lieunaissTextField;

    @FXML
    private TextField loacalisationTextField;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField professionTextField;

    @FXML
    private RadioButton sexeFeminRadio;

    @FXML
    private RadioButton sexeMasculinRadio;

    @FXML
    private Button storeButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private Text stateText;

    @FXML
    private CheckBox typePatientCheck;

    @FXML
    private Label errorLabel;

    private static final CarnetModel carnetModel = new CarnetModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        stateText.textProperty().bind(Bindings.concat(carnetModel.messageProperty()));

        groupeSanguinBox.setItems(Help.groupeSanguinListe());

        ToggleGroup toggleGroup = new ToggleGroup();

        sexeMasculinRadio.setToggleGroup(toggleGroup);
        sexeFeminRadio.setToggleGroup(toggleGroup);


        errorLabel.textProperty().bind(Help.message);

        Help.nan(telephoneTextField);
        Help.dateMax(datenaissPicker);




        storeButton.setOnMouseClicked(mouseEvent -> {
            boolean a;
            boolean b;
            boolean c;
            boolean d;
            boolean f;
            boolean g;
            boolean h;
            boolean i;
            a = Help.controle(nomTextField.getText());
            b = Help.controle(prenomTextField.getText());
            c = Help.controle(emailTextField.getText());
            d = Help.controle(telephoneTextField.getText());
            f = Help.controle(loacalisationTextField.getText());
            g = Help.controle(groupeSanguinBox.getSelectionModel().getSelectedItem());
            h = Help.controle(lieunaissTextField.getText());
            i = Help.controle(professionTextField.getText());



            if(a && b && c && d && f && g && h && i) {
                Help.message.set("");
                carnetModel.setNom(nomTextField.getText());
                carnetModel.setPrenom(prenomTextField.getText());
                carnetModel.setEmail(emailTextField.getText());
                carnetModel.setNumero(telephoneTextField.getText());
                carnetModel.setDateNaissance(datenaissPicker.getValue().toString());
                carnetModel.setAdresse(loacalisationTextField.getText());
                carnetModel.setGroupeSanguin(groupeSanguinBox.getSelectionModel().getSelectedItem());
                carnetModel.setLieuNaissance(lieunaissTextField.getText());
                carnetModel.setProfession(professionTextField.getText());
                carnetModel.setAntecedents(antecedentsTextArea.getText());

                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                if (selectedRadioButton != null) {
                    carnetModel.setSexe(selectedRadioButton.getText());
                    if (selectedRadioButton.getText().equals("F") && typePatientCheck.isSelected()) {
                        carnetModel.setTypePatient("1");
                    } else {
                        carnetModel.setTypePatient("0");
                    }
                }

                assert selectedRadioButton != null;
                if (selectedRadioButton.getText().equals("F") && typePatientCheck.isSelected()) {
                    carnetModel.setTypePatient("1");
                }else{
                    carnetModel.setTypePatient("0");
                }

                carnetModel.setSexe(selectedRadioButton.getText());
                try {
                    carnetModel.storeCarnet();
                } catch (SQLException err) {
                    throw new IllegalStateException(err);
                }
            }

        });

        buttonMakeAntecedents.setOnMouseClicked(mouseEvent -> {
            antecedentsTextArea.setVisible(true);
            buttonFinAntecedents.setVisible(true);
        });

        buttonFinAntecedents.setOnMouseClicked(mouseEvent -> {
            antecedentsTextArea.setVisible(false);
            buttonFinAntecedents.setVisible(false);
        });

        backButton.setOnMouseClicked(mouseEvent -> Router.toAccueil());

    }


}
