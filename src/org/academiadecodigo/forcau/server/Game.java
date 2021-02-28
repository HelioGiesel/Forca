package org.academiadecodigo.forcau.server;

import org.academiadecodigo.forcau.Color;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
    private final int maxTries = 10;
    private int charactersGuessed = 0;
    private int tries = 0;
    private String charactersNotGuessed = "";
    private HashSet<String> usedCharacters;

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
        usedCharacters = new HashSet<>();
        start();
    }

    public void start() {
        UserHandler p1 = players.get(0);
        readfile();
        p1.systemMessage(Color.PURPLE_BOLD + "The word has " + randomWord() + " characters." + Color.RESET);
        p1.systemMessage(Color.PURPLE_BOLD + underscores + Color.RESET);

        //First try
        while (charactersGuessed < word.length() && tries < maxTries) {
            try {
                String charGuessed = "";

                if (charactersNotGuessed.equals("")) {
                    p1.systemMessage(Color.CYAN + p1.getName() + " pick a character" + Color.RESET);
                } else {
                    p1.systemMessage(Color.CYAN + p1.getName() + " pick a character. Already tried: " + charactersNotGuessed + Color.RESET);
                }

                charGuessed = p1.getRead().readLine();
                p1.systemMessage(Color.YELLOW + p1.getName() + " tried " + charGuessed + "." + Color.RESET);

                if (charGuessed.equals(word)) {
                    p1.systemMessage("\n" + Color.GREEN_BOLD + players.get(0).getName() + " you have won bro." + Color.RESET);
                    break;
                }

                if (!usedCharacters.add(charGuessed)) {
                    p1.systemMessage(Color.RED + p1.getName() + " that character has already been tried bro." + Color.RESET);
                    continue;
                }
                checkChar(charGuessed);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (charactersGuessed == word.length()) {
            players.get(0).systemMessage("\n" + Color.GREEN_BOLD + players.get(0).getName() + " you have won bro." + Color.RESET);
        } else if (tries == maxTries) {
            players.get(0).systemMessage("\n" + Color.RED_BOLD + players.get(0).getName() + " you have failed bro! iei" + Color.RESET);
            p1.systemMessage(Color.RED_BOLD + "The word was: " + word + Color.RESET);
        }

        restart();
    }

    public void restart() {
        for (int i = players.size(); i >= 0; i--) {
            players.remove(players.get(i));
        }
        tries = 0;
        charactersGuessed = 0;
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
        if (start) return false;
        players.add(newPlayer);
        return true;
    }

    /**
     * read word file
     */
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

}