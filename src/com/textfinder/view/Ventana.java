package com.textfinder.view;

import com.textfinder.documentlibrary.DocumentLibrary;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.paint.Color;


public class Ventana extends Application {

    private Stage stage;
    private Pane root;
    private Pane libraryPanel;
    private VBox vBoxLibrary;

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage stage){

        this.stage = stage;
        stage.setTitle("Text Finder");

        //Se crea el panel y se agrega al stage
        Pane panel = new Pane();
        root = panel;
        stage.setScene(new Scene(panel, 1360, 760));

        initLibraryPanel(panel);

        stage.show();
    }

    private void initLibraryPanel(Pane pParent){

        //Panel de libreria

        libraryPanel = new Pane();
        libraryPanel.setPrefSize(320,760);
        libraryPanel.setLayoutX(0);
        libraryPanel.setLayoutY(0);
        libraryPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        pParent.getChildren().add(libraryPanel);

        //Label del titulo panel de libreria

        Label lblTitle = new Label("File manager tools");
        lblTitle.setLayoutX(30);
        lblTitle.setLayoutY(22);
        lblTitle.setFont(new Font(30));
        lblTitle.setStyle("-fx-font-weight: bold;");
        lblTitle.setTextFill(Color.rgb(255,255,255));
        libraryPanel.getChildren().add(lblTitle);

        //Boton de agregar archivos
        Button btnAddFile = new Button();
        btnAddFile.setLayoutX(48);
        btnAddFile.setLayoutY(90);
        btnAddFile.setPrefSize(198,31);
        btnAddFile.setText("Add file");
        btnAddFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFile(stage);
            }
        });
        libraryPanel.getChildren().add(btnAddFile);

        //Boton de agregar folders
        Button btnAddFolder = new Button();
        btnAddFolder.setLayoutX(48);
        btnAddFolder.setLayoutY(130);
        btnAddFolder.setPrefSize(198,31);
        btnAddFolder.setText("Add folder");
        btnAddFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFolder(stage);
            }
        });
        libraryPanel.getChildren().add(btnAddFolder);

        //Boton de borrar archivos
        Button btnDeleteFile = new Button();
        btnDeleteFile.setLayoutX(48);
        btnDeleteFile.setLayoutY(170);
        btnDeleteFile.setPrefSize(198,31);
        btnDeleteFile.setText("Delete files");
        btnDeleteFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.deleteFiles("");
            }
        });
        libraryPanel.getChildren().add(btnDeleteFile);

        //Boton de borrar archivos
        Button btnRefreshFiles = new Button();
        btnRefreshFiles.setLayoutX(48);
        btnRefreshFiles.setLayoutY(210);
        btnRefreshFiles.setPrefSize(198,31);
        btnRefreshFiles.setText("Refresh files");
        btnRefreshFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.refreshFiles("");
            }
        });
        libraryPanel.getChildren().add(btnRefreshFiles);

        //Scroll pane

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(270,475);
        scrollPane.setLayoutX(24);
        scrollPane.setLayoutY(269);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        libraryPanel.getChildren().add(scrollPane);

        //Container vbox

        vBoxLibrary = new VBox();
        vBoxLibrary.setPrefSize(251, 100);

        scrollPane.setContent(vBoxLibrary);

    }

}
