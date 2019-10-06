package com.textfinder.structures;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.filemanagers.DOCXManager;
import com.textfinder.filemanagers.PDFManager;
import com.textfinder.filemanagers.TXTManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private static BinarySearchTree binarySearchTree;
    private static ArrayList<String> words;
    private static int wordsIndexed = 0;
    private static Label[] labels;
    //private static

    private Indexing() {
        binarySearchTree = new BinarySearchTree();
        words = new ArrayList<String>();
    }

    public Indexing getInstance() {
        return INSTANCE;
    }

    public static void parseDocuments() {

        binarySearchTree = new BinarySearchTree();

        wordsIndexed = 0;

        //Recorre la lista de archivos buscando los pdf y parseandolos e indexandolos
        for (int i = 0; i < DocumentLibrary.files.size(); i++) {


            parseFile(DocumentLibrary.files.get(i));

        }

        binarySearchTree.inorderPrint();

        Dialogs.showInformationDialog("Success", wordsIndexed + " words have been indexed");

    }

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

    //Divide un pdf en palabras y las almacena en la lista words
    private static void parseFile(File pFile) {

        String words[] = getWords(pFile);

        int counter = 0;

        for (int i = 0; i < words.length; i++) {

            wordsIndexed++;

            if (!words[i].equals("") && !words[i].equals("\n") && !words[i].equals(" ")) {
                //Crea el occurrence de cada palabra

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

    public static void textSearch(String pText, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile, Label[] pLabels) {

        labels = pLabels;

        if (pText.split(" ").length == 1) {//Una palabra

            wordSearch(pText, pVBox, pTextFlow, btnOpenFile);

        } else { //Una frase

        }

    }

    private static void wordSearch(String pWord, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile) {

        System.out.println("Buscando la palabra: " + pWord);
        KeyNode node = binarySearchTree.searchWord(pWord);
        pVBox.getChildren().clear();

        if (node != null) {
            System.out.println("Se encontraron " + node.getOccurrenceList().size() + " resultados");
            for (int i = 0; i < node.getOccurrenceList().size(); i++) {

                Button button = createButton(node.getOccurrenceList().get(i), pTextFlow, btnOpenFile, pWord);
                button.setId(node.getOccurrenceList().get(i).getDocument().getAbsolutePath());
                pVBox.getChildren().add(button);

            }

        }else{
            Dialogs.showErrorDialog("Failed", "Word not found");
        }
    }

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

    private static Button createButton(Occurrence pOccurrence, TextFlow pTextFlow, Button btnOpenFile, String pWordSearched){

        Button button = new Button(pOccurrence.getDocumentName());
        String finalPath = pOccurrence.getDocument().getAbsolutePath();
        String finalText = getText(pOccurrence.getDocument());

        ArrayList<Node> nodes = new ArrayList<Node>();

        for(int e = 0; e < finalText.length(); e++){

            Text text;

            if(e + pWordSearched.length()<= finalText.length() && finalText.substring(e, e + pWordSearched.length()).equals(pWordSearched) ) {
                text = new Text(pWordSearched + " ");
                text.setFill(Color.RED);
                text.setUnderline(true);
                e = e + pWordSearched.length();
            }else{
                text = new Text(""+finalText.charAt(e));
                text.setFill(Color.BLACK);
            }

            nodes.add(text);
        }


        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pTextFlow.getChildren().clear();

                clearLabels();

                updateLabels(pOccurrence.getDocumentName(), pWordSearched, ""+pOccurrence.getPosition().size());

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
        return button;
    }

    private static void updateLabels(String pDocumentName, String pWord, String pAparitions){

        labels[0].setText(labels[0].getText()+pDocumentName);
        labels[1].setText(labels[1].getText()+pWord);
        labels[2].setText(labels[2].getText()+pAparitions);
    }

    private static void clearLabels(){
        labels[0].setText("Document: ");
        labels[1].setText("Word: ");
        labels[2].setText("Aparitions: ");
    }


}