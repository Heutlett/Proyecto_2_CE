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
        stage.setTitle("Text Finder");

        //Se crea el panel y se agrega al stage
        Pane panel = new Pane();
        stage.setScene(new Scene(panel, 1360, 760));

        //Boton de agregar archivos
        Button btnAddFile = new Button();
        btnAddFile.setLayoutX(100);
        btnAddFile.setLayoutY(100);
        btnAddFile.setText("Agregar archvivos");
        btnAddFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFile(stage);
            }
        });
        panel.getChildren().add(btnAddFile);

        //Boton de agregar folders
        Button btnAddFolder = new Button();
        btnAddFolder.setLayoutX(100);
        btnAddFolder.setLayoutY(200);
        btnAddFolder.setText("Agregar carpeta");
        btnAddFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFolder(stage);
            }
        });
        panel.getChildren().add(btnAddFolder);



        stage.show();
    }


}
