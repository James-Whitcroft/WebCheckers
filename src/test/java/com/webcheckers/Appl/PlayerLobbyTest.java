package com.webcheckers.Appl;

import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
class PlayerLobbyTest {

    private PlayerLobby PlT;

    private final String spaceName = " ";
    private final String shortName = "";
    private final String longName = "dddddddddddddddddddddddddddddddddddddddddddddddddddddd";
    private final String specialName = "dragonTaco@!";
    private final String goodName = "Cellar Door";

    @BeforeEach
    public void testSetup() {
        PlT = new PlayerLobby();
    }

    @Test
    void addPlayerTest() {

        assertFalse(PlT.addPlayer(shortName));
        assertFalse(PlT.addPlayer(spaceName));
        assertFalse(PlT.addPlayer(longName));

        assertFalse(PlT.addPlayer(specialName));

        assertTrue(PlT.addPlayer(goodName));

        assertFalse(PlT.addPlayer(goodName));

    }

    @Test
    void removePlayerTest() {

        assertTrue(PlT.addPlayer(goodName));

        assertEquals(PlT.getActivePlayers().size(), 1);

        PlT.removePlayer(goodName);

        assertEquals(PlT.getActivePlayers().size(), 0);

    }

    @Test
    void getPlayerTest() {

        assertTrue(PlT.addPlayer(goodName));

        final Player compare = new Player(goodName);

        assertEquals(PlT.getPlayer(goodName), compare);

        assertNull(PlT.getPlayer("dragon"));
    }

    @Test
    void getActivePlayersTest() {

        assertTrue(PlT.addPlayer(goodName));

        final Player compare = new Player(goodName);

        assertTrue(PlT.getActivePlayers().contains(compare));
    }

    @Test
    void getOtherActivePlayersTest() {

        final String goodName2 = "Taco";
        final String goodName3 = "Bacon Bits";
        assertTrue(PlT.addPlayer(goodName));
        assertTrue(PlT.addPlayer(goodName2));
        assertTrue(PlT.addPlayer(goodName3));

        assertFalse(PlT.getOtherActivePlayers(new Player(goodName)).contains(goodName));

    }
}