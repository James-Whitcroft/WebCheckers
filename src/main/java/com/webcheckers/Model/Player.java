package com.webcheckers.Model;

import java.util.Objects;

public class Player {

    private String name;
    private boolean inGame;
    private int gamesPlayed;
    private int gamesWon;

    /**
     * Create a new Player
     * @param name
     *      String representing this player's name
     */
    public Player(String name) {
        this.name = name;
        inGame = false;
        gamesPlayed = 0;
        gamesWon = 0;
    }

    /**
     * Get a player's name
     * @return
     *      String representing this player's name
     */
    public String getName() {
        return name;
    }

    /**
     * checks whether the player is in a game
     * @return boolean true if in game
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * increment the number of games this player has won
     */
    public synchronized void winGame() {
        gamesWon += 1;
    }

    /**
     * sets the in game status of this player
     * also increases the games played count by one
     * when the inGame is set to false
     * @param inGame boolean true if in game
     */
    public synchronized void setInGame(boolean inGame) {
        gamesPlayed = !inGame ? gamesPlayed + 1 : gamesPlayed;
        this.inGame = inGame;
    }


    /**
     * returns the number of games this player has played
     * @return int
     */
    public synchronized int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * return the number of games this player has won
     * @return int
     */
    public synchronized int getGamesWon() {
        return gamesWon;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Player)) {
            return false;
        }
        Player that = (Player) obj;
        return this.name.toLowerCase().equals(that.name.toLowerCase());
    }

    /**
     *
     * @return
     *      unique hash for a player of type int
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
