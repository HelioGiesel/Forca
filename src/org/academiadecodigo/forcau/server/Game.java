package org.academiadecodigo.forcau.server;

import org.academiadecodigo.forcau.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {

    private Vector<UserHandler> players;
    private HashSet<String> usedCharacters;
    private List<String> map;
    private String[] wordChars;
    private String word;
    private String tips;
    private String underscores;
    private String charactersNotGuessed = "";
    private String charGuessed;
    private final int maxTries = 10;
    private int tries = 0;
    private int charactersGuessed = 0;
    public boolean start;
    private boolean multiplayer;
    private UserHandler p1;
    private static String listPath;

    /**
     * Creates game and adds first player
     * Gametype 1: one player
     * Gametype 2: multiplayer
     *
     * @param
     * @param
     */
    public Game() {

        map = new ArrayList<>();
        usedCharacters = new HashSet<>();
        start = false;

    }

    //Setters
    public static void setListPath(String newPath) {
        listPath = newPath;
    }

    //Custom Methods
    public void start(UserHandler firstPlayer, boolean multiplayer) {

        players = new Vector<>();
        players.add(firstPlayer);
        this.multiplayer = multiplayer;
        p1 = players.get(0);

        if (!multiplayer) {
            soloGame();
        } else{
            multiplayerGame();
        }

    }

    private void soloGame(){
        readfile();
        p1.systemMessage(Color.PURPLE_BOLD + "The word has " + randomWord() + " characters." + Color.RESET);
        p1.systemMessage(Color.PURPLE_BOLD + underscores + Color.RESET);

        gameLogic();
        gameFinal();
        restart();
    }

    private void multiplayerGame(){

        int countDown = 5;

        while (countDown >= 0) {
            try {
                if (countDown < 10) {
                    p1.dispatchMessage(Color.RED + "Players have: " + countDown + " seconds to join" + Color.RESET);
                    Thread.sleep(1000);
                    countDown--;
                } else {
                    p1.dispatchMessage(Color.GREEN + "Players have: " + countDown + " seconds to join" + Color.RESET);
                    Thread.sleep(5000);
                    countDown = countDown - 5;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (players.size() == 1){
            p1.dispatchMessage(Color.RED_BOLD + "\nNobody entered your game bro\nYou will return to initial menu."  + Color.RESET);
            p1.broadCast(Color.RED_BOLD + "Game didn't had enough players to start." + Color.RESET);
            restart();
            return;
        }else {
            p1.systemMessage(Color.GREEN_BOLD + "Game started BRO" + Color.RESET+ "\n");
        }

        readfile();
        start = true;
        p1.systemMessage(Color.PURPLE_BOLD + "The word has " + randomWord() + " characters." + Color.RESET);
        p1.systemMessage(Color.PURPLE_BOLD + underscores + Color.RESET);

        gameLogic();
        gameFinal();
        restart();
    }

    public void restart() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = players.size(); i > 0; i--) {

            players.get(i-1).dispatchMessage("\n");
            players.get(i-1).startMenu();


        }
    }

    public void checkChar(String charGuessed) {

        if (word.contains(charGuessed)) {
            boolean[] hiddenLetters = new boolean[word.length()];

            for (int i = 0; i < wordChars.length; i++) {
                if (wordChars[i].equals(charGuessed)) {
                    hiddenLetters[i] = true;
                    charactersGuessed++;
                }
            }

            displayLetter(hiddenLetters, charGuessed);
            players.get(0).systemMessage(Color.GREEN + "Character " + charGuessed + " found!\n" + underscores + Color.RESET);

        } else {
            players.get(0).systemMessage(Color.RED + charGuessed + " is not on the word bro :(" + Color.RESET);
            charactersNotGuessed += charGuessed + ", ";
            tries++;
        }

    }

    public boolean join(UserHandler newPlayer) {

        if (start){
            return false;
        }

        players.add(newPlayer);
        return true;

    }

    /**
     * read word file
     */
    private void readfile() {

        try {

            FileReader fileReader = new FileReader(listPath);
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

    /**
     * Pick a random word and generate underscore string
     *
     * @return
     */
    private int randomWord() {

        int randomNumber = (int) (Math.random() * map.size());
        randomNumber = ((randomNumber % 2) == 0) ? randomNumber : randomNumber - 1;
        word = map.get(randomNumber);
        wordChars = word.split("");

        tips = map.get(randomNumber + 1);

        underscores();
        return word.length();

    }

    private void underscores() {
        underscores = "";

        for (int i = 0; i < word.length(); i++) {

            if (i == word.length() - 1) {
                underscores += "_";
                return;
            }
            underscores += "_ ";
        }
    }

    private void displayLetter(boolean[] hiddenLetters, String charGuessed) {

        char[] underToChar = underscores.toCharArray();

        for (int i = 0; i < hiddenLetters.length; i++) {
            if (hiddenLetters[i]) {
                underToChar[i * 2] = charGuessed.charAt(0);
            }
        }
        underscores = String.valueOf(underToChar);
    }

    private boolean correctAnswer(){

        if (charGuessed.equals(word)) {
            p1.systemMessage("\n" + Color.GREEN_BOLD + players.get(0).getName() + " you have won bro." + Color.RESET);
            restart();
            start = false;
            return true;
        }
        return false;
    }

    private boolean alreadyTried(){

        if (!usedCharacters.add(charGuessed)) {

            p1.systemMessage(Color.RED + p1.getName() + " that character has already been tried bro." + Color.RESET);
            return true;

        }
        return false;
    }

    private void gameFinal(){

        if (charactersGuessed == word.length()) {
            players.get(0).systemMessage("\n" + Color.GREEN_BOLD + players.get(0).getName() + " you have won bro." + Color.RESET);
        } else if (tries == maxTries) {
            players.get(0).systemMessage("\n" + Color.RED_BOLD + players.get(0).getName() + " you have failed bro! iei" + Color.RESET);
            p1.systemMessage(Color.RED_BOLD + "The word was: " + word + Color.RESET);
        }

        resetProperties();


    }

    private void resetProperties(){
        tries = 0;
        charactersGuessed = 0;
        charactersNotGuessed = "";
        charGuessed = "";
        start = false;
        map.clear();
        usedCharacters.clear();
        underscores = "";

    }

    private void gameLogic(){
        int counter = 0;

        while (charactersGuessed < word.length() && tries < maxTries) {
            try {
                charGuessed = "";

                if (charactersNotGuessed.equals("")) {
                    p1.systemMessage(Color.CYAN + players.get(counter).getName() + " pick a character" + Color.RESET);
                } else {
                    p1.systemMessage(Color.CYAN + players.get(counter).getName() + " pick a character. Already tried: " + charactersNotGuessed + Color.RESET);
                }
                charGuessed = players.get(counter).getRead().readLine();
                p1.systemMessage(Color.YELLOW + players.get(counter).getName() + " tried " + charGuessed + "." + Color.RESET);

                if (correctAnswer()) {
                    break;
                }

                if (alreadyTried()) {

                    if (counter < players.size() - 1){
                        counter++;

                    } else {
                        counter = 0;
                    }

                    continue;
                }

                checkChar(charGuessed);

                if (counter < players.size() - 1){
                    counter++;
                } else {
                    counter = 0;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}