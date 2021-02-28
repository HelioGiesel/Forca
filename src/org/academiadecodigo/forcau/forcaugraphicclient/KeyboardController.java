package org.academiadecodigo.forcau.forcaugraphicclient;

import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

public class KeyboardController implements KeyboardHandler {
    //Fields
    private Keyboard keyboard;
    private int[] charKey;
    Graphic game;


    //Constructor
    public  KeyboardController(Graphic game){
        this.game = game;
        keyboard = new Keyboard(this);
        mapKey();
        initKeyboard();

    }

    public void initKeyboard(){
        KeyboardEvent[] events = new KeyboardEvent[27];
        for (int i = 0; i < events.length; i++) {
            events[i] = new KeyboardEvent();
            events[i].setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            events[i].setKey(charKey[i]);
            keyboard.addEventListener(events[i]);
        }

    }

    public void mapKey(){
        charKey = new int[]{
                KeyboardEvent.KEY_A,
                KeyboardEvent.KEY_B,
                KeyboardEvent.KEY_C,
                KeyboardEvent.KEY_D,
                KeyboardEvent.KEY_E,
                KeyboardEvent.KEY_F,
                KeyboardEvent.KEY_G,
                KeyboardEvent.KEY_H,
                KeyboardEvent.KEY_I,
                KeyboardEvent.KEY_J,
                KeyboardEvent.KEY_K,
                KeyboardEvent.KEY_L,
                KeyboardEvent.KEY_M,
                KeyboardEvent.KEY_N,
                KeyboardEvent.KEY_O,
                KeyboardEvent.KEY_P,
                KeyboardEvent.KEY_Q,
                KeyboardEvent.KEY_R,
                KeyboardEvent.KEY_S,
                KeyboardEvent.KEY_T,
                KeyboardEvent.KEY_U,
                KeyboardEvent.KEY_V,
                KeyboardEvent.KEY_W,
                KeyboardEvent.KEY_X,
                KeyboardEvent.KEY_Y,
                KeyboardEvent.KEY_Z,
                KeyboardEvent.KEY_SPACE
        };
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()){
            case KeyboardEvent.KEY_A:
                game.appendCharToString("a");
                break;
            case KeyboardEvent.KEY_B:
                game.appendCharToString("b");
                break;
            case KeyboardEvent.KEY_C:
                game.appendCharToString("c");
                break;
            case KeyboardEvent.KEY_D:
                game.appendCharToString("d");
                break;
            case KeyboardEvent.KEY_E:
                game.appendCharToString("e");
                break;
            case KeyboardEvent.KEY_F:
                game.appendCharToString("f");
                break;
            case KeyboardEvent.KEY_G:
                game.appendCharToString("g");
                break;
            case KeyboardEvent.KEY_H:
                game.appendCharToString("h");
                break;
            case KeyboardEvent.KEY_I:
                game.appendCharToString("i");
                break;
            case KeyboardEvent.KEY_J:
                game.appendCharToString("j");
                break;
            case KeyboardEvent.KEY_K:
                game.appendCharToString("k");
                break;
            case KeyboardEvent.KEY_L:
                game.appendCharToString("l");
                break;
            case KeyboardEvent.KEY_M:
                game.appendCharToString("m");
                break;
            case KeyboardEvent.KEY_N:
                game.appendCharToString("n");
                break;
            case KeyboardEvent.KEY_O:
                game.appendCharToString("o");
                break;
            case KeyboardEvent.KEY_P:
                game.appendCharToString("p");
                break;
            case KeyboardEvent.KEY_Q:
                game.appendCharToString("q");
                break;
            case KeyboardEvent.KEY_R:
                game.appendCharToString("r");
                break;
            case KeyboardEvent.KEY_S:
                game.appendCharToString("s");
                break;
            case KeyboardEvent.KEY_T:
                game.appendCharToString("t");
                break;
            case KeyboardEvent.KEY_U:
                game.appendCharToString("u");
                break;
            case KeyboardEvent.KEY_V:
                game.appendCharToString("v");
                break;
            case KeyboardEvent.KEY_W:
                game.appendCharToString("w");
                break;
            case KeyboardEvent.KEY_X:
                game.appendCharToString("x");
                break;
            case KeyboardEvent.KEY_Y:
                game.appendCharToString("y");
                break;
            case KeyboardEvent.KEY_Z:
                game.appendCharToString("z");
                break;
            case KeyboardEvent.KEY_SPACE:
                game.sendClientMessage();
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }
}
