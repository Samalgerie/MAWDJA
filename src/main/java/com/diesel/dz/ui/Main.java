package com.diesel.dz.ui;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    ResourceBundle bundle;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Parent root = null;
        try {

//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/diesel/dz/ui/main/main.fxml")/*, bundle*/);
//            root = loader.load();
     /*       MainController controller = loader.getController();
            controller.setHostServices(getHostServices());

*/
//          root = FXMLLoader.load(getClass().getResource("/m/and/a/ui/addOccupation/addOccupation.fxml"));


         root = FXMLLoader.load(getClass().getResource("/com/diesel/dz/ui/main/main.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(670);

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });


    }




}


