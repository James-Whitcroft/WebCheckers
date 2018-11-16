package com.webcheckers.Model;

import java.util.Objects;

/**
 * The model representation of an individual checkers piece.
 * Holds the color and type of piece.
 * @author Lucas Szeto
 */
public class Piece {

    //region enum declarations

    public enum pieceType {
        SINGLE,
        KING
    }

    public enum color {
        RED,
        WHITE
    }

    //endregion

    //region Attributes

    private pieceType type;
    private color color;

    //endregion

    //region Constructors

    /**
     * Creates a new single checkers piece of the specified color
     * @param color the color of the piece to be created (Red or White)
     */
    public Piece(color color) {
        this.color = color;
        this.type = pieceType.SINGLE;
    }

    /**
     * Creates a new checkers piece of the specified color and type
     * @param color the color of the piece to be created (Red or White)
     * @param type the type of piece to be created (Single or King)
     */
    public Piece(color color, pieceType type) {
        this.color = color;
        this.type = type;
    }

    /**
     * Copy Constructor for piece
     * @param copy the Piece to be copied
     */
    public Piece(Piece copy) {
        this.color = copy.color;
        this.type = copy.type;
    }

    //endregion

    //region Public Methods

    /**
     * @return type of piece (single or king)
     */
    public pieceType getType() {
        return type;
    }

    /**
     * @return color of piece (red or white)
     */
    public color getColor() {
        return color;
    }

    /**
     * sets the type value of the piece to KING
     */
    public void setKing() {
        this.type = pieceType.KING;
    }

    public boolean checkIfKingMe(Piece piece, Move endMove){
        switch (piece.getColor()){
            case WHITE:
                if(endMove.getEnd().getRow() == 0){
                    piece.setKing();
                    return true;
                }
                break;
            case RED:
                if (endMove.getEnd().getRow() == 7){
                    piece.setKing();
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            return (this.type == ((Piece) obj).type &&
                    this.color == ((Piece) obj).color);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    //endregion

}
