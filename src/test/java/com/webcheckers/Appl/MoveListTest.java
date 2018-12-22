package com.webcheckers.Appl;

import com.webcheckers.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class MoveListTest {

    private MoveList CuT;
    private BoardModel boardModel;

    @BeforeEach
    public void setUp(){
        Player redPlayer = mock(Player.class);
        Player whitePlayer = mock(Player.class);
        this.CuT = new MoveList(redPlayer.getName(), whitePlayer.getName());
    }

    @Test
    public void testAddMove(){
        Move move = mock(Move.class);
        assertTrue(CuT.getMoveList().size() == 0);
        CuT.addMove(move);
        assertTrue(CuT.getMoveList().size() > 0);
    }

    @Test
    public void getMove(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertEquals(move, CuT.getMove(0));
    }

    @Test
    public void testHasNextTrue(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertTrue(CuT.hasNext());
    }

    @Test
    public void testHasNextFalse(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        CuT.forward();
        assertFalse(CuT.hasNext());
    }

    @Test
    public void testHasPreviousTrue(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        CuT.addMove(move);
        CuT.forward();
        assertTrue(CuT.hasPrevious());
    }

    @Test
    public void testHasPreviousFalse(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertFalse(CuT.hasPrevious());
    }

    @Test
    public void testGetIndex(){
        int index = CuT.getIndex();
        int test = 0;
        assertEquals(test, index);
    }

    @Test
    public void testForwardTrue(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertTrue(CuT.forward());
        assertEquals(1, CuT.getIndex());
    }

    @Test
    public void testForwardFalse(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        CuT.forward();
        assertFalse(CuT.forward());
        assertEquals(1, CuT.getIndex());
    }

    @Test
    public void testBackwardTrue(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        CuT.forward();
        assertTrue(CuT.backward());
        assertEquals(0, CuT.getIndex());

    }

    @Test
    public void testBackwardFalse(){
        Move move = mock(Move.class);
        CuT.addMove(move);
        assertFalse(CuT.backward());
        assertEquals(0, CuT.getIndex());
    }


}
