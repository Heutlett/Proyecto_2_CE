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

    private static String[] getWords(File pFile){

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

        return text.split(" ");

        }catch (IOException e) {
            Dialogs.showErrorDialog("Failed", "Cannot find the file");
            return null;
        }

    }

    //Divide un pdf en palabras y las almacena en la lista words
    private static void parseFile(File pFile){

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

    public void textSearch(String pText){

    }

    public void openDocument(){

    }

}
