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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import static com.textfinder.structures.Indexing.parseDocuments;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel
 *
 * @version 1.8
 */
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
    private Label lblResultDocumentName;
    private Label lblResultWord;
    private Label lblResultAparitions;
    private Button btnOpenFile;
    private RadioButton radioButtonByWord;
    private RadioButton radioButtonByPhrase;

    /**
     * Main del programa
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    /**
     * Devuelve la fecha de creacion del archivo pasado por parametro (en un formato de milisegundos para comparar con otros archivos)
     * @param file
     * @return
     */
    public static long getFileCreationEpoch(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return attr.creationTime()
                    .toInstant().toEpochMilli();
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }

    /**
     * Inicia la interfaz.
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        stage.setResizable(false);

        //Actualiza los documentos de la biblioteca
        DocumentLibrary.updateFileList();
        checkboxListLibraryFiles = new ArrayList<CheckBox>();

        this.stage = stage;
        stage.setTitle("Text Finder");

        //Se crea el panel y se agrega al stage
        Pane panel = new Pane();
        root = panel;
        stage.setScene(new Scene(panel, 1350, 750));

        //Inicializa todos los paneles con sus componentes
        initLibraryPanel();
        initDocumentsResultPanel();
        initResultPanel();

        stage.show();
    }

    /**
     * Crea el panel que muestra y controla los documentos disponibles en la libreria
     */
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
                DocumentLibrary.addFile(stage); //Agrega un archivo a la biblioteca
                updateCheckBox(); //Actualiza la lista que muestra estos archivos
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
                DocumentLibrary.addFolder(stage); //Agrega una carpeta de archivos a la biblioteca
                updateCheckBox(); //Actualiza la lista que muestra los documentos
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
                removeCheckBoxFiles(); //Borra los archivos seleccionados
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
                DocumentLibrary.refreshFiles(); //Actualiza los archivos de la biblioteca
                updateCheckBox(); //Actualiza la lista de documentos de la biblioteca
                parseDocuments(); //Indexea nuevamente los documentos.
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
                stage.getScene().setCursor(Cursor.WAIT); //Pone el cursor en wait
            }
        });
        btnIndex.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.getScene().setCursor(Cursor.DEFAULT); //Pone el cursor default
            }
        });
        btnIndex.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parseDocuments(); //Indexa los documentos
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
        vBoxLibrary = new VBox();
        vBoxLibrary.setPrefSize(251, 100);
        updateCheckBox(); //Actualiza la lista de documentos de la biblioteca
    }

    /**
     * Actualiza la lista de documentos de la biblioteca
     */
    private void updateCheckBox() {
        vBoxLibrary.getChildren().clear(); //Limpia la lista de documentos de container
        checkboxListLibraryFiles = new ArrayList<CheckBox>();
        for (int i = 0; i < DocumentLibrary.filesString.size(); i++) { //Agrega los documentos a la lista de documentos del container

            CheckBox checkBox = new CheckBox(DocumentLibrary.filesString.get(i));
            checkboxListLibraryFiles.add(checkBox);
            vBoxLibrary.getChildren().add(checkBox);
        }
        scrollPaneLibrary.setContent(vBoxLibrary);
    }

    /**
     * Elimina los documentos seleccionados de la lista de documentos
     */
    private void removeCheckBoxFiles() {

        String success = "";
        String failed = "";
        String finalText = "";

        for (int i = 0; i < checkboxListLibraryFiles.size(); i++) { //Recorre la lista de checkbox buscando los documentos que esten seleccionados para borrarlos

            if (checkboxListLibraryFiles.get(i).isSelected()) {
                vBoxLibrary.getChildren().remove(i);

                if (DocumentLibrary.deleteFile(checkboxListLibraryFiles.get(i).getText())) {
                    success += checkboxListLibraryFiles.get(i).getText() + "\n";
                } else {
                    failed += checkboxListLibraryFiles.get(i).getText() + "\n";
                }
                checkboxListLibraryFiles.remove(i);
                i--;
            }
        }
        if (success != "") {
            finalText += "Se han borrado correctamente los siguientes archivos: \n\n" + success;
        }
        if (failed != "") {
            finalText += "No se han podido borrar los siguientes archivos: \n\n" + failed;
        }
        if (finalText != "") {
            Dialogs.showInformationDialog("Result", finalText);
        }
    }

    /**
     * Inicializa el panel que muestra los documentos que contienen la palabra buscada y sus componentes
     */
    private void initDocumentsResultPanel() {

        //Panel de resultados
        documentsResultPanel = new Pane();
        documentsResultPanel.setPrefSize(320, 760);
        documentsResultPanel.setLayoutX(1040);
        documentsResultPanel.setLayoutY(0);
        documentsResultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(documentsResultPanel);

        //Titulo del panel
        Label lblTitle = new Label("List of results");
        lblTitle.setLayoutX(69);
        lblTitle.setLayoutY(22);
        lblTitle.setFont(new Font(30));
        lblTitle.setStyle("-fx-font-weight: bold;");
        lblTitle.setTextFill(Color.rgb(255, 255, 255));
        documentsResultPanel.getChildren().add(lblTitle);

        //Titulo de los ordenamientos
        Label sortBy = new Label("Sort by");
        sortBy.setLayoutX(24);
        sortBy.setLayoutY(105);
        sortBy.setTextFill(Color.rgb(255, 255, 255));
        documentsResultPanel.getChildren().add(sortBy);

        //radio button de sortbyname
        radioButtonSortByName = new RadioButton("Name");
        radioButtonSortByName.setLayoutX(24);
        radioButtonSortByName.setLayoutY(140);
        radioButtonSortByName.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortByName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vBoxDocumentsResult.getChildren().size() != 0) { //Deselecciona los otros radiobuttons
                    if (radioButtonSortByDateCreated.isSelected()) {
                        radioButtonSortByDateCreated.setSelected(false);
                    }
                    if (radioButtonSortBySize.isSelected()) {
                        radioButtonSortBySize.setSelected(false);
                    }
                    sortByName(); //Ordena los archivos por nombre
                } else {
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortByName.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortByName);

        //Radio button de sortbydateCreated
        radioButtonSortByDateCreated = new RadioButton("Date created");
        radioButtonSortByDateCreated.setLayoutX(24);
        radioButtonSortByDateCreated.setLayoutY(170);
        radioButtonSortByDateCreated.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortByDateCreated.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vBoxDocumentsResult.getChildren().size() != 0) { //Deselecciona los demas radiobuttons
                    if (radioButtonSortByName.isSelected()) {
                        radioButtonSortByName.setSelected(false);
                    }
                    if (radioButtonSortBySize.isSelected()) {
                        radioButtonSortBySize.setSelected(false);
                    }
                    sortByDateCreated(); //Ordena los resultados por fecha de creacion
                } else {
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortByDateCreated.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortByDateCreated);

        //Radio button de sortbysize
        radioButtonSortBySize = new RadioButton("Size");
        radioButtonSortBySize.setLayoutX(24);
        radioButtonSortBySize.setLayoutY(200);
        radioButtonSortBySize.setTextFill(Color.rgb(255, 255, 255));
        radioButtonSortBySize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vBoxDocumentsResult.getChildren().size() != 0) { //Deselecciona los otros radiobuttons
                    if (radioButtonSortByDateCreated.isSelected()) {
                        radioButtonSortByDateCreated.setSelected(false);
                    }
                    if (radioButtonSortByName.isSelected()) {
                        radioButtonSortByName.setSelected(false);
                    }
                    sortBySize(); //Ordena los resultados por tamano
                } else {
                    Dialogs.showErrorDialog("Failed", "You must perform a search");
                    radioButtonSortBySize.setSelected(false);
                }
            }
        });
        documentsResultPanel.getChildren().add(radioButtonSortBySize);

        //boton que abre el archivo
        btnOpenFile = new Button();
        btnOpenFile.setLayoutX(24);
        btnOpenFile.setLayoutY(240);
        btnOpenFile.setPrefSize(198, 31);
        btnOpenFile.setText("Open File");
        documentsResultPanel.getChildren().add(btnOpenFile);

        //scroll panel de container de los resultados
        scrollPaneDocumentsResult = new ScrollPane();
        scrollPaneDocumentsResult.setPrefSize(270, 439);
        scrollPaneDocumentsResult.setLayoutX(24);
        scrollPaneDocumentsResult.setLayoutY(305);
        scrollPaneDocumentsResult.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneDocumentsResult.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        documentsResultPanel.getChildren().add(scrollPaneDocumentsResult);

        //container de los resultados
        vBoxDocumentsResult = new VBox();
        vBoxDocumentsResult.setPrefSize(251, 100);
        scrollPaneDocumentsResult.setContent(vBoxDocumentsResult);
    }

    /**
     * Inicializa el panel que muestra la vista previa del documento con las palabras buscadas
     */
    private void initResultPanel() {
        //Panel de la vista previa
        resultPanel = new Pane();
        resultPanel.setPrefSize(716, 674);
        resultPanel.setLayoutX(322);
        resultPanel.setLayoutY(84);
        resultPanel.setBackground(new Background(new BackgroundFill(Color.rgb(255, 220, 192), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(resultPanel);

        //label que muestra el nombre del documento
        lblResultDocumentName = new Label("Document: ");
        lblResultDocumentName.setLayoutX(10);
        lblResultDocumentName.setLayoutY(10);
        lblResultDocumentName.setTextFill(Color.rgb(102, 51, 0));
        resultPanel.getChildren().add(lblResultDocumentName);

        //label que muestra la palabra buscada
        lblResultWord = new Label("Word: ");
        lblResultWord.setLayoutX(350);
        lblResultWord.setLayoutY(10);
        lblResultWord.setTextFill(Color.rgb(102, 51, 0));
        resultPanel.getChildren().add(lblResultWord);

        //label que muestra la cantidad de apariciones de esa palabra en ese documento
        lblResultAparitions = new Label("Aparitions: ");
        lblResultAparitions.setLayoutX(620);
        lblResultAparitions.setLayoutY(10);
        lblResultAparitions.setTextFill(Color.rgb(102, 51, 0));
        resultPanel.getChildren().add(lblResultAparitions);

        //scrollpane que almacena la vista previa del texto
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(704,639);
        scrollPane.setLayoutX(4);
        scrollPane.setLayoutY(33);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        resultPanel.getChildren().add(scrollPane);

        //textflow que muestra la vista previa de los documentos resultantes
        textFlow = new TextFlow();
        textFlow.setPrefSize(704, 639);
        textFlow.setLayoutX(5);
        textFlow.setLayoutY(44);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setLineSpacing(5.0);
        textFlow.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setContent(textFlow);

        //panel de busqueda
        searchPanel = new Pane();
        searchPanel.setPrefSize(716, 82);
        searchPanel.setLayoutX(322);
        searchPanel.setLayoutY(0);
        searchPanel.setBackground(new Background(new BackgroundFill(Color.rgb(233, 150, 122), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(searchPanel);

        //textfield para ingresar la palabra a buscar
        textFieldSearch = new TextField();
        textFieldSearch.setPromptText("Enter a word or phrase to search");
        textFieldSearch.setPrefSize(472, 31);
        textFieldSearch.setLayoutX(29);
        textFieldSearch.setLayoutY(26);
        searchPanel.getChildren().add(textFieldSearch);

        //boton para buscar una palabra
        Button btnSearch = new Button("Search");
        btnSearch.setLayoutX(515);
        btnSearch.setLayoutY(26);
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label[] labels = {lblResultDocumentName, lblResultWord, lblResultAparitions};
                if (!textFieldSearch.getText().equals("")) {
                    Indexing.textSearch(textFieldSearch.getText(), vBoxDocumentsResult, textFlow, btnOpenFile, labels); //Busca la palabra en el arbol de indices
                } else { //Vacio
                    Dialogs.showErrorDialog("Failed", "The textfield is empty.");
                }
            }
        });
        searchPanel.getChildren().add(btnSearch);
        /*
        //boton de searchbyword
        radioButtonByWord = new RadioButton("By word");
        radioButtonByWord.setLayoutX(603);
        radioButtonByWord.setLayoutY(16);
        radioButtonByWord.setSelected(true);
        radioButtonByWord.setTextFill(Color.WHITE);
        radioButtonByWord.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                radioButtonByPhrase.setSelected(false); //deselecciona el otro radiobutton
            }
        });
        searchPanel.getChildren().add(radioButtonByWord);

        //boton de searchbyphrase
        radioButtonByPhrase = new RadioButton("By phrase");
        radioButtonByPhrase.setLayoutX(603);
        radioButtonByPhrase.setLayoutY(47);
        radioButtonByPhrase.setTextFill(Color.WHITE);
        radioButtonByPhrase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                radioButtonByWord.setSelected(false); //deselecciona el otro radiobutton
            }
        });
        searchPanel.getChildren().add(radioButtonByPhrase);
        
         */
    }

    /**
     * Devuelve los botones del container de botones que guardan los documentos resultantes
     * @return Button[]
     */
    private Button[] getButtons() {
        Button[] buttons = new Button[vBoxDocumentsResult.getChildren().size()];

        for (int i = 0; i < vBoxDocumentsResult.getChildren().size(); i++) {

            buttons[i] = ((Button) vBoxDocumentsResult.getChildren().get(i));

        }
        return buttons;
    }

    /**
     * Ordena los resultados del container por tamano con el algoritmo de radixsort
     */
    private void sortBySize() {

        Button[] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento redixsort
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones

            //Para obtener el tamano de un archivo
            File file = new File(buttons[i].getId()); //El id de boton es la ruta del archivo, de esta manera se abre el archivo con este nombre
            long fileSize = file.length(); //obtiene el peso del archivo

            System.out.println("Archivo = " + file.getName() + " peso = " + file.length());

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }
    }

    /**
     * Ordena los resultados del container por nombre con el algoritmo de quicksort
     */
    private void sortByName() {

        Button[] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento quicksort
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones

            //Para obtener el nombre de un archivo
            String name = buttons[i].getText(); //El boton tiene como nombre el nombre del archivo

            System.out.println("Nombre = " + name);

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }

    }

    /**
     * Ordena los resultados del container por fecha de creacion con el algoritmo de bubblesort
     */
    private void sortByDateCreated() {

        Button[] buttons = getButtons(); //Obtiene todos los botones (lista de documentos) y los almacena en un arreglo
        vBoxDocumentsResult.getChildren().clear(); //Vacia el container para agregar de nuevo los elementos en orden

        //Hacer ordenamiento bubblesort
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones

            //Para la fecha de creacion de un archivo
            File file = new File(buttons[i].getId()); //El id de boton es la ruta del archivo, de esta manera se abre el archivo con este nombre
            long fileDateCreated = getFileCreationEpoch(file); //obtiene el peso del archivo

            System.out.println("Archivo = " + file.getName() + " fecha = " + fileDateCreated);

        }

        //Una vez ordenados volver a recorrer la lista ya ordenada agregando nuevamente los botones al container
        for (int i = 0; i < buttons.length; i++) { //Recorre los botones
            vBoxDocumentsResult.getChildren().add(buttons[i]);
        }
    }
}