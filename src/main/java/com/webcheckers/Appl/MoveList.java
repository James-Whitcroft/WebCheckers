package com.webcheckers.Appl;
import com.webcheckers.Model.Move;
import java.util.ArrayList;

/**
 * MoveList contains an ordered list of moves
 * made in a game of WebCheckers. The moves are
 * stored in the order they are made and can
 * be retrieved in order to replay the game.
 */
public class MoveList {

    private static int ID = 0;

    private int GameID;
    private ArrayList<Move> moveList;
    private int index;
    private String redPlayerName;
    private String whitePlayerName;

    /**
     * Initializes the storage for moves
     */
    public MoveList(String redPlayerName, String whitePlayerName){
        GameID = ID;
        ID++;
        moveList = new ArrayList<>();
        index = 0;
        this.whitePlayerName = whitePlayerName;
        this.redPlayerName = redPlayerName;
    }

    public ArrayList<Move> getMoveList(){return moveList;}

    /**
     * Adds a move to the end of the move list.
     * @param move the move to add to the list
     */
    public void addMove(Move move){
        moveList.add(move);
    }

    /**
     * Retrieves the move at the index that it was added
     * to from the game. Ideally used for the replay function.
     * @param moveNumber The index of the move to get
     * @return
     */
    public Move getMove(int moveNumber){
        return moveList.get(moveNumber);
    }

    public boolean hasNext(){
        return moveList.size() != index;
    }

    public boolean hasPrevious(){
        return index > 0;
    }

    public int getIndex(){return index;}

    public boolean forward(){
        if(index < moveList.size()){
            index += 1;
            return true;
        }
        return false;
    }

    public boolean backward(){
        if(index > 0){
            index -= 1;
            return true;
        }
        return false;
    }

    public void resetReplay(){
        index = 0;
    }

    public Integer getGameID() {
        return GameID;
    }

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }
}
