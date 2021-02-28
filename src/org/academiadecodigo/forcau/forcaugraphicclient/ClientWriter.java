package org.academiadecodigo.forcau.forcaugraphicclient;

import java.io.*;
import java.net.Socket;

public class ClientWriter implements Runnable {
    //Fields
    private Socket socket;
    private PrintWriter write;
    private String currentMessage = "";
    private TextBox userInput;
    private Client clientMain;
    private Graphic game;

    //Constructor
    public ClientWriter(Socket socket, Client clientMain) {
        this.socket = socket;
        this.clientMain = clientMain;
    }

    public void setGraphicsHandler(Graphic game) {
        this.game = game;
    }

    public void buildMessageString(String message) {

        currentMessage += message;
        userInput.setTextShape(currentMessage);

    }

    public void sendMessage() {

        try {
            write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        write.println(currentMessage);
        write.flush();

        currentMessage = "";
        userInput.setTextShape("");

    }

    public void setUserInputBox(TextBox userInput){
        this.userInput = userInput;
    }

    @Override
    public void run() {

        BufferedReader in;

        try {
            write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (clientMain.isStarted()) {
            try {
                in = new BufferedReader(new InputStreamReader(System.in));
                String sentence = null;

                sentence = in.readLine();

                write.println(sentence);

                write.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("saiu caraio");
    }
}