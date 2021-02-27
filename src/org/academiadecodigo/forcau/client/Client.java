package org.academiadecodigo.forcau.client;

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

    //Constructor
    public Client() throws IOException {
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

        ClientListener serverListener = new ClientListener(socket);
        ClientWriter serverWriter = new ClientWriter(socket);

        fixedPool.submit(serverWriter);
        fixedPool.submit(serverListener);

    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
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
}
