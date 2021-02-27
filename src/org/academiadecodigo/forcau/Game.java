package org.academiadecodigo.forcau;

import org.academiadecodigo.forcau.server.UserHandler;

import java.util.LinkedList;

public class Game{

    private LinkedList<UserHandler> players;
    private int gameType;
    private boolean start;

    /**
     * Creates game and adds first player
     * Gametype 1: one player
     * Gametype 2: multiplayer
     * @param firstPlayer
     * @param gameType
     */
    public Game(UserHandler firstPlayer, int gameType) {
        players.add(firstPlayer);
        this.gameType = gameType;
    }

    public boolean join(UserHandler newPlayer) {
        if (start) return false;
        players.add(newPlayer);
        return true;
    }
}