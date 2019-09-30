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


public class Ventana extends Application {

    private Stage stage;

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage stage){

        this.stage = stage;

        Button btn = new Button();
        btn.setLayoutX(100);
        btn.setLayoutY(100);
        btn.setText("Agregar archvivos");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFile(stage);
            }
        });

        Pane root = new Pane();
        root.getChildren().add(btn);

        stage.setTitle("Text Finder");
        stage.setScene(new Scene(root, 1360, 760));

        stage.show();
    }


}
