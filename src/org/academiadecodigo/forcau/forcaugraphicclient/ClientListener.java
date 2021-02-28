package org.academiadecodigo.forcau.forcaugraphicclient;

import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener implements Runnable {

    private Socket socket;
    private BufferedReader read;
    private String lineRead = "";
    private Client clientMain;
    private Graphic game;

    public ClientListener(Socket socket, Client clientMain) {
        this.socket = socket;
        this.clientMain = clientMain;
    }

    public void setGraphicsHandler(Graphic game) {
        this.game = game;
    }

    @Override
    public void run() {

        try {
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (!clientMain.isStarted()) {
            try {
                lineRead = read.readLine();

                System.out.println(lineRead);

                if (lineRead != null) {

                    if (lineRead.contains("characters")) {

                        int numOfCharacters = Integer.parseInt(lineRead.split(" ")[3]);

                        clientMain.startGraphicGame(numOfCharacters);

                    }
                }

            } catch (IOException error) {
                System.err.println(error);
            }
        }

        while (clientMain.isStarted()) {
            try {

                lineRead = read.readLine();

                if (lineRead.equals("")) continue;

                if (lineRead.contains(":(")) {
                    game.hangingPartsSchedule();
                    continue;
                }

                if (lineRead.contains("[0;") || lineRead.contains("[1;")) lineRead = lineRead.substring(lineRead.indexOf("m") + 1);

                if (lineRead.contains("[0m")) lineRead = lineRead.substring(0, lineRead.indexOf("[") - 1);

                if (lineRead.contains("found")) {
                    game.newMessageToConsole(lineRead);
                    lineRead = read.readLine();

                    if (lineRead.contains("[0m")) lineRead = lineRead.substring(0, lineRead.indexOf("[") - 1);

                    String[] splitedWord = lineRead.split(" ");
                    for (int i = 0; i < splitedWord.length; i++) {

                        if (!splitedWord[i].contains("_")) {
                            game.wordField.drawChar(i, splitedWord[i]);
                        }
                    }
                    continue;
                }

                if (lineRead.contains("won")) {
                    Picture youWon = new Picture(0,0,"resources/youwon.jpg");
                    youWon.draw();
                    Thread.sleep(5000);
                    System.exit(0);
                }

                if (lineRead.contains("failed")) {
                    Picture youLost = new Picture(0,0,"resources/endgame.jpg");
                    youLost.draw();
                    Thread.sleep(4000);
                    System.exit(0);

                }

                game.newMessageToConsole(lineRead);
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
