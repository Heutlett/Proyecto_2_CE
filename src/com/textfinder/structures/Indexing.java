package com.textfinder.structures;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.filemanagers.DOCXManager;
import com.textfinder.filemanagers.PDFManager;
import com.textfinder.filemanagers.TXTManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 * @version 1.8
 */
public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private static BinarySearchTree binarySearchTree;
    private static ArrayList<String> words;
    private static int wordsIndexed = 0;
    private static Label[] labels;
    private static boolean isIndexed = false;

    /**
     * Constructor
     */
    private Indexing() {
        binarySearchTree = new BinarySearchTree();
        words = new ArrayList<String>();
    }

    /**
     * Retorna la instancia unica de la clase (singleton)
     * @return Indexing
     */
    public Indexing getInstance() {
        return INSTANCE;
    }

    /**
     * Parsea los documentos.
     */
    public static void parseDocuments() {

        binarySearchTree = new BinarySearchTree();
        wordsIndexed = 0;

        for (int i = 0; i < DocumentLibrary.files.size(); i++) {
            parseFile(DocumentLibrary.files.get(i));
        }

        binarySearchTree.inorderPrint();
        Dialogs.showInformationDialog("Success", wordsIndexed + " words have been indexed");
        isIndexed = true;
    }

    /**
     * Devuelve un arreglo con las palabras del documento
     * @param pFile
     * @return String[]
     */
    private static String[] getWords(File pFile) {

        String text = "";

        if (DocumentLibrary.getFileExtension(pFile).equals("pdf")) {
            text = PDFManager.toText(pFile.getAbsolutePath());
        } else {
            if (DocumentLibrary.getFileExtension(pFile).equals("docx")) {

                text = DOCXManager.getText(pFile.getAbsolutePath());

            } else {
                if (DocumentLibrary.getFileExtension(pFile).equals("txt")) {
                    text = TXTManager.getText(pFile.getAbsolutePath());
                }
            }
        }
        return text.split(" ");
    }

    /**
     * Indexa todas las palabras en el arbol binario de busqueda
     * @param pFile
     */
    private static void parseFile(File pFile) {

        String words[] = getWords(pFile);
        int counter = 0;

        for (int i = 0; i < words.length; i++) {

            wordsIndexed++;

            if (!words[i].equals("") && !words[i].equals("\n") && !words[i].equals(" ")) {

                Occurrence occurrence = new Occurrence(pFile, pFile.getName());
                counter++;
                KeyNode node = binarySearchTree.searchWord(words[i]);
                boolean added = false;

                if(node != null){
                    for(int e = 0; e < node.getOccurrenceList().size(); e++){

                        if(node.getOccurrenceList().get(e).getDocumentName().equals(pFile.getName())){

                            node.getOccurrenceList().get(e).addPositions(counter);
                            added = true;
                        }
                    }
                    if(!added){
                        occurrence.addPositions(counter);
                        node.getOccurrenceList().add(occurrence);
                    }
                }else{
                    occurrence.addPositions(counter);
                    Indexing.words.add(words[i]);
                    OccurrenceLinkedList occurrenceLinkedList = new OccurrenceLinkedList();
                    occurrenceLinkedList.add(occurrence);
                    binarySearchTree.insert(new KeyNode(words[i], occurrenceLinkedList));
                }
            }
        }
    }

    /**
     * Busca la palabra en el arbol binario de busqueda
     * @param pText
     * @param pVBox
     * @param pTextFlow
     * @param btnOpenFile
     * @param pLabels
     */
    public static void textSearch(String pText, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile, Label[] pLabels) {

        labels = pLabels;

        if(isIndexed){

            if (pText.split(" ").length == 1) {//Una palabra

                wordSearch(pText, pVBox, pTextFlow, btnOpenFile);

            } else { //Una frase

                phraseSearch(pText, pVBox, pTextFlow, btnOpenFile);

            }
        }else{
            Dialogs.showErrorDialog("Indexing error", "You must index to perform the search");
        }

    }

    /**
     * Agrega los documentos que contienen la frase buscada a la lista de documentos resultantes.
     * @param pWord
     * @param pVBox
     * @param pTextFlow
     * @param btnOpenFile
     */
    private static void phraseSearch(String pWord, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile){

        System.out.println("Buscando la frase: " + pWord);
        pVBox.getChildren().clear();

        ArrayList<File> files = DocumentLibrary.files;

        for(int i = 0; i < files.size(); i++){

            String text = getText(files.get(i));

            for(int e = 0; e < text.length(); e++){

                if((e + pWord.length() <= text.length()) && pWord.equals(text.substring(e, e + pWord.length()))){

                    e = e + pWord.length();

                    Button button = createButtonPhrase(files.get(i).getName(),files.get(i).getAbsolutePath(),getText(files.get(i)), pTextFlow, btnOpenFile, pWord);
                    button.setId(files.get(i).getAbsolutePath());
                    pVBox.getChildren().add(button);

                }
            }
        }

    }

    /**
     * Agrega los documentos que contienen la palabra buscada en la lista de documentos resultantes.
     * @param pWord
     * @param pVBox
     * @param pTextFlow
     * @param btnOpenFile
     */
    private static void wordSearch(String pWord, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile) {

        System.out.println("Buscando la palabra: " + pWord);
        KeyNode node = binarySearchTree.searchWord(pWord);
        pVBox.getChildren().clear();

        if (node != null) {
            System.out.println("Se encontraron " + node.getOccurrenceList().size() + " resultados");
            for (int i = 0; i < node.getOccurrenceList().size(); i++) {

                Button button = createButtonWord(node.getOccurrenceList().get(i), pTextFlow, btnOpenFile, pWord);
                button.setId(node.getOccurrenceList().get(i).getDocument().getAbsolutePath());
                pVBox.getChildren().add(button);
            }

        }else{
            Dialogs.showErrorDialog("Failed", "Word not found");
        }
    }

    /**
     * Obtiene el texto de los archivos resultantes.
     * @param pFile
     * @return
     */
    private static String getText(File pFile){

        if (DocumentLibrary.getFileExtension(pFile).equals("pdf")) {
            return PDFManager.getPlainText(pFile.getAbsolutePath());
        }

        if (DocumentLibrary.getFileExtension(pFile).equals("txt")) {
            return TXTManager.getPlainText(pFile.getAbsolutePath());
        }

        if (DocumentLibrary.getFileExtension(pFile).equals("docx")) {
            return DOCXManager.getPlainText(pFile.getAbsolutePath());
        }
        return null;

    }

    /**
     * Crea y le da la funcion a los botones que abriran los documentos y mostraran los resultados de las frases subrayadas.
     * @param pDocumentName
     * @param pPath
     * @param pText
     * @param pTextFlow
     * @param btnOpenFile
     * @param pWordSearched
     * @return
     */
    private static Button createButtonPhrase(String pDocumentName, String pPath, String pText, TextFlow pTextFlow, Button btnOpenFile, String pWordSearched){

        Button button = new Button(pDocumentName); //Nombra el boton con el nombre de documento
        String finalPath = pPath; //Guarda la ruta del documento
        String finalText = pText; //Almacena el texto del documento

        ArrayList<Node> nodes = new ArrayList<Node>(); //Crea la lista de nodos que almacenara los textos del textflow
        int aparitions = 0;

        for(int e = 0; e < finalText.length(); e++){

            Text text;

            if((e + pWordSearched.length() <= finalText.length()) && pWordSearched.equals(finalText.substring(e, e + pWordSearched.length()))){
                aparitions++;
                text = new Text(" " + pWordSearched + " ");
                text.setFill(Color.YELLOW);
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));

                text.setStrokeWidth(1);

                text.setStroke(Color.BLACK);
                e = e + pWordSearched.length(); //Mueve el marcador de caracteres despues de la palabra

            }else{
                text = new Text(""+finalText.charAt(e));
                text.setFill(Color.BLACK);
            }
            nodes.add(text);
        }

        addAction(button,pDocumentName,pTextFlow,btnOpenFile,pWordSearched,nodes,finalPath, aparitions);

        return button;
    }

    /**
     * Crea y le da la funcion a los botones que abriran los documentos y mostraran los resultados de las palabras subrayadas.
     * @param pOccurrence
     * @param pTextFlow
     * @param btnOpenFile
     * @param pWordSearched
     * @return
     */
    private static Button createButtonWord(Occurrence pOccurrence, TextFlow pTextFlow, Button btnOpenFile, String pWordSearched){

        Button button = new Button(pOccurrence.getDocumentName()); //Nombra el boton con el nombre de documento
        String finalPath = pOccurrence.getDocument().getAbsolutePath(); //Guarda la ruta del documento
        String finalText = getText(pOccurrence.getDocument()); //Almacena el texto del documento

        ArrayList<Node> nodes = new ArrayList<Node>(); //Crea la lista de nodos que almacenara los textos del textflow

        int counter = 0;
        int aparitions = 0;

        for(int e = 0; e < finalText.length(); e++){

            Text text;

            if(finalText.charAt(e) == ' ' || finalText.charAt(e) == '\n') { //Busca los espacios para contar las palabras
                counter++;
                text = new Text(""+finalText.charAt(e));
                text.setFill(Color.BLACK);
            }

            if(pOccurrence.getPosition().contains(counter)){ //Si la ocurrencia tiene esta posicion en su lista de posiciones la pinta diferente
                    aparitions++;
                    text = new Text(" " + pWordSearched + " ");
                    text.setFill(Color.YELLOW);
                    text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));

                    text.setStrokeWidth(1);

                    text.setStroke(Color.BLACK);
                    e = e + pWordSearched.length(); //Mueve el marcador de caracteres despues de la palabra

                    pOccurrence.getPosition().remove((Integer)counter); //Elimina la posicion de la lista de posiciones de la palabra
            }else{
                text = new Text(""+finalText.charAt(e));
                text.setFill(Color.BLACK);
            }
            nodes.add(text);
        }

        addAction(button,pOccurrence.getDocumentName(),pTextFlow,btnOpenFile,pWordSearched,nodes,finalPath, aparitions);

        return button;
    }

    /**
     * Agrega las funciones al boton del documento resultante
     * @param pButton
     * @param pDocumentName
     * @param pTextFlow
     * @param btnOpenFile
     * @param pWordSearched
     * @param nodes
     * @param finalPath
     */
    private static void addAction(Button pButton, String pDocumentName, TextFlow pTextFlow, Button btnOpenFile, String pWordSearched, ArrayList<Node> nodes, String finalPath, int pAparitions){
        pButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pTextFlow.getChildren().clear();
                clearLabels();
                updateLabels(pDocumentName, pWordSearched, ""+pAparitions);

                for (int i = 0; i < nodes.size(); i++) {
                    pTextFlow.getChildren().add(nodes.get(i));
                }

                btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        File file = new File(finalPath);
                        Desktop desktop = Desktop.getDesktop();
                        if(file.exists())//checks file exists or not
                        {
                            try {
                                desktop.open(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Actualiza los labels que muestran la informacion de la busqueda.
     * @param pDocumentName
     * @param pWord
     * @param pAparitions
     */
    private static void updateLabels(String pDocumentName, String pWord, String pAparitions){
        labels[0].setText(labels[0].getText()+pDocumentName);
        labels[1].setText(labels[1].getText()+pWord);
        labels[2].setText(labels[2].getText()+pAparitions);
    }

    /**
     * Limpia la informacion de los labels de busqueda.
     */
    private static void clearLabels(){
        labels[0].setText("Document: ");
        labels[1].setText("Word: ");
        labels[2].setText("Aparitions: ");
    }
}