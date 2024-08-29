package com.example.javaweb.alem;

import com.example.javaweb.alem.controller.Constantes;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Router {

    public static Stage stageHold = new Stage();

    public static void changeView(String path, String titre) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Router.class.getResource(path));
            Scene scene = new Scene(root, 1315, 810);
            stageHold.setScene(scene);

            // Récupérer les dimensions de l'écran
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Calculer les coordonnées X et Y pour centrer la fenêtre
            double centerX = (screenBounds.getWidth() - stageHold.getWidth())/2;
            double centerY = (screenBounds.getHeight() - stageHold.getHeight())/2.5;

            // Positionner la fenêtre au centre de l'écran
            stageHold.setX(centerX);
            stageHold.setY(centerY);

            stageHold.setTitle(titre);
            stageHold.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*    public static void toHello(){
        Parent root = null;
        try {
            root = FXMLLoader.load(Router.class.getResource("/com/example/javaweb/hello-view.fxml"));
            Scene scene = new Scene(root, 1315, 810);
            stageHold.setTitle("titre");
            stageHold.setScene(scene);
            stageHold.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        changeView("hello-view.fxml","Hello Vue");
    }*/


    public static void toConnexion() {
        changeView("/com/example/javaweb/form_accueil.fxml", "Connexion Vue");
    }

    public static void toAccueil() {
        changeView("/com/example/javaweb/yeoviews/accueil.fxml", "com.example.javaweb.alem.controller.accueil.Accueil Vue");
    }

    public static void toBegin() {
        changeView("/com/example/javaweb/animation.fxml", "com.example.javaweb.alem.controller.accueil.Accueil Vue");
    }

    public static void toCarnet() {
        changeView("/com/example/javaweb/yeoviews/form_carnet.fxml", "Formulaire Carnet");
    }

    public static void toConstantes(String idCarnet) {
        changeView("/com/example/javaweb/yeoviews/form_infirmier.fxml", "Formulaire Carnet");
    }

    public static void toInfirmerie() {
        changeView("/com/example/javaweb/infirmerie.fxml", "Formulaire Carnet");
    }

    public static void toConsultation() {
        changeView("/com/example/javaweb/yeoviews/form_consultation.fxml", "Formulaire Carnet");
    }

    public static void toMedecine() {
        changeView("/com/example/javaweb/medecine.fxml", "Formulaire Med");
    }

    public static void toAnalyse() {
        changeView("/com/example/javaweb/yeoviews/form_laboratoire.fxml", "Formulaire Analyse");
    }

    public static void toLabo() {
        changeView("/com/example/javaweb/labo.fxml", "Formulaire Analyse");
    }

    public static void toAdmin() {
        changeView("/com/example/javaweb/dashboard/administrateur.fxml", "Dashboard Administrateur");
    }





}
