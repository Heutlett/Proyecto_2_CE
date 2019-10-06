package com.textfinder.structures;

public class Position {

    private int wordNumber;
    private int posInicial;
    private int posFinal;

    public Position(int wordNumber, int posInicial, int posFinal) {
        this.wordNumber = wordNumber;
        this.posInicial = posInicial;
        this.posFinal = posFinal;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public int getPosInicial() {
        return posInicial;
    }

    public void setPosInicial(int posInicial) {
        this.posInicial = posInicial;
    }

    public int getPosFinal() {
        return posFinal;
    }

    public void setPosFinal(int posFinal) {
        this.posFinal = posFinal;
    }
}
