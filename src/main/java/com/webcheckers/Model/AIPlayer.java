package com.webcheckers.Model;

import com.webcheckers.Appl.AIAlgorithm;

import java.util.List;

public class AIPlayer extends Player {

    private int difficulty;

    /**
     * Creates a new AI Player
     * @param name name of the player
     * @param difficulty difficulty of the player (0 - 6)
     */
    public AIPlayer(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
    }

    /**
     * @return difficulty value of the AI Player
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Makes the moves for the AI player and changes the turn back.
     * @param model BoardModel on which to make the move
     */
    public void makeMove(BoardModel model) {
        List<Move> moves = AIAlgorithm.GenerateMove(model, this.difficulty);
        for (Move move: moves) {
            model.addPendingMove(move);
        }
        while (model.isJumping()) {
            Position lastPos = model.getPendingMoves().getLast().getEnd();
            Move move = AIAlgorithm.GetMultipleJump(model, lastPos);
            if (move != null) {
                model.addPendingMove(move);
            }
            else {
                break;
            }
        }
        model.makeMoves();
        model.changeTurns();
    }

}

