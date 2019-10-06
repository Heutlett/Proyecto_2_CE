package com.textfinder.structures;

/**
 *
 */
class BinarySearchTree {

    private Node root;
    private KeyNode result;

    /**
     *
     */
    class Node {
        KeyNode key;
        Node left, right;

        public Node(KeyNode item) {
            key = item;
            left = right = null;
        }
    }

    /**
     *
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     *
     * Inserta un nodo
     *
     * @param key
     */
    public void insert(KeyNode key) {
        root = insertRec(root, key);
    }

    /**
     *
     * Llama recursiva para insertar el nodo
     *
     * @param root
     * @param key
     * @return
     */
    public Node insertRec(Node root, KeyNode key) {

        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (key.getWord().compareTo(root.key.getWord()) < 0)
            root.left = insertRec(root.left, key);
        else if (key.getWord().compareTo(root.key.getWord()) > 0)
            root.right = insertRec(root.right, key);

        return root;

    }

    /**
     * Recorre el arbol recursivamente inroder imprimiendo sus nodos
     */
    public void inorderPrint()  {
        inorderPrintRec(root);
    }
    public void inorderPrintRec(Node root) {
        if (root != null) {
            inorderPrintRec(root.left);
            System.out.println(root.key.toString());
            //System.out.println(root.key.getWord());
            inorderPrintRec(root.right);
        }
    }

    /**
     *
     * Busca la palabra inroder pasada por parametro y devuelve el nodo
     *
     * @param pWord
     * @return KeyNode
     */
    KeyNode searchWord(String pWord)  {
        result = null;
        searchWordRec(root, pWord);
        return result;
    }

    /**
     *
     * Llamada recursiva inorder para buscar la palabra.
     *
     * @param root
     * @param pWord
     */
    public void searchWordRec(Node root, String pWord) {
        if (root != null) {
            searchWordRec(root.left, pWord);
            if(root.key.getWord().equals(pWord)){
                result = root.key;
            }
            searchWordRec(root.right, pWord);
        }
    }
}