package com.webcheckers.Appl;

import java.util.Collection;
import java.util.HashMap;

public class SavedGameList {


    /**
     * The HashMap storage for all saved games on the server.
     * Player names are keys to a list of games that player has played.
     */
    private HashMap<Integer, MoveList> savedGames;

    /**
     * Initializes the HashMap that will store
     * all games played on the server as values with
     * the player's name as a key
     */
    public SavedGameList(){
        savedGames = new HashMap<>();
    }

    /**
     * Adds a game that just finished to the list of
     * a player's saved games.
     * @param movelist the game they just finished
     */
    public void addGameToSavedGames(MoveList movelist){
        savedGames.remove(movelist.getGameID());
        savedGames.put(movelist.getGameID(), movelist);
    }

    /**
     * Retrieves a player's saved games
     * @param gameId the player who played the games
     * @return
     */
    public MoveList getSavedGameList(Integer gameId){
        return savedGames.get(gameId);
    }

    /**
     * Get a list of all available MoveLists
     * @return a collection of MoveList objects
     */
    public Collection<MoveList> getAllSavedGames() {
        return savedGames.values();
    }

    public void resetReplay(Integer gameId) {
        savedGames.get(gameId).resetReplay();
    }

}
