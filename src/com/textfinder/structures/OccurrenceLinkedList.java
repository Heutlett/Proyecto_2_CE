package com.textfinder.structures;

/**
 *
 * @autor Adrian Araya Ramirez
 * @author Daniel Quesada Camacho
 *
 * @version 1.8
 *
 */
public class OccurrenceLinkedList {

    private NodeList first;
    private NodeList last;
    private int size = 0;

    /**
     * @author Adrian Araya Ramirez
     * @autor Daniel Quesada Camacho
     * @version 1.8
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
     * Este metodo devuelve true si la lista esta vacia y false de lo contrario.
     * @return boolean
     */
    private boolean isEmpty() {
        if(first==null) {
            return true;
        }
        return false;
    }

    /**
     * Este metodo agrega una ocurrencia al principio de la lista.
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
     * Este metodo devuelve la ocurrencia que se encuentra en el indice pasado por parametro.
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
     * Busca un occurrence en la lista que coincida con el nombre pasado por parametro y lo devuelve.
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
     * Devuelve true si la lista contiene el ocurrence pasado por parametro.
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
     * Busca el occurrence pasado por parametro y si lo contiene lo borra.
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
     * Devuelve el tamano de la lista.
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
