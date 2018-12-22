package com.webcheckers.Appl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SavedGameListTest {

    private SavedGameList CuT;
    private MoveList moveList;
    private String playerName = "testPlayer";

    @BeforeEach
    public void setup(){
        CuT = new SavedGameList();
        moveList = mock(MoveList.class);
    }

    @Test
    public void testAddGameToSavedGames(){
        assertTrue(CuT.getAllSavedGames().size() == 0);
        CuT.addGameToSavedGames(moveList);
        assertTrue(CuT.getAllSavedGames().size() == 1);
    }

    @Test
    public void testGetGameList(){
        CuT.addGameToSavedGames(moveList);
        assertEquals(moveList, CuT.getSavedGameList(moveList.getGameID()));
    }

    @Test
    public void testResetReplay(){
        CuT.addGameToSavedGames(moveList);
        CuT.resetReplay(moveList.getGameID());
        assertEquals(moveList, CuT.getSavedGameList(moveList.getGameID()));
    }
}