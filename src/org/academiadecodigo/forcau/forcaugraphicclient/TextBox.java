package org.academiadecodigo.forcau.forcaugraphicclient;

import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.graphics.Text;

public class TextBox {

    private Rectangle rectangle;
    private Text textShape;
    private int y;

    TextBox(int x, int y, int width, int height, String text) {

        this.y = y;

        this.rectangle = new Rectangle(x, y, width, height);
        this.textShape = new Text(x + 12, y + 15, text);

        rectangle.draw();
    }

    public void setTextShape(String text) {

        this.textShape.setText(text);
        this.textShape.draw();
    }

    public void deleteBox(){

        rectangle.delete();
        textShape.delete();
    }

    public void drawChar(){
        textShape.draw();
    }

    public void moveUp() {

        if (y < 10) {
            deleteBox();
        } else {
            rectangle.translate(0, -50);
            textShape.translate(0, -50);
            y -= 50;
        }
    }

    public int getRectangleY() {
        return rectangle.getY();
    }
}
