package org.academiadecodigo.forcau.server;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class UserHandler implements Runnable {
    //Fields
    private final LinkedList<UserHandler> list;
    private final Socket serverSocket;
    private DataOutputStream write;
    private BufferedReader read;
    private Scanner userName;
    private String lineRead = "";
    private boolean isUserNameSet;
    private String name;
    private String currentColor = BLUE;
    Game game;
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String RESET = "\033[0m";  // Text Reset

    //Constructor
    public UserHandler(Socket serverSocket, LinkedList<UserHandler> list) {
        this.list = list;
        this.serverSocket = serverSocket;
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

    public void setWrite(DataOutputStream write) {
        this.write = write;
    }

    public BufferedReader getRead() {
        return read;
    }

    public void setRead(BufferedReader read) {
        this.read = read;
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
                String serverMsg = (currentColor + this.getName() + " Broadcasts: " + removeCommandTag(message));
                user.dispatchMessage(serverMsg);
            }
        }
    }

    public void systemMessage(String message){
        for (UserHandler user : list) {
            user.dispatchMessage(message);
        }

    }

    /**
     * general message sender
     *
     * @param message
     */
    private void dispatchMessage(String message) {
        try {
            write.writeBytes(message + "\n");
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
                write.writeBytes("welcome " + Thread.currentThread().getName() + "\n");
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
     * receives string and change user's text color
     *
     * @param color
     */
    private void setColor(String color) {
        switch (color) {
            case "BLACK " -> currentColor = BLACK;
            case "RED " -> currentColor = RED;
            case "GREEN " -> currentColor = GREEN;
            case "YELLOW " -> currentColor = YELLOW;
            case "BLUE " -> currentColor = BLUE;
            case "PURPLE " -> currentColor = PURPLE;
            case "CYAN " -> currentColor = CYAN;
            case "WHITE " -> currentColor = WHITE;
        }
    }

    /**
     * receives "raw" message and removed the user's command tag
     *
     * @param message
     * @return
     */
    private String removeCommandTag(String message) {
        String[] newMsg = message.split(" ");
        String finalMsg = "";
        for (int i = 1; i < newMsg.length; i++) {
            finalMsg = finalMsg.concat(newMsg[i] + " ");
        }
        System.out.println(finalMsg);
        return finalMsg;
    }

    /**
     * lists all commands and chat functions
     */
    private void chatCommands() {
            switch (lineRead.split(" ")[0]) {
                case "/help":
                    help();
                    break;
                case "/setColor":
                    setColor(removeCommandTag(lineRead));
                    break;
                case "/participants":
                    printOnlineUsers();
                    break;
                case "/broadcast":
                    broadCast(lineRead);
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
    private void start() {
        try {
            write.writeBytes("1. Solo game \n" + "2. Multiplayer game \n");
            write.flush();
            String input = "lol";
            while(!input.equals("1") && !input.equals("2")) {
                input = read.readLine();
                switch (input) {
                    case "1":
                        startSoloGame();
//                        broadCast();
                        break;
                    case "2":
                        startMultiGame();
                        break;
                    default:
                        write.writeBytes("Please select option 1 or 2.\n");
                        write.flush();
                        break;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void startSoloGame() {
        broadCast("Soloplayer game started by " + this.name + ".");
        game = new Game(this,false);

    }

    private void startMultiGame() {
        broadCast("Multiplayer Game started by " + this.name + ". Type /join to join. You have 20 seconds.");
        game = new Game(this, true);
    }

    /**
     * user join game
     */
    private void join() {
        if (game.join(this)) {
            broadCast(this.name + " joined game.");
            return;
        }
        dispatchMessage("Game already started. Please wait!");
    }

    /**
         * identifies user by userName and sends given message
         * @param message
         */
        private void privateMessage (String message){
            try {
                for (UserHandler user : list) {
                    if (message.contains(user.getName())) {
                        DataOutputStream privateWrite = new DataOutputStream(user.getOutputStream());
                        privateWrite.writeBytes(currentColor + this.getName() + " PM: " + removeCommandTag(message) + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * show help commands
     */
        public void help () {
            try {
                write.writeBytes("/help for help \n" +
                        "/setColor <color> for colors - Available Colors: \n" +
                        "BLACK -- RED -- GREEN -- YELLOW -- BLUE -- PURPLE -- CYAN -- WHITE\n" +
                        "/participants to see online users \n" +
                        "/pm/<name> for private message \n" +
                        "/broadcast to enter broadcast mode\n" +
                        "/start to start a game\n" +
                        "/join to join an active game\n" +
                        "/quit to quit \n");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


        @Override
        public void run () {

            try {
                read = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                write = new DataOutputStream(serverSocket.getOutputStream());

                write.writeBytes(GREEN + "Connection established \n" + RESET);
                write.writeBytes(GREEN + "/help to see available commands \n" + RESET);
                write.writeBytes("Please setup your username \n");
                setUserName();
                write.writeBytes("Please enter your message \n");

                while (serverSocket.isBound()) {
                    lineRead = read.readLine();
                    //System.out.println(lineRead);
                    chatCommands();
                }
                read.close();
                write.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }