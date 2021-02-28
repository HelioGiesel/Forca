package org.academiadecodigo.forcau.server;

import org.academiadecodigo.forcau.Color;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class UserHandler implements Runnable {
    //Fields
    private final LinkedList<UserHandler> list;
    private Vector<Game> vector;
    private final Socket serverSocket;
    private Scanner userName;
    private String currentColor = Color.BLUE;
    private String lineRead = "";
    private String name;
    private DataOutputStream write;
    private BufferedReader read;
    private boolean isUserNameSet;
    Game game;


    //Constructor
    public UserHandler(Socket serverSocket, LinkedList<UserHandler> list) {
        this.list = list;
        this.serverSocket = serverSocket;
        vector = new Vector<>();
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
        game = new Game(this, false);
        vector.add(game);

    }

    private void startMultiGame() {
        broadCast("Multiplayer Game started by " + this.name + ". Type /join to join. You have 20 seconds.");
        game = new Game(this, true);
        vector.add(game);

    }

    /**
     * user join game
     */
    private void join() {
        System.out.println("teste");
        if (vector.get(0) != null) {
            if (vector.get(0).join(this)) {
                broadCast(this.name + " joined game.");
                return;
            }
            dispatchMessage("Game already started. Please wait!");
        } else {
            systemMessage("You have to start a game first");
        }
    }

    /**
     * identifies user by userName and sends given message
     *
     * @param message
     */
    private void privateMessage(String message) {
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
    public void help() {
        try {
            write.writeBytes("/help for help \n" +
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
    public void run() {

        beginServer();
        startMenu();

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


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startMenu(){
        try {

            if(vector != null){
                vector.clear();
            }

            write.writeBytes(Color.GREEN_BOLD + "Please enter your message \n" + Color.RESET);

            while (serverSocket.isBound()) {
                lineRead = read.readLine();
                //System.out.println(lineRead);
                chatCommands();
            }
            read.close();
            write.close();
            serverSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}