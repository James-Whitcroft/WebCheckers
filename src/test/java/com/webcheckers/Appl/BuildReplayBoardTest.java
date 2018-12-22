package com.webcheckers.Appl;

import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Move;
import com.webcheckers.Model.Player;
import com.webcheckers.Model.Position;
import com.webcheckers.ui.BoardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuildReplayBoardTest {

    private Player redPlayer = new Player("red");
    private Player whitePlayer = new Player("white");
    private MoveList moveList = new MoveList(redPlayer.getName(), whitePlayer.getName());
    private BoardModel model;

    @BeforeEach
    public void setup(){
        model = new BoardModel(redPlayer, whitePlayer, null);
        moveList.resetReplay();
    }

    @Test
    void testBuildBoard(){
        BoardView initialReplay = BuildReplayBoard.buildBoard(moveList);
        assertTrue(initialReplay.getRedPlayer().getName() == "red");

        Move newMove = new Move(new Position(2, 1), new Position(3, 0));

        moveList.addMove(newMove);
        moveList.forward();

        BoardView fullReplay = BuildReplayBoard.buildBoard(moveList);
        assertTrue(fullReplay.getRedPlayer().getName() == "red");
    }
}