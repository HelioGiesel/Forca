package org.academiadecodigo.forcau.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener implements Runnable {

    private Socket socket;
    private BufferedReader read;
    private String lineRead = "";

    public ClientListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (!socket.isClosed()) {
            try {
                lineRead = read.readLine();
                if (lineRead != null) {

                    System.out.println(lineRead);

                } else {

                    System.out.println("Connection closed, exiting...");
                    read.close();
                    socket.close();
                }
            }catch (IOException e) {
            }
        }

    }

}
