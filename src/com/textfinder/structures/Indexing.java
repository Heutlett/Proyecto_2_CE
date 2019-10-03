package com.textfinder.structures;

import com.textfinder.documentlibrary.DocumentLibrary;
import com.textfinder.filemanagers.DOCXManager;
import com.textfinder.filemanagers.PDFManager;
import com.textfinder.filemanagers.TXTManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private static BinarySearchTree binarySearchTree;
    private static ArrayList<String> words;
    private static int wordsIndexed = 0;

    private Indexing(){
        binarySearchTree = new BinarySearchTree();
        words = new ArrayList<String>();
    }

    public Indexing getInstance(){
        return INSTANCE;
    }

    public static void parseDocuments(){

        binarySearchTree = new BinarySearchTree();

        wordsIndexed = 0;

        //Recorre la lista de archivos buscando los pdf y parseandolos e indexandolos
        for(int i = 0; i < DocumentLibrary.files.size(); i++){


            parseFile(DocumentLibrary.files.get(i));

        }

        binarySearchTree.inorder();

        Dialogs.showInformationDialog("Success",   wordsIndexed + " words have been indexed");

    }



    //Divide un pdf en palabras y las almacena en la lista words
    private static void parseFile(File pFile){
        try {

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



            String palabra[] = text.split(" ");

            int counter = 0;

            for (int i = 0; i < palabra.length; i++) {

                wordsIndexed++;

                if (!palabra[i].equals("") && !palabra[i].equals("\n") && !palabra[i].equals(" ")) {
                    //Crea el occurrence de cada palabra
                    Occurrence occurrence = new Occurrence(pFile, pFile.getName(), counter);
                    counter++;
                    //Si la palabra ya se habia agregado al indexado
                    if (!words.contains(palabra[i])) {
                        words.add(palabra[i]);
                        OccurrenceLinkedList occurrenceLinkedList = new OccurrenceLinkedList();
                        occurrenceLinkedList.add(occurrence);
                        binarySearchTree.insert(new KeyNode(palabra[i], occurrenceLinkedList));
                    } else { //Si la palabra ya estaba indexada solo agrega la ocurrencia
                        binarySearchTree.inorderSearchWord(palabra[i], occurrence);
                    }
                }

            }

        }catch (IOException e) {
            Dialogs.showErrorDialog("Failed", "Cannot find the file");
        }
    }



    public void textSearch(String pText){

    }

    public void openDocument(){

    }

}
