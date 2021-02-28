package org.academiadecodigo.forcau.forcaugraphicclient;

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
                if (lineRead != null) {
                    System.out.println(lineRead);
                    if (lineRead.contains("characters")) {
                        System.out.println("entrou if" + lineRead.split(" ")[3]);
                        int numOfCharacters = Integer.parseInt(lineRead.split(" ")[3]);
                        System.out.println("passou int " + numOfCharacters);
                        clientMain.startGraphicGame(numOfCharacters);
                        System.out.println("passou startGraphics");
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

                if (lineRead.contains("found")) {
                    game.newMessageToConsole(lineRead);
                    lineRead = read.readLine();
                    String[] splitedWord = lineRead.split(" ");
                    for (int i = 0; i < splitedWord.length; i++) {
                        System.out.println(splitedWord[i]);
                        if (!splitedWord[i].equals("_")) {
                            game.wordField.drawChar(i, splitedWord[i]);
                        }
                    }
                    continue;
                }

                game.newMessageToConsole(lineRead);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
