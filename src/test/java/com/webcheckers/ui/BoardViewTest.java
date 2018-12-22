package com.webcheckers.ui;

import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
class BoardViewTest {


    private Player red;
    private Player white;
    private SavedGameList savedGames;

    private BoardModel testModel;
    private BoardView CuT;


    @BeforeEach
    void testSetup() {
        red = Mockito.mock(Player.class);
        white = Mockito.mock(Player.class);
        savedGames = Mockito.mock(SavedGameList.class);
        testModel = new BoardModel(white, red, savedGames);

        CuT = new BoardView(white, testModel);
    }

    @Test
    void iteratorWhiteTest() {
        int rowNum = 0;
        Iterator<Row> rows = CuT.iterator();
        while (rows.hasNext()) {
            int colNum = 0;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(testModel.getBoard()[rowNum][colNum], space, "Space mismatch in iterator");
                colNum++;
            }
            rowNum++;
        }
    }

    @Test
    void iteratorRedTest() {
        CuT = new BoardView(red, testModel);
        int rowNum = 7;
        Iterator<Row> rows = CuT.iterator();
        while (rows.hasNext()) {
            int colNum = 7;
            Row row = rows.next();
            Iterator<Space> spaces = row.iterator();
            while (spaces.hasNext()) {
                Space space = spaces.next();
                Assertions.assertEquals(testModel.getBoard()[rowNum][colNum], space, "Space mismatch in iterator");
                colNum--;
            }
            rowNum--;
        }
    }


    @Test
    void getWhitePlayerTest() {
        Assertions.assertSame(CuT.getWhitePlayer(), white, "White player was not retrieved properly");
    }

    @Test
    void getRedPlayerTest() {
        Assertions.assertSame(CuT.getRedPlayer(), red, "Red player was not retrieved properly");
    }

    @Test
    void getActiveColorTest() {
        Assertions.assertSame(CuT.getActiveColor(), Piece.color.RED, "Active player was not red");
    }
}