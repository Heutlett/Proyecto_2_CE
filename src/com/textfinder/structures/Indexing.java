package com.textfinder.structures;

public class Indexing {

    private static final Indexing INSTANCE = new Indexing();
    private BinarySearchTree binarySearchTree;

    private Indexing(){
        binarySearchTree = new BinarySearchTree();
    }

    public Indexing getInstance(){
        return INSTANCE;
    }

    public void parseDocuments(){

    }






    public void textSearch(String pText){

    }

    public void openDocument(){

    }

}
