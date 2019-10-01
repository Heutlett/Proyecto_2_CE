package com.textfinder.structures;

import com.textfinder.documentlibrary.DocumentLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private static BinarySearchTree binarySearchTree;
    private static ArrayList<String> words;

    private Indexing(){
        binarySearchTree = new BinarySearchTree();
        words = new ArrayList<String>();
    }

    public Indexing getInstance(){
        return INSTANCE;
    }

    public static void parseDocuments(){

        //Recorre la lista de archivos buscando los pdf y parseandolos e indexandolos
        for(int i = 0; i < DocumentLibrary.files.size(); i++){

            if(DocumentLibrary.getFileExtension(DocumentLibrary.files.get(i)).equals("pdf")){
                parsePDF(DocumentLibrary.files.get(i));
            }
        }

        binarySearchTree.inorder();

    }



    //Divide un pdf en palabras y las almacena en la lista words
    private static void parsePDF(File pFile){
        try{
            String text = PDFManager.toText(pFile.getAbsolutePath());

            String palabra[] = text.split(" ");

            for(int i = 0; i < palabra.length; i++){

                if(!palabra[i].equals("") && !palabra[i].contains("\n")){
                    //Crea el occurrence de cada palabra
                    Occurrence occurrence = new Occurrence(pFile, pFile.getName(), i);
                    //Si la palabra ya se habia agregado al indexado
                    if(!words.contains(palabra[i])){
                        words.add(palabra[i]);
                        OccurrenceLinkedList occurrenceLinkedList = new OccurrenceLinkedList();
                        occurrenceLinkedList.add(occurrence);
                        binarySearchTree.insert(new KeyNode(palabra[i], occurrenceLinkedList));
                    }else{ //Si la palabra ya estaba indexada solo agrega la ocurrencia
                        binarySearchTree.inorderSearchWord(palabra[i],occurrence);
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
