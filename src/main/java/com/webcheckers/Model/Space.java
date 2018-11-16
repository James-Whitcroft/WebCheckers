package com.webcheckers.Model;

import java.util.Objects;

public class Space {

    private int cellIdx;
    private boolean isLight; //true = light, false = dark
    private Piece myPiece;

    /*
        Space represents one square on a board of checkers.
        @param cellIdx the index (number) of the space in its row
        @param isLight the boolean signifying which color the space is
     */
    public Space(int cellIdx, boolean isLight) {
        this.cellIdx = cellIdx;
        this.myPiece = null;
        this.isLight = isLight;
    }

    public Space(int cellIdx, boolean isLight, Piece piece) {
        this.cellIdx = cellIdx;
        this.myPiece = piece;
        this.isLight = isLight;
    }

    /*
        returns a copy of the Space
     */
    public Space(Space copy) {
        this.cellIdx = copy.cellIdx;
        this.isLight = copy.isLight;
        if (copy.myPiece != null) {
            this.myPiece = new Piece(copy.getPiece());
        }
        else {
            myPiece = null;
        }
    }

    /*
        A space is valid if it does not already have a Piece on it,
        and if it is dark-colored.
     */
    public synchronized boolean isValid() {
        return !isLight && myPiece == null;
    }

    /*
        isValid MUST be called before this method!
        placePiece assigns a piece to a space as the player moves it.
        @param piece the checkers piece to be placed in this space.
     */
    public boolean placePiece(Piece piece) {
        if (piece != null) {
            myPiece = piece;
            return true;
        } else {
            return false;
        }
    }

    public void removePiece() {
        if (myPiece != null) {
            myPiece = null;
        }
    }

    public Piece getPiece() {
        return myPiece;
    }

    public int getCellIdx() {
        return cellIdx;
    }

    public boolean getCellColor(){ return isLight; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Space) {
            if (this.myPiece != null && ((Space) obj).myPiece != null) { // Both spaces have pieces
                return (this.myPiece.equals(((Space) obj).myPiece) &&
                        this.cellIdx == ((Space) obj).cellIdx &&
                        this.isLight == ((Space) obj).isLight);
            }
            else if (this.myPiece != null || ((Space) obj).myPiece != null) { // When one space has a piece
                return false;
            }
            else { // When no space has a piece
                return (this.cellIdx == ((Space) obj).cellIdx &&
                        this.isLight == ((Space) obj).isLight);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPiece(), cellIdx, isLight);
    }

    public String toString(){
        if(myPiece == null){
            return "Index: " + cellIdx + ", light space: " + isLight;
        }
        else{
            return "Index: " + cellIdx + ", light space: " + isLight + " occupied: " + true;
        }
    }
}
