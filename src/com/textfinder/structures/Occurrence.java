package com.textfinder.structures;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 * @version 1.8
 */
public class Occurrence {

    private File document;
    private String documentName;
    private ArrayList<Integer> positions = new ArrayList<Integer>();

    /**
     * Constructor
     * @param document
     * @param documentName
     */
    public Occurrence(File document, String documentName) {
        this.document = document;
        this.documentName = documentName;
    }

    /**
     *
     * @return File
     */
    public File getDocument() {
        return document;
    }

    /**
     *
     * @param document
     */
    public void setDocument(File document) {
        this.document = document;
    }

    /**
     *
     * @return String
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     *
     * @param documentName
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     *
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getPosition() {
        return positions;
    }

    /**
     *
     * @param position
     */
    public void addPositions(int position) {
        this.positions.add(position);
    }

    /**
     *
     * @return String
     */
    public String toString(){
        return "File=" +documentName  + " Position=" + this.positions.toString();
    }
}
