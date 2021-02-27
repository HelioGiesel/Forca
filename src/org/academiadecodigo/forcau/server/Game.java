package org.academiadecodigo.forcau.server;

import org.academiadecodigo.forcau.server.UserHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Game {

    private LinkedList<UserHandler> players;
    private boolean multiplayer;
    private boolean start;
    private List<String> map;
    private String word;
    private String[] wordChars;
    private String tips;
    private DataOutputStream write;
    private BufferedReader read;
    private String underscores;
    private final int maxTries = 5;
    private int charactersGuessed;
    private int tries;

    /**
     * Creates game and adds first player
     * Gametype 1: one player
     * Gametype 2: multiplayer
     *
     * @param firstPlayer
     * @param multiplayer
     */
    public Game(UserHandler firstPlayer, boolean multiplayer) {
        System.out.println("Entered Game");
        players = new LinkedList<>();
        players.add(firstPlayer);
        this.multiplayer = multiplayer;
        write = firstPlayer.getWrite();
        read = firstPlayer.getRead();
        map = new ArrayList<>();
        start();
    }

    public void start() {
        UserHandler p1 = players.get(0);
        readfile();
        p1.systemMessage("The word has " + randomWord() + " characters.");
        p1.systemMessage(underscores);

        //First try
        p1.systemMessage(p1.getName() + " pick a character.");
        try {
            String charGuessed = p1.getRead().readLine();
            p1.systemMessage(p1.getName() + " guessed " + charGuessed + ".");
            if(word.contains(charGuessed)){
                for (int i = 0; i < wordChars.length; i++) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public boolean join(UserHandler newPlayer) {
        if (start) return false;
        players.add(newPlayer);
        return true;
    }

    private void readfile() {

        try {

            FileReader fileReader = new FileReader("resources/caseironabo.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String dica = "";
                String[] split = line.split(" ");

                for (int i = 1; i < split.length; i++) {
                    if (i == (split.length - 1)) {
                        dica += split[i] + ".";
                        break;
                    }
                    dica += line.split(" ")[i] + " ";
                }

                map.add(line.split(" ")[0]);
                map.add(dica);
            }


        } catch (IOException ioException) {
            System.out.println("I hate exceptions");
        }

    }

    private int randomWord(){

        int randomNumber = (int) (Math.random() * map.size());
        randomNumber = ((randomNumber % 2) == 0) ? randomNumber : randomNumber - 1;
        word = map.get(randomNumber);
        wordChars = word.split("");
        tips = map.get(randomNumber + 1);

        underscores();
        return word.length();


    }

    private void underscores(){
        underscores = "";
        for (int i = 0; i < word.length(); i++) {
            underscores += "_ ";
        }
    }




}