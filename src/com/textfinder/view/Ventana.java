package com.textfinder.view;

import com.textfinder.documentlibrary.DocumentLibrary;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Ventana extends Application {

    public static void main(String[] args) throws IOException {



        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button btn = new Button();
        btn.setLayoutX(100);
        btn.setLayoutY(100);
        btn.setText("¡Hola mundo!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("¡Hola mundo!");
            }
        });

        Pane root = new Pane();
        root.getChildren().add(btn);

        stage.setTitle("¡Hola mundo!");
        stage.setScene(new Scene(root, 1360, 760));

        DocumentLibrary.addFile(stage);

        stage.show();
    }
}
