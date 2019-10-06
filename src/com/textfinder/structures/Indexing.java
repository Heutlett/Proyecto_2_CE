package com.textfinder.structures;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.filemanagers.DOCXManager;
import com.textfinder.filemanagers.PDFManager;
import com.textfinder.filemanagers.TXTManager;
import com.textfinder.view.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;

public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private static BinarySearchTree binarySearchTree;
    private static ArrayList<String> words;
    private static int wordsIndexed = 0;
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

        binarySearchTree.inorder();

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
                Occurrence occurrence = new Occurrence(pFile, pFile.getName(), counter);
                counter++;
                //Si la palabra ya se habia agregado al indexado
                if (!Indexing.words.contains(words[i])) {
                    Indexing.words.add(words[i]);
                    OccurrenceLinkedList occurrenceLinkedList = new OccurrenceLinkedList();
                    occurrenceLinkedList.add(occurrence);
                    binarySearchTree.insert(new KeyNode(words[i], occurrenceLinkedList));
                } else { //Si la palabra ya estaba indexada solo agrega la ocurrencia
                    binarySearchTree.inorderSearchWord(words[i], occurrence);
                }
            }

        }
    }

    public static void textSearch(String pText, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile) {

        if (pText.split(" ").length == 1) {//Una palabra

            wordSearch(pText, pVBox, pTextFlow, btnOpenFile);

        } else { //Una frase

        }

    }

    private static void wordSearch(String pWord, VBox pVBox, TextFlow pTextFlow, Button btnOpenFile) {

        System.out.println("Buscando la palabra: " + pWord);
        KeyNode node = binarySearchTree.searchWord(pWord);

        if (node != null) {
            System.out.println("Se encontraron " + node.getOccurrenceList().size() + " resultados");
            for (int i = 0; i < node.getOccurrenceList().size(); i++) {

                Button button = createButton(node.getOccurrenceList().get(i), pTextFlow, btnOpenFile);
                button.setId(node.getOccurrenceList().get(i).getDocument().getAbsolutePath());
                pVBox.getChildren().add(button);

            }


        }else{
            Dialogs.showErrorDialog("Failed", "Word not found");
        }
    }

    private static Button createButton(Occurrence pOccurrence, TextFlow pTextFlow, Button btnOpenFile){

        String text = "";

        Button button = new Button(pOccurrence.getDocumentName());
        String finalPath = pOccurrence.getDocument().getAbsolutePath();
        String finalText = "";

        if (DocumentLibrary.getFileExtension(pOccurrence.getDocument()).equals("pdf")) {
            finalText = PDFManager.getPlainText(pOccurrence.getDocument().getAbsolutePath());
        }

        if (DocumentLibrary.getFileExtension(pOccurrence.getDocument()).equals("txt")) {
            finalText = TXTManager.getPlainText(pOccurrence.getDocument().getAbsolutePath());;
        }

        if (DocumentLibrary.getFileExtension(pOccurrence.getDocument()).equals("docx")) {
            finalText = DOCXManager.getPlainText(pOccurrence.getDocument().getAbsolutePath());
        }

        ArrayList<Text> texts = new ArrayList<Text>();
        Text text1 = new Text(finalText.substring(0,5));
        text1.setFill(Color.DARKBLUE);
        Text text2 = new Text(finalText.substring(5,10));
        text2.setFill(Color.RED);
        Text text3 = new Text(finalText.substring(10,15));
        text3.setFill(Color.GREEN);
        texts.add(text1);
        texts.add(text2);
        texts.add(text3);


        String finalText1 = finalText;
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pTextFlow.getChildren().clear();
                pTextFlow.getChildren().add(texts.get(0));
                pTextFlow.getChildren().add(texts.get(1));
                pTextFlow.getChildren().add(texts.get(2));
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


}