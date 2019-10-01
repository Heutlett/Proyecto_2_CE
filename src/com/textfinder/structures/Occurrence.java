package com.textfinder.structures;

import java.io.File;

public class Occurrence {

    private File document;
    private String documentName;
    private int position;

    public Occurrence(File document, String documentName, int position) {
        this.document = document;
        this.documentName = documentName;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String toString(){
        return "Document=" + document.toString() + " DocumentName=" + documentName + " Position=" + position;
    }

}
