package com.example.javaweb.alem.controller.admin;

import com.example.javaweb.alem.core.Help;
import com.example.javaweb.alem.model.admin.PersonnelModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FormulairePersonnel implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private DatePicker datenaissPicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> grade;

    @FXML
    private ComboBox<String> groupeSanguinBox;

    @FXML
    private TextField motDePasseTextField;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField nomPrenomsTextField;

    @FXML
    private TextField adresseTextField;

    @FXML
    private TextField numeroTextField;

    @FXML
    private Button setButton;

    @FXML
    private Button storeButton;

    @FXML
    private RadioButton sexeFeminRadio;

    @FXML
    private RadioButton sexeMasculinRadio;

    @FXML
    private ComboBox<String> specialiteCombo;

    @FXML
    private Text stateText;

    @FXML
    private ComboBox<String> typePersonnnelCombo;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //On verifie si personnel n'est pas vide pour ajouter les données dans les champs//

        datenaissPicker.setValue(LocalDate.now());
        typePersonnnelCombo.setItems(Help.comboListe("type_personnel","libelle_type_personnel"));
        specialiteCombo.setItems(Help.comboListe("specialite","libelle"));
        grade.setItems(Help.comboListe("grade_medical","lib_grade_medical"));
        groupeSanguinBox.setItems(Help.groupeSanguinListe());




        errorLabel.textProperty().bind(Help.message);

        Help.nan(numeroTextField);
        Help.dateMax(datenaissPicker);

        Help.fillField(PersonnelModel.getInstance().getNomPrenoms(),nomPrenomsTextField);
        Help.fillField(PersonnelModel.getInstance().getTypePersonnel(),typePersonnnelCombo);
        Help.fillField(PersonnelModel.getInstance().getSpecialite(),specialiteCombo);
        Help.fillField(PersonnelModel.getInstance().getAdresse(),adresseTextField);
        Help.fillField(PersonnelModel.getInstance().getNumeroTelephone(), numeroTextField);
        Help.fillField(PersonnelModel.getInstance().getMotDePasse(),motDePasseTextField);
        Help.fillField(PersonnelModel.getInstance().getGradeMedical(),grade);
        Help.fillField(PersonnelModel.getInstance().getEmail(),emailTextField);
        Help.fillField(PersonnelModel.getInstance().getGroupeSanguin(),groupeSanguinBox);
        Help.fillField(PersonnelModel.getInstance().getDateNaissance(),datenaissPicker);
        Help.fillField(PersonnelModel.getInstance().getDateNaissance(),sexeFeminRadio,sexeMasculinRadio);
        Help.fillField(PersonnelModel.getInstance().getLogin(),loginTextField);

        Button controlButton = new Button();
        if(storeButton != null){
            controlButton = storeButton;
        }else if(setButton != null){
            controlButton = setButton;
        }

        controlButton.setOnMouseClicked(mouseEvent -> {
            boolean a,b,c,d,e,f,g,h,i,j,k = false;
            a = Help.controle(nomPrenomsTextField.getText());
            b = Help.controle(typePersonnnelCombo.getValue());
            c = Help.controle(specialiteCombo.getValue());
            d = Help.controle(adresseTextField.getText());
            e = Help.controle(numeroTextField.getText());
            f = Help.controle(motDePasseTextField.getText());
            g = Help.controle(grade.getValue());
            h = Help.controle(emailTextField.getText());
            i = Help.controle(groupeSanguinBox.getValue());
            j = Help.controle(datenaissPicker.getValue().toString());
            k = Help.controle(loginTextField.getText());

            if(a && b && c && d && e && f && g && h && i && j && k){
                PersonnelModel.getInstance().setNomPrenoms(nomPrenomsTextField.getText());
                PersonnelModel.getInstance().setTypePersonnel(typePersonnnelCombo.getValue());
                PersonnelModel.getInstance().setSpecialite(specialiteCombo.getValue());
                PersonnelModel.getInstance().setAdresse(adresseTextField.getText());
                PersonnelModel.getInstance().setNumeroTelephone(numeroTextField.getText());
                PersonnelModel.getInstance().setMotDePasse(motDePasseTextField.getText());
                PersonnelModel.getInstance().setGradeMedical(grade.getValue());
                PersonnelModel.getInstance().setEmail(emailTextField.getText());
                PersonnelModel.getInstance().setGroupeSanguin(groupeSanguinBox.getValue());
                PersonnelModel.getInstance().setDateNaissance(datenaissPicker.getValue().toString());

                ToggleGroup toggleGroup = new ToggleGroup();

                sexeMasculinRadio.setToggleGroup(toggleGroup);
                sexeFeminRadio.setToggleGroup(toggleGroup);

                RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
                if (radioButton != null) {
                    PersonnelModel.getInstance().setSexe(radioButton.getText());
                }

                PersonnelModel.getInstance().setLogin(loginTextField.getText());

                if(storeButton != null){
                    PersonnelModel.getInstance().storePersonel();
                }else if(setButton != null){
                    PersonnelModel.getInstance().setPersonnel();
                }

            }

        });

    }
}
