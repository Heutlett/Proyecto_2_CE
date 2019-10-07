package com.textfinder.structures;

/**
 * @author Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 */
public class KeyNode {

    private String word;
    private OccurrenceLinkedList occurrenceList;

    /**
     * Constructor
     * @param word
     * @param occurrenceList
     */
    public KeyNode(String word, OccurrenceLinkedList occurrenceList) {
        this.word = word;
        this.occurrenceList = occurrenceList;
    }

    /**
     *
     * @return String
     */
    public String getWord() {
        return word;
    }

    /**
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     *
     * @return OccurrenceLinkedList
     */
    public OccurrenceLinkedList getOccurrenceList() {
        return occurrenceList;
    }

    /**
     *
     * @param occurrenceList
     */
    public void setOccurrenceList(OccurrenceLinkedList occurrenceList) {
        this.occurrenceList = occurrenceList;
    }

    /**
     *
     * @return String
     */
    public String toString(){
        return "word: " + word + ", occurrenceList: " + occurrenceList.toString() + "\n";
    }
}
