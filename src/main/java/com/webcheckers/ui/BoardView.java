package com.webcheckers.ui;

import com.webcheckers.Model.*;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardView {

    //region Attributes

    private ArrayList<Row> rows;
    private Player redPlayer;
    private Player whitePlayer;
    private Piece.color activeColor;

    //endregion

    //region Constructor

    /**
     * Creates an iterable of iterables to be easily read by the freemarker
     * template from a 2D array of spaces in BoardModel
     * @param model the model to be represented by the BoardView
     */
    public BoardView(Player player, BoardModel model) {
        rows = new ArrayList<>();

        redPlayer = model.getRedPlayer();
        whitePlayer = model.getWhitePlayer();
        activeColor = model.getActive();

        Piece.color playerColor = getPlayerColor(player, model);

        if (playerColor == Piece.color.WHITE) {
            for (int r = 0; r < 8; r++) {
                // Converts the row from model into an iterable Row class
                rows.add(new Row(r, model.getRow(r)));
            }
        }
        if (playerColor == Piece.color.RED) {
            for (int r = 7; r >= 0; r--) {
                // Converts the row from model into an iterable Row class
                rows.add(new Row(r, model.getRowReversed(r)));
            }
        }

    }

    //endregion

    //region Private Methods

    /**
     * return the given player's color
     * @param player the player for whom youre querying
     * @param model the model the player uses
     * @return an enum RED or WHITE
     */
    private Piece.color getPlayerColor(Player player, BoardModel model) {
        if (model.getWhitePlayer() == player) {
            return Piece.color.WHITE;
        }
        if (model.getRedPlayer() == player) {
            return Piece.color.RED;
        }
        return null;
    }

    //endregion

    //region Public Methods

    /**
     * @return the iterable list of rows containing an iterable list of spaces
     */
    public synchronized Iterator<Row> iterator() {
        return rows.iterator();
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * get the active player's color
     * @return enum WHITE or RED
     */
    public Piece.color getActiveColor() {
        return activeColor;
    }

    /**
     *  get the active player
     * @return a Player object representing the active player
     */
    public Player getActivePlayer() {
        return activeColor == Piece.color.RED ? redPlayer : whitePlayer;
    }

    //endregion

}
