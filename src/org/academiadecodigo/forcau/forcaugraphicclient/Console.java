package org.academiadecodigo.forcau.forcaugraphicclient;

import org.academiadecodigo.simplegraphics.graphics.Color;
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

        Rectangle console = new Rectangle(400,20,500,560);
        console.setColor(Color.LIGHT_GRAY);
        console.fill();
    }

    public void buildTextBox(String incomingMessage) {

        TextBox textBox = new TextBox(420, 530, 460,40, incomingMessage);

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
