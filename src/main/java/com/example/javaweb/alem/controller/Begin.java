package com.example.javaweb.alem.controller;

import com.example.javaweb.alem.Router;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class Begin implements Initializable {

    @FXML
    private ImageView graphe;

    @FXML
    private ImageView medecin;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Text titre;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ScaleTransition cercle3 = new ScaleTransition();
        cercle3.setNode(graphe);
        cercle3.setDuration(javafx.util.Duration.millis(1000));
        cercle3.setCycleCount(TranslateTransition.INDEFINITE);
        cercle3.setInterpolator(Interpolator.LINEAR);
        cercle3.setByX(0.05);
        cercle3.setByY(0.05);
        cercle3.play();

        //titre
        FadeTransition fade3 = new FadeTransition();
        fade3.setNode(titre);
        fade3.setDuration(javafx.util.Duration.millis(1000));
        fade3.setCycleCount(FadeTransition.INDEFINITE);
        fade3.setInterpolator(Interpolator.LINEAR);
        fade3.setFromValue(0.5);
        fade3.setToValue(0.3);
        fade3.play();


        RotateTransition rotate = new RotateTransition();
        rotate.setNode(medecin);
        rotate.setDuration(javafx.util.Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.EASE_IN);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.play();

        AtomicReference<Float> count = new AtomicReference<>((float) 0);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.003), e -> {
                    count.set((float) (count.get() + 0.0005));
                    progressbar.setProgress(count.get());
                })
        );
        timeline.setCycleCount(2000);
        timeline.play();

        timeline.setOnFinished(actionEvent -> {
            Router.toConnexion();
        });

    }
}
