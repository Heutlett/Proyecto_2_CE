package com.textfinder.structures;

import java.io.File;
import java.util.ArrayList;

public class Occurrence {

    private File document;
    private String documentName;
    private ArrayList<Integer> positions = new ArrayList<Integer>();

    public Occurrence(File document, String documentName) {
        this.document = document;
        this.documentName = documentName;
    }

    public File getDocument() {
        return document;
    }

    public void setDocument(File document) {
        this.document = document;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public ArrayList<Integer> getPosition() {
        return positions;
    }

    public void addPositions(int position) {
        this.positions.add(position);
    }

    public String toString(){
        return "File=" +documentName  + " Position=" + this.positions.toString();
    }

}
