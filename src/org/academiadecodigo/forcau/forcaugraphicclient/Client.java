package org.academiadecodigo.forcau.forcaugraphicclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    //Fields
    private final Socket socket;
    private final ExecutorService fixedPool;
    private String host;
    private int portNumber;
    private Graphic game;
    private boolean started;
    private ClientListener serverListener;
    private ClientWriter serverWriter;

    //Constructor
    public Client() throws IOException {
        started = false;
        setPortNumber();
        socket = new Socket(host, portNumber);
        fixedPool = Executors.newFixedThreadPool(8);
        start();
    }

    /**
     * Method start
     */
    public void start() {

        System.out.println("Client started: " + socket);
        System.out.println("Waiting for a server connection...");

        serverListener = new ClientListener(socket, this);
        serverWriter = new ClientWriter(socket, this);

        fixedPool.submit(serverListener);
        fixedPool.submit(serverWriter);

    }

    public void startGraphicGame(int numOfCharacters) {
        started = true;
        System.out.println("reached here");

        game = new Graphic(numOfCharacters);
        game.setClientWriter(serverWriter);
        game.setClientReader(serverListener);

    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPortNumber() {
        Scanner sysIn = new Scanner(System.in);
        do {
            System.out.println("========= Welcome to ForcaU =========");
            System.out.print("Host: ");
            host = sysIn.nextLine();
            if (host.equals("1")) {
                host = "localhost";
            }
            System.out.print("PortNumber: ");
            portNumber = sysIn.nextInt();
            if (portNumber == 1) {
                portNumber = 16969;
            }
        } while (portNumber == 0 && host == null);
    }

    public boolean isStarted(){
        return started;
    }
}