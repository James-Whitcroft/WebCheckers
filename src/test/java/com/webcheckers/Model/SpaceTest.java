package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Model-tier")
class SpaceTest {

    //global variables
    private static final int INDEX = 0;
    private static final boolean IS_LIGHT = false;

    private Space CuT;

    //mock object
    private Piece piece;

    @BeforeEach
    void testSetup(){
        CuT = mock(Space.class);
    }

    @Test
    void constructor_no_piece(){
        CuT = new Space(INDEX, IS_LIGHT);
        assertEquals("Index: " + INDEX + ", light space: " + IS_LIGHT, CuT.toString());
    }

    @Test
    void constructor_with_piece(){
        piece = mock(Piece.class);
        CuT = new Space(INDEX, IS_LIGHT, piece);
        assertEquals("Index: " + INDEX + ", light space: " + IS_LIGHT + " occupied: " + true,
                     CuT.toString());
    }

    @Test
    void copy_constructor(){
        Space copy = new Space(INDEX, IS_LIGHT);
        CuT = new Space(copy);
        assertEquals(CuT.getCellIdx(), INDEX);
        assertEquals(CuT.getCellColor(), IS_LIGHT);
    }

    @Test
    void copy_constructor_with_piece(){
        piece = mock(Piece.class);
        Space copy = new Space(INDEX, IS_LIGHT, piece);
        CuT = new Space(copy);
        assertEquals(CuT.getCellIdx(), INDEX);
        assertEquals(CuT.getCellColor(), IS_LIGHT);
        assertEquals(CuT.getPiece(), piece);
    }

    @Test
    void isValid_with_piece() {
        piece = mock(Piece.class);
        CuT = new Space(INDEX, IS_LIGHT, piece);
        assertEquals(false, CuT.isValid());
    }

    @Test
    void isValid_no_piece() {
        CuT = new Space(INDEX, IS_LIGHT);
        assertEquals(true, CuT.isValid());
    }

    @Test
    void isValid_light_space() {
        CuT = new Space(INDEX, !IS_LIGHT);
        assertEquals(false, CuT.isValid());
    }

    @Test
    void isValid_light_space_with_piece() {
        piece = mock(Piece.class);
        CuT = new Space(INDEX, !IS_LIGHT, piece);
        assertEquals(false, CuT.isValid());
    }

    @Test
    void test_get_piece_with_piece(){
        piece = mock(Piece.class);
        CuT = new Space(INDEX, IS_LIGHT, piece);
        assertEquals(piece, CuT.getPiece());
    }

    @Test
    void test_get_piece_no_piece() {
        CuT = new Space(INDEX, IS_LIGHT);
        assertEquals(null, CuT.getPiece());
    }

    @Test
    void test_get_cellIdx(){
        CuT = new Space(INDEX, IS_LIGHT);
        assertEquals(INDEX, CuT.getCellIdx());
    }

    @Test
    void placePiece_no_piece() {
        CuT = new Space(INDEX, IS_LIGHT);
        assertEquals(false, CuT.placePiece(null));
    }

    @Test
    void placePiece_with_piece() {
        CuT = new Space(INDEX, IS_LIGHT);
        piece = mock(Piece.class);
        assertEquals(true, CuT.placePiece(piece));
    }

    @Test
    void test_remove_piece(){
        piece = mock(Piece.class);
        CuT = new Space(INDEX, IS_LIGHT, piece);
        assertEquals(CuT.getPiece(), piece);
        CuT.removePiece();
        assertEquals(CuT.getPiece(), null);
    }

    @Test
    void test_equals_and_hashCode(){
        piece = mock(Piece.class);
        CuT = new Space(INDEX, IS_LIGHT, piece);
        Space copySpace = new Space(0, false, piece); //same values as CuT
        Space spaceNoPiece = new Space(1, true); //different from CuT (no piece)
        Space difCopySpace = new Space(1, !IS_LIGHT); //copy of the dif space without a piece
        Space otherSpace = new Space(3, IS_LIGHT);


        assertFalse(copySpace.equals(new Piece(Piece.color.RED)));



        //test if two spaces with pieces and same values are equal
        assertEquals(true, CuT.equals(copySpace));
        assertEquals(CuT.hashCode(), copySpace.hashCode());

        //test if two spaces with different values are equal
        assertEquals(false, CuT.equals(spaceNoPiece));
        assertEquals(false, CuT.hashCode() == spaceNoPiece.hashCode());

        //test if two copy spaces without pieces and same values are equal
        assertEquals(true, spaceNoPiece.equals(difCopySpace));
        assertEquals(true, spaceNoPiece.hashCode() == difCopySpace.hashCode());

        //test if two different spaces without pieces are equal
        assertEquals(false, spaceNoPiece.equals(otherSpace));
        assertEquals(false, spaceNoPiece.hashCode() == otherSpace.hashCode());
    }

}