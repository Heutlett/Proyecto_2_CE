package com.textfinder.structures;

public class KeyNode {

    private String word;
    private OccurrenceLinkedList occurrenceList;

    public KeyNode(String word, OccurrenceLinkedList occurrenceList) {
        this.word = word;
        this.occurrenceList = occurrenceList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public OccurrenceLinkedList getOccurrenceList() {
        return occurrenceList;
    }

    public void setOccurrenceList(OccurrenceLinkedList occurrenceList) {
        this.occurrenceList = occurrenceList;
    }

    public String toString(){
        return "word: " + word + " occurrenceList: " + occurrenceList.toString() + "\n";
    }
}
