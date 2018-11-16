package com.webcheckers.Model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PositionTest {

    @Test
    void invalidConstructorTest() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-2, 4));
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 4));
        assertThrows(IllegalArgumentException.class, () -> new Position(4, -1));
        assertThrows(IllegalArgumentException.class, () -> new Position(4, 9));
    }

    @Test
    void getRowTest() {
        Position CuT = new Position(2, 5);
        assertEquals(2, CuT.getRow(), "getRow did not return the correct value");
        CuT = new Position(7, 2);
        assertEquals(7, CuT.getRow(), "getRow did not return the correct value");
    }

    @Test
    void getCellTest() {
        Position CuT = new Position(2, 5);
        assertEquals(5, CuT.getCell(), "getCell did not return the correct value");
        CuT = new Position(7, 2);
        assertEquals(2, CuT.getCell(), "getCell did not return the correct value");
    }

    @Test
    void equalsTest() {
        Position a = new Position(3, 3);
        Position b = new Position(3, 3);

        assertEquals(a, b, "These positions should be equal");
        assertNotSame(a, b, "These positions should not be the same");
        assertEquals(a.hashCode(), b.hashCode(), "The hashcodes should be the same");

    }
}