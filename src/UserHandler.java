import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class UserHandler implements Runnable {

    private final LinkedList<UserHandler> list;
    private final Socket serverSocket;
    private DataOutputStream write;
    private final Scanner userName;
    private String lineRead = "";
    private boolean isUserNameSet;
    private String name;
    private String currentColor = BLUE;
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String RESET = "\033[0m";  // Text Reset

    public UserHandler(Socket serverSocket, LinkedList<UserHandler> list) throws IOException {
        this.list = list;
        this.serverSocket = serverSocket;
        userName = new Scanner(serverSocket.getInputStream());
        list.add(this);
    }

    //returns userName;
    private String getName() {
        return name;
    }

    //returns this outputStream;
    private OutputStream getOutputStream() throws IOException {
        return serverSocket.getOutputStream();
    }

    //returns connection state;
    private boolean isClosed() {
        return serverSocket.isClosed();
    }

    //iterates all users and sends message;
    public void broadCast(String message) {
        for (UserHandler user : list) {
            if (user != this) {
                String serverMsg = (currentColor + this.getName() + " Broadcasts: " + removeCommandTag(message));
                user.dispatchMessage(serverMsg);
            }
        }
    }

    //general message sender;
    private void dispatchMessage(String message) {
        try {
            write.writeBytes(message + "\n");
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sets userName;
    private void setUserName() throws IOException {
        if (!isUserNameSet) {
            isUserNameSet = true;
            name = userName.nextLine();
            Thread.currentThread().setName(name);
            write.writeBytes("welcome " + Thread.currentThread().getName() + "\n");
        }
    }

    //lists current users; NOT REMOVING OFFLINE USERS;
    private void printOnlineUsers() throws IOException {
        for (UserHandler user : list) {
            if(user.isClosed()){
                list.remove(user);
            }
            if (!(user.isClosed())) {
                write.writeBytes(user.getName() + "\n");
            }
        }
    }

    //receives string and change user's text color;
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

    //receives "raw" message and removed the user's command tag;
    private String removeCommandTag(String message) {
        String[] newMsg = message.split(" ");
        String finalMsg = "";
        for (int i = 1; i < newMsg.length; i++) {
            finalMsg = finalMsg.concat(newMsg[i] + " ");
        }
        System.out.println(finalMsg);
        return finalMsg;
    }

    //lists all commands and chat functions;
    private void chatCommands() throws IOException {
        if (lineRead.contains("/help")) {
            write.writeBytes("/help for help \n" +
                    "/setColor <color> for colors - Available Colors: \n" +
                    "BLACK -- RED -- GREEN -- YELLOW -- BLUE -- PURPLE -- CYAN -- WHITE\n" +
                    "/participants to see online users \n" +
                    "/pm/<name> for private message \n" +
                    "/broadCast to enter broadcast mode\n" +
                    "/quit to quit \n");
        } else if (lineRead.contains("/setColor")) {
            setColor(removeCommandTag(lineRead));
        } else if (lineRead.contains("/participants")) {
            printOnlineUsers();
        } else if (lineRead.contains("/pm")) {
            privateMessage(lineRead);
        } else if (lineRead.contains("/broadCast")) {
            broadCast(lineRead);
        }
    }

    //identifies user by userName and sends given message;
    private void privateMessage(String message) throws IOException {
        for (UserHandler user : list) {
            if (message.contains(user.getName())) {
                DataOutputStream privateWrite = new DataOutputStream(user.getOutputStream());
                privateWrite.writeBytes(currentColor + this.getName() + " PM: " + removeCommandTag(message) + "\n");
            }
        }
    }

    @Override
    public void run() {

        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            write = new DataOutputStream(serverSocket.getOutputStream());

            write.writeBytes(GREEN + "Connection established \n" + RESET);
            write.writeBytes(GREEN + "/help to see available commands \n" + RESET);
            write.writeBytes("Please setup your username \n");
            setUserName();
            write.writeBytes("Please enter your message \n");

            while (serverSocket.isBound()) {
                lineRead = read.readLine();
                System.out.println(lineRead);
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
