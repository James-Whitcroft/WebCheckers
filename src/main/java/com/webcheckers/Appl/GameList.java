package com.webcheckers.Appl;

import com.webcheckers.Model.BoardModel;
import com.webcheckers.ui.BoardView;
import com.webcheckers.Model.Player;

import java.util.HashMap;

/**
 * Keeps track of all ongoing games on the server and allows easy access to the games
 */
public class GameList {
    
    //region Attributes

    /**
     * Keeps track of all the ongoing games
     */
    private HashMap<String, BoardModel> gameList;

    //endregion

    //region Constructor

    public GameList() {
        gameList = new HashMap<>();
    }

    //endregion

    //region Private Methods

    private synchronized BoardModel getModel(Player player) {
        if (gameExists(player.getName())) {
            return gameList.get(player.getName());
        }
        return null;
    }


    //endregion

    //region Public Methods
    /**
     * Adds a game to the server side list of games
     * @param model the game to be added
     * @return true if the game is successfully added
     */
    public synchronized boolean addGame(BoardModel model) {
        String whiteName = model.getWhitePlayer().getName();
        String redName = model.getRedPlayer().getName();


        if (!(gameExists(whiteName) && gameExists(redName))) {
            gameList.put(model.getWhitePlayer().getName(), model);
            gameList.put(model.getRedPlayer().getName(), model);
            return true;
        }
        return false;
    }

    public synchronized boolean gameExists(String playerName) {
        return gameList.get(playerName) != null;
    }

    /**
     * @param player the player whose boardModel is to be returned
     * @return the boardModel corresponding to the given player
     */
    public synchronized BoardModel getBoardModel(Player player) {
        return getModel(player);
    }

    /**
     * @param player the player who is requesting the BoardView
     * @return the boardView of the model corresponding to the given player
     */
    public synchronized BoardView getBoardView(Player player) {
        BoardModel model = getModel(player);
        return new BoardView(player, model);
    }

    public synchronized void removeBoard(Player player) {
        gameList.remove(player.getName().toLowerCase());
    }

    //endregion

}
