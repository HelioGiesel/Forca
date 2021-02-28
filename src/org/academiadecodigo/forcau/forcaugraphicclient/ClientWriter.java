package org.academiadecodigo.forcau.forcaugraphicclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriter implements Runnable {
    //Fields
    private Socket socket;
    private PrintWriter write;
    private String currentMessage = "";
    private TextBox userInput;
    private Client clientMain;
    private Graphic game;
    private Scanner input;

    //Constructor
    public ClientWriter(Socket socket, Client clientMain) {
        this.socket = socket;
        this.clientMain = clientMain;
        input = new Scanner(System.in);
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
        System.out.println("Entrou no run");
        try {
            write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Passou do write");

        while(true) {
            System.out.println("Entrou no while da root");
            String sentence = input.nextLine();
            System.out.println(sentence + " Deu certo miseria!");
            write.println(sentence);

            write.flush();

        }
    }
}