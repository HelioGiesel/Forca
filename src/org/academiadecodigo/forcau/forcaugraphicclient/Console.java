package org.academiadecodigo.forcau.forcaugraphicclient;

import org.academiadecodigo.simplegraphics.graphics.Rectangle;

import java.util.Iterator;
import java.util.LinkedList;

public class Console {
    LinkedList<TextBox> logList;

    Console() {
        buildConsole();
        logList = new LinkedList<>();
    }

    private void buildConsole() {

        Rectangle console = new Rectangle(500,20,400,560);
        console.draw();
    }

    public void buildTextBox(String incomingMessage) {

        TextBox textBox = new TextBox(520, 530, 360,40, incomingMessage);

        Iterator<TextBox> logListIterator = logList.listIterator();
        while (logListIterator.hasNext()) {
            TextBox element = logListIterator.next();
            element.moveUp();
            if (element.getRectangleY() < 10) {
                element.deleteBox();
                logListIterator.remove();
            }
        }

        textBox.drawChar();

        logList.add(textBox);
    }
}
