package com.example.javaweb.alem.core;

import com.example.javaweb.alem.model.admin.PersonnelModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;

public class MedecinCrudButton extends TableCell<PersonnelModel, Void> {
    private final Button button = new Button();

    public static enum Action {
        SET,
        DEL;
    }

    public MedecinCrudButton(Enum action) {
        this.button.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
        this.button.setPrefWidth(150);

        if (action.equals(Action.SET)) {
            this.button.setText("Modifier");
        } else {
            this.button.setText("Supprimer");
        }

        this.button.setOnMouseClicked(mouseEvent -> {
            PersonnelModel personnelModel = getTableView().getItems().get(getIndex());
            PersonnelModel.getInstance().copy(personnelModel);
            PersonnelModel.getInstance().loadPersonnel();
            System.out.println(PersonnelModel.getInstance().getTypePersonnel());
                try {
                    Help.doorPen("/com/example/javaweb/yeoviews/form_personnel_mod.fxml", "Formulaire");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });

    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}
