package com.webcheckers.Appl;

import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Player;
import com.webcheckers.ui.BoardView;

public class BuildReplayBoard {

    public static BoardView buildBoard(MoveList moveList) {
        Player white = new Player("white");
        Player red = new Player("red");

        BoardModel model = new BoardModel(white, red, null);

        if (moveList.getIndex() == 0) {
            return new BoardView(red, model);
        }
        for (int i = 1; i <= moveList.getIndex(); i++) {
            model.addPendingMove(moveList.getMove(i - 1));
            model.makeMoves();
        }
        return new BoardView(red, model);
    }

}
