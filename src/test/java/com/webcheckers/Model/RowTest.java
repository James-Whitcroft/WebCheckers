package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class RowTest {

    private int index;
    private Space[] spaces;
    private Row CuT;

    @BeforeEach
    public void setUp(){
        this.index = 1;
        spaces = new Space[8];
        CuT = new Row(index,spaces);
    }

    @Test
    public void testGetIndex(){
        int test = CuT.getIndex();
        assertEquals(index, test);
    }

    @Test
    public void testIterator(){
        assertNotNull(CuT.iterator());

    }
}
