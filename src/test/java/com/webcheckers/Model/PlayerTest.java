package com.webcheckers.Model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Model-tier")
public class PlayerTest {

    Player player = new Player("testPlayer");

    /**
     * Tests that if setInGame is set to true, the player.isInGame registers
     * that it is actively in a game
     */
    @Test
    public void testInGameTrue(){
        player.setInGame(true);
        assertTrue(player.isInGame(), "The player is in a game.");
    }

    /**
     * Tests that if setInGame is set to false, the player.isInGame registers
     * that it is not actively in a game (set to true)
     */
    @Test
    public void testInGameFalse(){
        player.setInGame(false);
        assertFalse(player.isInGame(), "The player is not in a game");
    }

    @Test
    public void testEqualsTrue(){
        Player player1 = new Player("testPlayer");
        player.setInGame(false);
        player1.setInGame(false);
        assertTrue(player.equals(player1), "These players are the same.");
    }

    @Test
    public void testEqualsFalse(){
        Player player1 = new Player("playerTest");
        player.setInGame(false);
        player1.setInGame(false);
        assertFalse(player.equals(player1), "These players are the not the same.");
    }
    @Test
    public void testEqualsObj(){
        Player player1 = new Player("playerTest");
        assertTrue(player1.equals(player1));
    }
    @Test
    public void testEqualsNotPlayer(){
        Player player1 = new Player("playerTest");
        assertFalse(player1.equals(new Piece(Piece.color.RED)));
    }
    @Test
    public void testHashCode(){
        Player player1 = new Player("dragon");
        assertEquals(player1.hashCode(), Objects.hash("dragon"));
    }

}
