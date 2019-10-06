package com.textfinder.view;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.structures.Dialogs;
import com.textfinder.structures.Indexing;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javafx.scene.paint.Color;

import static com.textfinder.structures.Indexing.*;


public class Window extends Application {

    private Stage stage;
    private Pane root;

    private Pane libraryPanel;
    private ScrollPane scrollPaneLibrary;
    private VBox vBoxLibrary;
    private ArrayList<CheckBox> checkboxListLibraryFiles;

    private Pane documentsResultPanel;
    private ScrollPane scrollPaneDocumentsResult;
    private VBox vBoxDocumentsResult;

    private Pane resultPanel;
    private TextFlow textFlow;
    private Pane searchPanel;
    private TextField textFieldSearch;

    private RadioButton radioButtonSortByName;
    private RadioButton radioButtonSortByDateCreated;
    private RadioButton radioButtonSortBySize;

    private static String fileName;

    private Label lblResultDocumentName;
    private Label lblResultWord;
    private Label lblResultAparitions;

    private Button btnOpenFile;

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage stage){

        DocumentLibrary.updateFileList();
        checkboxListLibraryFiles = new ArrayList<CheckBox>();

        this.stage = stage;
        stage.setTitle("Text Finder");

        //Se crea el panel y se agrega al stage
        Pane panel = new Pane();
        root = panel;
        stage.setScene(new Scene(panel, 1360, 760));

        initLibraryPanel();
        initDocumentsResultPanel();
        initResultPanel();

        stage.show();
    }

    public static void setFileName(String pFileName){
        fileName = pFileName;
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
                parseDocuments();
                Dialogs.showInformationDialog("Success", "The file list has been updated successfully");
            }
        });
        libraryPanel.getChildren().add(btnRefreshFiles);

        //Boton de indexar archivos
        Button btnIndex = new Button();
        btnIndex.setLayoutX(48);
        btnIndex.setLayoutY(250);
        btnIndex.setPrefSize(198, 31);
        btnIndex.setText("Index");
        btnIndex.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.getScene().setCursor(Cursor.WAIT);
            }
        });
        btnIndex.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        btnIndex.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parseDocuments();
            }
        });
        libraryPanel.getChildren().add(btnIndex);

        //Scroll pane

        scrollPaneLibrary = new ScrollPane();
        scrollPaneLibrary.setPrefSize(270, 439);
        scrollPaneLibrary.setLayoutX(24);
        scrollPaneLibrary.setLayoutY(305);
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

    private void initDocumentsResultPanel(){

        documentsResultPanel = new Pane();
        documentsResultPanel.setPrefSize(320, 760);
        documentsResultPanel.setLayoutX(1040);
        documentsResultPanel.setLayoutY(0);
        documentsResultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(documentsResultPanel);

        Label lblTitle = new Label("List of results");
        lblTitle.setLayoutX(69);
        lblTitle.setLayoutY(22);
        lblTitle.setFont(new Font(30));
        lblTitle.setStyle("-fx-font-weight: bold;");
        lblTitle.setTextFill(Color.rgb(255, 255, 255));
        documentsResultPanel.getChildren().add(lblTitle);

        Label sortBy = new Label("Sort by");
        sortBy.setLayoutX(24);
        sortBy.setLayoutY(105);
        sortBy.setTextFill(Color.rgb(255, 255, 255));
        documentsResultPanel.getChildren().add(sortBy);

        radioButtonSortByName = new RadioButton("Name");
        radioButtonSortByName.setLayoutX(24);
        radioButtonSortByName.setLayoutY(140);
        radioButtonSortByName.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortByName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(vBoxDocumentsResult.getChildren().size() != 0){
                    if(radioButtonSortByDateCreated.isSelected()){
                        radioButtonSortByDateCreated.setSelected(false);
                    }
                    if(radioButtonSortBySize.isSelected()){
                        radioButtonSortBySize.setSelected(false);
                    }
                    sortByName();
                }else{
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortByName.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortByName);

        radioButtonSortByDateCreated = new RadioButton("Date created");
        radioButtonSortByDateCreated.setLayoutX(24);
        radioButtonSortByDateCreated.setLayoutY(170);
        radioButtonSortByDateCreated.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortByDateCreated.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(vBoxDocumentsResult.getChildren().size() != 0){
                    if(radioButtonSortByName.isSelected()){
                        radioButtonSortByName.setSelected(false);
                    }
                    if(radioButtonSortBySize.isSelected()){
                        radioButtonSortBySize.setSelected(false);
                    }
                    sortByDateCreated();
                }else{
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortByDateCreated.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortByDateCreated);

        radioButtonSortBySize = new RadioButton("Size");
        radioButtonSortBySize.setLayoutX(24);
        radioButtonSortBySize.setLayoutY(200);
        radioButtonSortBySize.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortBySize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(vBoxDocumentsResult.getChildren().size() != 0){
                    if(radioButtonSortByDateCreated.isSelected()){
                        radioButtonSortByDateCreated.setSelected(false);
                    }
                    if(radioButtonSortByName.isSelected()){
                        radioButtonSortByName.setSelected(false);
                    }
                    sortBySize();
                }else{
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortBySize.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortBySize);

        btnOpenFile = new Button();
        btnOpenFile.setLayoutX(24);
        btnOpenFile.setLayoutY(240);
        btnOpenFile.setPrefSize(198, 31);
        btnOpenFile.setText("Open File");
        documentsResultPanel.getChildren().add(btnOpenFile);

        scrollPaneDocumentsResult = new ScrollPane();
        scrollPaneDocumentsResult.setPrefSize(270, 439);
        scrollPaneDocumentsResult.setLayoutX(24);
        scrollPaneDocumentsResult.setLayoutY(305);
        scrollPaneDocumentsResult.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneDocumentsResult.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        documentsResultPanel.getChildren().add(scrollPaneDocumentsResult);

        vBoxDocumentsResult = new VBox();
        vBoxDocumentsResult.setPrefSize(251, 100);
        scrollPaneDocumentsResult.setContent(vBoxDocumentsResult);


        //Llamar funcion para re indexar todoo

    }

    private void initResultPanel(){

        resultPanel = new Pane();
        resultPanel.setPrefSize(716,674);
        resultPanel.setLayoutX(322);
        resultPanel.setLayoutY(84);
        resultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(255, 220, 192), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(resultPanel);

        lblResultDocumentName = new Label("Document: ");
        lblResultDocumentName.setLayoutX(10);
        lblResultDocumentName.setLayoutY(10);
        lblResultDocumentName.setTextFill(Color.rgb(102,51,0));
        resultPanel.getChildren().add(lblResultDocumentName);

        lblResultWord = new Label("Word: ");
        lblResultWord.setLayoutX(300);
        lblResultWord.setLayoutY(10);
        lblResultWord.setTextFill(Color.rgb(102,51,0));
        resultPanel.getChildren().add(lblResultWord);

        lblResultAparitions = new Label("Aparitions: ");
        lblResultAparitions.setLayoutX(620);
        lblResultAparitions.setLayoutY(10);
        lblResultAparitions.setTextFill(Color.rgb(102,51,0));
        resultPanel.getChildren().add(lblResultAparitions);

        textFlow = new TextFlow();
        textFlow.setPrefSize(706, 627);
        textFlow.setLayoutX(5);
        textFlow.setLayoutY(44);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setLineSpacing(5.0);
        textFlow.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        resultPanel.getChildren().add(textFlow);

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
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label[] labels = {lblResultDocumentName, lblResultWord, lblResultAparitions};
                if (!textFieldSearch.getText().equals("")) {
                    Indexing.textSearch(textFieldSearch.getText(), vBoxDocumentsResult, textFlow, btnOpenFile, labels);
                }else{ //Vacio
                    Dialogs.showErrorDialog("Failed", "The textfield is empty.");
                }
            }
        });
        searchPanel.getChildren().add(btnSearch);

    }

    private Button[] getButtons(){
        Button [] buttons = new Button[vBoxDocumentsResult.getChildren().size()];

        for(int i = 0; i < vBoxDocumentsResult.getChildren().size(); i++){

            buttons[i] = ((Button)vBoxDocumentsResult.getChildren().get(i));

        }

        return buttons;
    }


    private void sortBySize(){

        Button [] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento redixsort
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones

            //Para obtener el tamano de un archivo
            File file = new File(buttons[i].getId()); //El id de boton es la ruta del archivo, de esta manera se abre el archivo con este nombre
            long fileSize = file.length(); //obtiene el peso del archivo

            System.out.println("Archivo = " + file.getName() + " peso = " + file.length());

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }
    }

    private void sortByName(){

        Button [] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento quicksort
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones

            //Para obtener el nombre de un archivo
            String name = buttons[i].getText(); //El boton tiene como nombre el nombre del archivo

            System.out.println("Nombre = " + name);

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }

    }

    private void sortByDateCreated(){

        Button [] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento bubblesort
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones

            //Para la fecha de creacion de un archivo
            File file = new File(buttons[i].getId()); //El id de boton es la ruta del archivo, de esta manera se abre el archivo con este nombre
            long fileDateCreated = getFileCreationEpoch(file); //obtiene el peso del archivo

            System.out.println("Archivo = " + file.getName() + " fecha = " + fileDateCreated);

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for(int i = 0; i < buttons.length; i++){ //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }

    }

    public static long getFileCreationEpoch (File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return attr.creationTime()
                    .toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

}
