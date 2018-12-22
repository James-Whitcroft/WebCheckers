package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AIPlayerTest {

    private AIPlayer CuT;

    @BeforeEach
    void initializeTest() {
        CuT = new AIPlayer("testName", 2);
    }

    @Test
    void getDifficulty() {
        assertEquals(2, CuT.getDifficulty(), "Difficulty should be 2");
    }

    @Test
    void makeMove() {
        Player testPlayerRed = new Player("red");
        BoardModel model = new BoardModel(CuT, testPlayerRed, null);

        Move firstMove = new Move(new Position(2, 3), new Position(3, 4));
        Move secondMove = new Move(new Position(5, 4), new Position(4, 5));
        Move thirdMove = new Move(new Position(2, 1), new Position(3, 0));
        model.addPendingMove(firstMove);
        model.makeMoves();
        model.changeTurns();
        model.addPendingMove(secondMove);
        model.makeMoves();
        model.changeTurns();
        model.addPendingMove(thirdMove);
        model.makeMoves();
        model.changeTurns();
        CuT.makeMove(model);
        Position emptyPos = new Position(3, 4);
        assertNull(model.getSpace(emptyPos).getPiece(), "No piece should be at this space");
    }
}