package com.webcheckers.Appl;

import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Player;
import com.webcheckers.ui.BoardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
class GameListTest {

    private GameList Glt;
    private Player red;
    private Player white;
    private BoardModel model;
    private SavedGameList savedGames;

    @BeforeEach
    public void testSetup() {
        red = new Player("red");
        white = new Player("white");
        savedGames = Mockito.mock(SavedGameList.class);
        model = new BoardModel(white, red, savedGames);
        Glt = new GameList();
    }


    @Test
    void addGameTest() {
        assertTrue(Glt.addGame(model));
        assertFalse(Glt.addGame(model));
    }

    @Test
    void gameExistsTest() {
        Glt.addGame(model);
        assertTrue(Glt.gameExists("red"));
        assertFalse(Glt.gameExists("dragon"));
    }

    @Test
    void getBoardModelTest() {
        Glt.addGame(model);
        assertNotNull(Glt.getBoardModel(red));
    }

    @Test
    void getBoardViewTest() {
        BoardView test = new BoardView(red, model);
        Glt.addGame(model);
        assertEquals(test.getRedPlayer(), Glt.getBoardView(red).getRedPlayer());
    }

    @Test
    void removeBoardTest() {
        Glt.addGame(model);
        Glt.removeBoard(red);
        assertNull(Glt.getBoardModel(red));
    }
}