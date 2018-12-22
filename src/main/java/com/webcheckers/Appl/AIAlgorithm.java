package com.webcheckers.Appl;

import com.webcheckers.Model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.MAX_VALUE;


//add a small subclass that stores a move and a strength to be able to associate the two together

public class AIAlgorithm {

    /**
     * Generate a possible list of moves for the Ai player to make.
     * @param model the BoardModel on which to generate a move for the AI
     * @param depth number of recursive calls to algorithm or -1 for RandomAI
     * @return AI move list.
     */
    public static List<Move> GenerateMove(BoardModel model, int depth) {
        if (depth < 0) {
            return RandomMove(model);
        }
        else {
            ArrayList<Move> moves = new ArrayList<>();
            //make a call to the recursive function
            //keep track of the moves that are the strongest
            //return moves;


            //TODO replace this with actual algorithm
            return RandomMove(model);
        }
    }

    /**
     * Returns a random valid move/moves
     * @param model the model on which to make a randomMove
     * @return random valid move
     */
    private static List<Move> RandomMove(BoardModel model) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = model.getSpace(new Position(r, c)).getPiece();
                if (piece == null || piece.getColor() == Piece.color.RED) {
                    continue;
                }
                possibleMoves.addAll(getAllMoves(model, new Position(r, c)));
            }
        }
        int i = (int)(Math.random() * possibleMoves.size());
        ArrayList<Move> selectedMove = new ArrayList<>();
        selectedMove.add(possibleMoves.get(i));
        return selectedMove;
    }

    /**
     * Gets another jump, or null if there are no more valid jumps
     * @param model the BoardModel on which to continue the jump
     * @param pos the position of the jumping piece
     * @return a valid jump continuation, or null if there are no other jumps
     */
    public static Move GetMultipleJump(BoardModel model, Position pos) {
        List<Move> jumps = getJumps(model, pos);
        int i = (int)(Math.random() * jumps.size());
        if (jumps.isEmpty()) {
            return null;
        }
        return jumps.get(i);
    }

    /**
     * Gets valid moves for a single piece
     * @param model BoardModel to check
     * @param pos position of piece to check
     * @return list of all valid moves of the piece
     */
    private static List<Move> getAllMoves(BoardModel model, Position pos) {
        ArrayList<Move> validMoves = new ArrayList<>();
        validMoves.addAll(getSimpleMoves(model, pos));
        validMoves.addAll(getJumps(model, pos));
        return validMoves;
    }

    /**
     * Gets all of the simple moves for a single piece
     * @param model BoardModel to check
     * @param pos position of piece to check
     * @return list of all valid simple moves
     */
    private static List<Move> getSimpleMoves(BoardModel model, Position pos) {
        ArrayList<Move> validMoves = new ArrayList<>();

        int forwardRow = pos.getRow() + 1;
        int rightCell = pos.getCell() + 1;
        int leftCell = pos.getCell() - 1;
        int backRow = pos.getRow() - 1;
        boolean leftValid = leftCell >= 0 && leftCell < 8;
        boolean rightValid = rightCell >= 0 && rightCell < 8;

        if (forwardRow >= 0 && forwardRow < 8) {
            if (rightValid) {
                Move forwardRight = new Move(pos, new Position(forwardRow, rightCell));
                if (MoveValidator.validateMove(model, forwardRight)) {
                    validMoves.add(forwardRight);
                }
            }
            if (leftValid) {
                Move forwardLeft = new Move(pos, new Position(forwardRow, leftCell));
                if (MoveValidator.validateMove(model, forwardLeft)) {
                    validMoves.add(forwardLeft);
                }
            }
        }
        if (backRow >= 0 && backRow < 8) {
            if (rightValid) {
                Move backRight = new Move(pos, new Position(backRow, rightCell));
                if (MoveValidator.validateMove(model, backRight)) {
                    validMoves.add(backRight);
                }
            }
            if (leftValid) {
                Move backLeft = new Move(pos, new Position(backRow, leftCell));
                if (MoveValidator.validateMove(model, backLeft)) {
                    validMoves.add(backLeft);
                }
            }
        }
        return validMoves;
    }

    /**
     * Gets all of the jumps for a single piece
     * @param model BoardModel to check
     * @param pos position of piece to check
     * @return list of all valid jumps
     */
    private static List<Move> getJumps(BoardModel model, Position pos) {
        ArrayList<Move> validJumps = new ArrayList<>();

        int forwardRow = pos.getRow() + 2;
        int rightCell = pos.getCell() + 2;
        int leftCell = pos.getCell() - 2;
        int backRow = pos.getRow() - 2;
        boolean leftValid = leftCell >= 0 && leftCell < 8;
        boolean rightValid = rightCell >= 0 && rightCell < 8;

        if (forwardRow >= 0 && forwardRow < 8) {
            if (rightValid) {
                Move forwardRight = new Move(pos, new Position(forwardRow, rightCell));
                if (MoveValidator.validateMove(model, forwardRight)) {
                    validJumps.add(forwardRight);
                }
            }
            if (leftValid) {
                Move forwardLeft = new Move(pos, new Position(forwardRow, leftCell));
                if (MoveValidator.validateMove(model, forwardLeft)) {
                    validJumps.add(forwardLeft);
                }
            }
        }
        if (backRow >= 0 && backRow < 8) {
            if (rightValid) {
                Move backRight = new Move(pos, new Position(backRow, rightCell));
                if (MoveValidator.validateMove(model, backRight)) {
                    validJumps.add(backRight);
                }
            }
            if (leftValid) {
                Move backLeft = new Move(pos, new Position(backRow, leftCell));
                if (MoveValidator.validateMove(model, backLeft)) {
                    validJumps.add(backLeft);
                }
            }
        }
        return validJumps;
    }
}
