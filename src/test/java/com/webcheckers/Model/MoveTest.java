package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class MoveTest {

    private Move CuT;
    private Position start;
    private Position end;

    @BeforeEach
    void initializeTest() {
        start = mock(Position.class);
        end = mock(Position.class);
        CuT = new Move(start, end);
    }


    @Test
    void getStartTest() {
        assertSame(start, CuT.getStart(), "getStart did not return the correct value");
    }

    @Test
    void getEndTest() {
        assertSame(end, CuT.getEnd(), "getEnd did not return the correct value");

    }
}