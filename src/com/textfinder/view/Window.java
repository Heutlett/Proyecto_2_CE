package com.textfinder.view;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.structures.Dialogs;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.paint.Color;


public class Window extends Application {

    private Stage stage;
    private Pane root;

    private Pane libraryPanel;
    private ScrollPane scrollPaneLibrary;
    private VBox vBoxLibrary;
    private ArrayList<CheckBox> checkboxListLibraryFiles;

    private Pane libraryResultPanel;
    private ScrollPane scrollPaneLibraryResult;
    private VBox vBoxLibraryResult;
    private ArrayList<Button> buttonsLibraryResult;

    private Pane resultPanel;

    private Pane searchPanel;
    private TextField textFieldSearch;

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage stage){

        DocumentLibrary.updateFileList();
        checkboxListLibraryFiles = new ArrayList<CheckBox>();
        buttonsLibraryResult = new ArrayList<Button>();

        this.stage = stage;
        stage.setTitle("Text Finder");

        //Se crea el panel y se agrega al stage
        Pane panel = new Pane();
        root = panel;
        stage.setScene(new Scene(panel, 1360, 760));

        initLibraryPanel();
        initLibraryResultPanel();
        initResultPanel();

        stage.show();
    }

    private void initLibraryPanel() {

        //Panel de libreria

        libraryPanel = new Pane();
        libraryPanel.setPrefSize(320, 760);
        libraryPanel.setLayoutX(0);
        libraryPanel.setLayoutY(0);
        libraryPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(libraryPanel);

        //Label del titulo panel de libreria

        Label lblTitle = new Label("File manager tools");
        lblTitle.setLayoutX(30);
        lblTitle.setLayoutY(22);
        lblTitle.setFont(new Font(30));
        lblTitle.setStyle("-fx-font-weight: bold;");
        lblTitle.setTextFill(Color.rgb(255, 255, 255));
        libraryPanel.getChildren().add(lblTitle);

        //Boton de agregar archivos
        Button btnAddFile = new Button();
        btnAddFile.setLayoutX(48);
        btnAddFile.setLayoutY(90);
        btnAddFile.setPrefSize(198, 31);
        btnAddFile.setText("Add file");
        btnAddFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFile(stage);
                updateCheckBox();
            }
        });
        libraryPanel.getChildren().add(btnAddFile);

        //Boton de agregar folders
        Button btnAddFolder = new Button();
        btnAddFolder.setLayoutX(48);
        btnAddFolder.setLayoutY(130);
        btnAddFolder.setPrefSize(198, 31);
        btnAddFolder.setText("Add folder");
        btnAddFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.addFolder(stage);
                updateCheckBox();
            }
        });
        libraryPanel.getChildren().add(btnAddFolder);

        //Boton de borrar archivos
        Button btnDeleteFile = new Button();
        btnDeleteFile.setLayoutX(48);
        btnDeleteFile.setLayoutY(170);
        btnDeleteFile.setPrefSize(198, 31);
        btnDeleteFile.setText("Delete files");
        btnDeleteFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeCheckBoxFiles();

            }
        });
        libraryPanel.getChildren().add(btnDeleteFile);

        //Boton de refrescar archivos
        Button btnRefreshFiles = new Button();
        btnRefreshFiles.setLayoutX(48);
        btnRefreshFiles.setLayoutY(210);
        btnRefreshFiles.setPrefSize(198, 31);
        btnRefreshFiles.setText("Refresh files");
        btnRefreshFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentLibrary.refreshFiles();
                updateCheckBox();
                Dialogs.showInformationDialog("Success", "La lista de archivos se ha actualizado correctamente");
            }
        });
        libraryPanel.getChildren().add(btnRefreshFiles);

        //Scroll pane

        scrollPaneLibrary = new ScrollPane();
        scrollPaneLibrary.setPrefSize(270, 475);
        scrollPaneLibrary.setLayoutX(24);
        scrollPaneLibrary.setLayoutY(269);
        scrollPaneLibrary.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLibrary.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        libraryPanel.getChildren().add(scrollPaneLibrary);

        //Container vbox
        updateCheckBox();


    }

    private void updateCheckBox(){
        vBoxLibrary = new VBox();
        vBoxLibrary.setPrefSize(251, 100);
        checkboxListLibraryFiles = new ArrayList<CheckBox>();
        for (int i = 0; i < DocumentLibrary.filesString.size(); i++) {

            CheckBox checkBox = new CheckBox(DocumentLibrary.filesString.get(i));
            checkboxListLibraryFiles.add(checkBox);
            vBoxLibrary.getChildren().add(checkBox);
        }
        scrollPaneLibrary.setContent(vBoxLibrary);

        //Llamar funcion para re indexar todoo.
    }

    private void removeCheckBoxFiles(){

        String success = "";
        String failed = "";
        String finalText = "";

        for(int i = 0; i < checkboxListLibraryFiles.size(); i++){

            if(checkboxListLibraryFiles.get(i).isSelected()){
                vBoxLibrary.getChildren().remove(i);

                if(DocumentLibrary.deleteFile(checkboxListLibraryFiles.get(i).getText())){
                    success += checkboxListLibraryFiles.get(i).getText() + "\n";
                }else{
                    failed += checkboxListLibraryFiles.get(i).getText() + "\n";
                }
                checkboxListLibraryFiles.remove(i);
                i--;
            }
        }
        if(success != ""){
            finalText += "Se han borrado correctamente los siguientes archivos: \n\n" + success;
        }
        if(failed != ""){
            finalText += "No se han podido borrar los siguientes archivos: \n\n" + failed;
        }
        if(finalText != ""){
            Dialogs.showInformationDialog("Result", finalText);
        }

    }

    private void initLibraryResultPanel(){

        libraryResultPanel = new Pane();
        libraryResultPanel.setPrefSize(320, 760);
        libraryResultPanel.setLayoutX(1040);
        libraryResultPanel.setLayoutY(0);
        libraryResultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(libraryResultPanel);

        Label lblTitle = new Label("List of results");
        lblTitle.setLayoutX(69);
        lblTitle.setLayoutY(22);
        lblTitle.setFont(new Font(30));
        lblTitle.setStyle("-fx-font-weight: bold;");
        lblTitle.setTextFill(Color.rgb(255, 255, 255));
        libraryResultPanel.getChildren().add(lblTitle);

        Label sortBy = new Label("Sort by");
        sortBy.setLayoutX(24);
        sortBy.setLayoutY(105);
        sortBy.setTextFill(Color.rgb(255, 255, 255));
        libraryResultPanel.getChildren().add(sortBy);

        RadioButton radioButtonSortByName = new RadioButton("Name");
        radioButtonSortByName.setLayoutX(24);
        radioButtonSortByName.setLayoutY(140);
        radioButtonSortByName.setTextFill(Color.rgb(255, 255, 255));
        libraryResultPanel.getChildren().add(radioButtonSortByName);

        RadioButton radioButtonSortByDateCreated = new RadioButton("Date created");
        radioButtonSortByDateCreated.setLayoutX(24);
        radioButtonSortByDateCreated.setLayoutY(170);
        radioButtonSortByDateCreated.setTextFill(Color.rgb(255, 255, 255));
        libraryResultPanel.getChildren().add(radioButtonSortByDateCreated);

        RadioButton radioButtonSortBySize = new RadioButton("Size");
        radioButtonSortBySize.setLayoutX(24);
        radioButtonSortBySize.setLayoutY(200);
        radioButtonSortBySize.setTextFill(Color.rgb(255, 255, 255));
        libraryResultPanel.getChildren().add(radioButtonSortBySize);

        scrollPaneLibraryResult = new ScrollPane();
        scrollPaneLibraryResult.setPrefSize(270, 475);
        scrollPaneLibraryResult.setLayoutX(24);
        scrollPaneLibraryResult.setLayoutY(269);
        scrollPaneLibraryResult.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneLibraryResult.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        libraryResultPanel.getChildren().add(scrollPaneLibraryResult);

    }

    private void initResultPanel(){

        resultPanel = new Pane();
        resultPanel.setPrefSize(716,674);
        resultPanel.setLayoutX(322);
        resultPanel.setLayoutY(84);
        resultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(255, 220, 192), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(resultPanel);

        searchPanel = new Pane();
        searchPanel.setPrefSize(716,82);
        searchPanel.setLayoutX(322);
        searchPanel.setLayoutY(0);
        searchPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(searchPanel);

        textFieldSearch = new TextField();
        textFieldSearch.setPromptText("Enter a word or phrase to search");
        textFieldSearch.setPrefSize(514,31);
        textFieldSearch.setLayoutX(29);
        textFieldSearch.setLayoutY(26);
        searchPanel.getChildren().add(textFieldSearch);

        Button btnSearch = new Button("Search");
        btnSearch.setLayoutX(594);
        btnSearch.setLayoutY(26);
        searchPanel.getChildren().add(btnSearch);

    }

}
