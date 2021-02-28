package org.academiadecodigo.forcau.forcaugraphicclient;

public class WordField {

    private int wordLength;
    private TextBox[] textBoxList;

    WordField(int wordLength) {

        this.wordLength = wordLength;
        this.textBoxList = new TextBox[wordLength];
    }

    public void buildBottomWord() {

        for (int i = 0; i < wordLength; i++) {

            TextBox textBox = new TextBox(50 + (i*45), 600, 30, 40, "");
            textBoxList[i] = textBox;
        }
    }

    public void drawChar(int index, String guessedChar) {
        textBoxList[index].setTextShape(guessedChar);
    }

    public void deleteShapes(){

        for (int i = 0; i < wordLength; i++) {

            textBoxList[i].deleteBox();
        }
    }
}
