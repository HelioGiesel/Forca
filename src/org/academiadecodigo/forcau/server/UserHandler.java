package org.academiadecodigo.forcau.server;

import org.academiadecodigo.forcau.Color;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class UserHandler implements Runnable {

    //Fields
    private LinkedList<UserHandler> list;
    private Socket serverSocket;
    private Scanner userName;
    private String currentColor = Color.BLUE;
    private String lineRead = "";
    private String name;
    private DataOutputStream write;
    private BufferedReader read;
    private boolean isUserNameSet;
    private Game game;


    //Constructor
    public UserHandler(Socket serverSocket, LinkedList<UserHandler> list, Game game) {
        this.list = list;
        this.serverSocket = serverSocket;
        this.game = game;
        try {
            userName = new Scanner(serverSocket.getInputStream());
            list.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return userName
     */
    public String getName() {
        return name;
    }

    /**
     * @return this outputStream
     */
    private OutputStream getOutputStream() {
        try {
            return serverSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DataOutputStream getWrite() {
        return write;
    }

    public BufferedReader getRead() {
        return read;
    }

    public Socket getServerSocket() {
        return serverSocket;
    }

    /**
     * returns connection state
     *
     * @return
     */
    private boolean isClosed() {
        return serverSocket.isClosed();
    }

    /**
     * iterates all users and sends message
     *
     * @param message
     */
    public void broadCast(String message) {
        for (UserHandler user : list) {
            if (user != this) {
                String serverMsg = (this.getName() + " Broadcasts: " + message);
                user.dispatchMessage(serverMsg);
            }
        }
    }

    public void systemMessage(String message) {
        for (UserHandler user : list) {
            user.dispatchMessage(message);
        }

    }

    /**
     * general message sender
     *
     * @param message
     */
    public void dispatchMessage(String message) {
        try {
            if (write != null) {
                write.writeBytes(message + "\n");
                write.flush();
            }
        } catch (IOException e) {
        }
    }

    /**
     * sets userName
     */
    private void setUserName() {
        try {
            if (!isUserNameSet) {
                isUserNameSet = true;
                name = userName.nextLine();
                Thread.currentThread().setName(name);
                write.writeBytes(Color.GREEN_BOLD + "welcome " + Thread.currentThread().getName() + "\n" + Color.RESET);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lists current users;
     */
    private void printOnlineUsers() { // NOT REMOVING OFFLINE USERS ***********************
        try {
            for (UserHandler user : list) {
                if (user.isClosed()) {
                    list.remove(user);
                }
                if (!(user.isClosed())) {
                    write.writeBytes(user.getName() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * lists all commands and chat functions
     */
    private void chatCommands() {
        switch (lineRead.split(" ")[0]) {
            case "/help":
                help();
                break;
            case "/participants":
                printOnlineUsers();
                break;
            case "/start":
                start();
                break;
            case "/join":
                join();
                break;
        }
    }

    /**
     * game start with options
     */
    public void start() {

        chooseWordsList();
        try {
            write.writeBytes(Color.RED_BOLD + "1. Solo game \n" + "2. Multiplayer game \n" + Color.RESET);
            write.flush();
            String input = "lol";
            while (!input.equals("1") && !input.equals("2")) {
                input = read.readLine();
                switch (input) {
                    case "1":
                        startSoloGame();
                        break;
                    case "2":
                        startMultiGame();
                        break;
                    default:
                        write.writeBytes(Color.RED_BOLD + "Please select option 1 or 2.\n" + Color.RESET);
                        write.flush();
                        break;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void chooseWordsList() {
        try {
            write.writeBytes(Color.RED_BOLD + "1. Fabio Mode \n" + "2. Academy Mode \n" + Color.RESET);
            write.flush();
            String input = null;
            while (input == null || !input.equals("1") && !input.equals("2")) {
                input = read.readLine();
                switch (input) {
                    case "1":
                        Game.setListPath("resources/foulWordsList.txt");
                        break;
                    case "2":
                        Game.setListPath("resources/ac_mode.txt");
                        break;
                    default:
                        write.writeBytes(Color.RED_BOLD + "Please select option 1 or 2.\n" + Color.RESET);
                        write.flush();
                        break;
                }

            }
        } catch (IOException error) {
            error.printStackTrace();
        }

    }

    private void startSoloGame() {
        broadCast("Soloplayer game started by " + this.name + ".");
        game.start(this, false);
    }

    private void startMultiGame() {
        broadCast("Multiplayer Game started by " + this.name + ". Type /join to join. You have 20 seconds.");
        game.start(this, true);
    }

    /**
     * user join game
     */
    private void join() {

        if (game != null) {
            if (game.join(this)) {
                systemMessage(this.name + " joined game. " + Game.players.size() + " players ready to start");
                try {
                    Thread.sleep(5000);

                    while (game.start) {
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("SAIO DO WHILE");
                return;
            }
            dispatchMessage("Game already started. Please wait!");
        } else {
            systemMessage("You have to start a game first");
        }
    }

    /**
     * show help commands
     */
    public void help() {
        try {
            write.writeBytes("/help for help \n" +
                    "/participants to see online users \n" +
                    "/start to start a game\n" +
                    "/join to join an active game\n" +
                    "/quit to quit \n");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (serverSocket.isBound()) {
            beginServer();
        }
    }

    public void beginServer() {


        try {
            read = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            write = new DataOutputStream(serverSocket.getOutputStream());

            write.writeBytes(Color.GREEN + "Connection established \n" + Color.RESET);
            write.writeBytes(Color.GREEN + "/help to see available commands \n" + Color.RESET);
            write.writeBytes("Please setup your username \n");
            setUserName();
            startMenu();
            logicMenu();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMenu() {
        try {

            write.writeBytes(Color.GREEN_BOLD + "Please enter your message \n" + Color.RESET);


        } catch (IOException io) {
            System.out.println("Caseiro yo yo");
        }

    }

    public void logicMenu() {

        try {
            while (serverSocket.isBound()) {
                lineRead = read.readLine();
                if (lineRead == null) {
                    list.remove(this);
                    Game.players.remove(this);
                    serverSocket.close();
                    read.close();
                    write.close();
                    read = null;
                    write = null;
                    return;
                }
                chatCommands();
            }
            read.close();
            write.close();
            serverSocket.close();
        } catch (IOException e) {
        }
    }
}