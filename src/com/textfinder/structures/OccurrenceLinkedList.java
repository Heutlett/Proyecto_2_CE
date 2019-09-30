package com.textfinder.structures;

public class OccurrenceLinkedList {

    private NodeList first;
    private NodeList last;
    private int size = 0;

    /**
     * @author Adrian Araya Ramirez
     *
     * @version 1.8
     *
     */
    private static class NodeList {

        private Occurrence occurrence;
        private NodeList next;

        /**
         * @param pOcurrence
         */
        public NodeList(Occurrence pOcurrence)
        {
            occurrence = pOcurrence;
            next = null;
        }
    }

    /**
     *
     *
     *
     * @return boolean
     */
    private boolean isEmpty() {
        if(first==null) {
            return true;
        }
        return false;
    }

    /**
     *
     *
     *
     * @param pOccurrence
     */
    public void add(Occurrence pOccurrence) {
        if(isEmpty()) {
            this.first = new NodeList(pOccurrence);
        }else {
            NodeList aux = first;

            while(aux.next != null) {
                aux = aux.next;
            }
            aux.next = new NodeList(pOccurrence);
        }
        size++;
    }

    /**
     *
     *
     *
     * @param pIndex
     * @return Occurrence
     */
    public Occurrence get(int pIndex) {

        if(isEmpty()) {
            return null;
        }else {
            NodeList aux = first;
            int counter = 0;
            while(aux != null) {
                if(counter == pIndex) {
                    return aux.occurrence;
                }
                aux = aux.next;
                counter++;
            }
        }
        return null;
    }

    /**
     *
     *
     *
     * @param pName
     * @return Gate
     */
    public Occurrence getByName(String pName) {

        if(isEmpty()) {
            return null;
        }else {
            NodeList aux = first;
            while(aux != null) {
                if(aux.occurrence.getDocumentName().equals(pName)) {
                    return aux.occurrence;
                }
                aux = aux.next;
            }
        }
        return null;
    }

    /**
     *
     *
     *
     * @param pOcurrence
     * @return boolean
     */
    public boolean find(Occurrence pOcurrence){

        NodeList aux = first;

        boolean finded = false;


        while(aux != null && finded != true){

            if (pOcurrence == aux.occurrence){

                finded = true;
            }
            else{

                aux = aux.next;
            }
        }

        return finded;
    }

    /**
     *
     *
     *
     * @param pOccurrence
     */
    public void remove(Occurrence pOccurrence) {

        if (find(pOccurrence)) {

            if (first.occurrence == pOccurrence) {

                first = first.next;
            } else{

                NodeList aux = first;

                while(aux.next.occurrence != pOccurrence){
                    aux = aux.next;
                }

                NodeList tmp = aux.next.next;
                aux.next = tmp;
            }
            size--;
        }

    }

    /**
     *
     * Devuelve el tamano de la lista.
     *
     * @return int
     */
    public int size() {
        return size;
    }

    public String toString(){

        String string = "[";
        NodeList aux = first;

        while(aux != null){
            string += aux.occurrence.toString() + ", ";
            aux = aux.next;
        }

        return string + "]";

    }

}
