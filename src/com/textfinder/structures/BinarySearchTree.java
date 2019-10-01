package com.textfinder.structures;

class BinarySearchTree {

    // Root of BST
    Node root;

    /* Class containing left and right child of current node and key value*/
    class Node {
        KeyNode key;
        Node left, right;

        public Node(KeyNode item) {
            key = item;
            left = right = null;
        }
    }

    // Constructor
    BinarySearchTree() {
        root = null;
    }

    // This method mainly calls insertRec()
    void insert(KeyNode key) {
        root = insertRec(root, key);
    }

    /* A recursive function to insert a new key in BST */
    Node insertRec(Node root, KeyNode key) {

        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        // Otherwise, recur down the tree
        if (key.getWord().compareTo(root.key.getWord()) < 0)
            root.left = insertRec(root.left, key);
        else if (key.getWord().compareTo(root.key.getWord()) > 0)
            root.right = insertRec(root.right, key);

        // return the (unchanged) node pointer
        return root;

        /*
        // Otherwise, recur down the tree
        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);

        // return the (unchanged) node pointer
        return root;
        */
    }

    // This method mainly calls InorderRec()
    void inorder()  {
        inorderRec(root);
    }

    // A utility function to do inorder traversal of BST
    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.key.toString());
            //System.out.println(root.key.getWord());
            inorderRec(root.right);
        }
    }

    // This method mainly calls InorderRec()
    void inorderSearchWord(String pWord, Occurrence pOccurrence)  {
        inorderSearchWordRec(root, pWord, pOccurrence);
    }

    // A utility function to do inorder traversal of BST
    void inorderSearchWordRec(Node root, String pWord, Occurrence pOccurrence) {
        if (root != null) {
            inorderSearchWordRec(root.left,  pWord,  pOccurrence);
            if(root.key.getWord().equals(pWord)){
                root.key.getOccurrenceList().add(pOccurrence);
            }
            //System.out.println(root.key.getWord());
            inorderSearchWordRec(root.right,  pWord,  pOccurrence);
        }
    }

}