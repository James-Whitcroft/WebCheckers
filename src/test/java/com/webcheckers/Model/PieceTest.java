package com.webcheckers.Model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.webcheckers.Model.Piece.color.RED;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PieceTest {

    private static final Piece.color COLOR = RED;

    /**
     * Test that the constructor creates a single piece with a red color.
     */
    @Test
    public void redOneArg() {
        final Piece testPiece = new Piece(RED);
        assertEquals(testPiece.getColor(), RED);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
    }

    /**
     * Test that the constructor creates a single piece with a white color.
     */
    @Test
    public void whiteOneArg(){
        final Piece testPiece = new Piece(Piece.color.WHITE);
        assertEquals(testPiece.getColor(), Piece.color.WHITE);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
    }

    /**
     * Test that the constuctor takes two args to make a white single
     */
    @Test
    public void whiteTwoArgSingle(){
        final Piece testPiece = new Piece(Piece.color.WHITE, Piece.pieceType.SINGLE);
        assertEquals(testPiece.getColor(), Piece.color.WHITE);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
    }

    /**
     * Test that the constuctor takes two args to make a red single
     */
    @Test
    public void redTwoArgSingle(){
        final Piece testPiece = new Piece(RED, Piece.pieceType.SINGLE);
        assertEquals(testPiece.getColor(), RED);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
    }

    /**
     * Test that the constuctor takes two args to make a white king
     */
    @Test
    public void whiteTwoArgKing(){
        final Piece testPiece = new Piece(Piece.color.WHITE, Piece.pieceType.KING);
        assertEquals(testPiece.getColor(), Piece.color.WHITE);
        assertEquals(testPiece.getType(), Piece.pieceType.KING);
    }

    /**
     * Test that the constuctor takes two args to make a red king
     */
    @Test
    public void redTwoArgKing(){
        final Piece testPiece = new Piece(RED, Piece.pieceType.KING);
        assertEquals(testPiece.getColor(), RED);
        assertEquals(testPiece.getType(), Piece.pieceType.KING);
    }

    /**
     * Check that a single piece has a single type
     */
    @Test
    void singleTypeTest() {
        final Piece testPiece = new Piece(COLOR);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
    }

    /**
     * Check that a king piece has a king type
     */
    @Test
    void kingTypeTest(){
        final Piece testPiece = new Piece(COLOR, Piece.pieceType.KING);
        assertEquals(testPiece.getType(), Piece.pieceType.KING);
    }

    /**
     * Check the piece is red
     */
    @Test
    void getRedTest() {
        final Piece testPiece = new Piece(COLOR);
        assertEquals(testPiece.getColor(), RED);
    }

    /**
     * Check the piece is white
     */
    @Test
    void getWhiteTest(){
        final Piece testPiece = new Piece(Piece.color.WHITE);
        assertEquals(testPiece.getColor(), Piece.color.WHITE);
    }

    /**
     * check the piece can be set to be a king
     */
    @Test
    void setKingTest() {
        final Piece testPiece = new Piece(COLOR);
        assertEquals(testPiece.getType(), Piece.pieceType.SINGLE);
        testPiece.setKing();
        assertEquals(testPiece.getType(), Piece.pieceType.KING);
    }

    /**
     * Test that a white piece is set to king with a move to the opponents back row
     */
    @Test
    void kingMeWhiteTest() {
        final Piece testPiece = new Piece(Piece.color.WHITE);
        final Position moveStart = new Position(1, 5);
        final Position moveEnd = new Position(0, 6);
        final Move testMove = new Move(moveStart, moveEnd);
        assertEquals(testPiece.checkIfKingMe(testPiece, testMove), true);
    }

    /**
     * Test that a red piece is set to king with a move to the opponents back row
     */
    @Test
    void kingMeRedTest() {
        final Piece testPiece = new Piece(Piece.color.RED);
        final Position moveStart = new Position(6, 5);
        final Position moveEnd = new Position(7, 6);
        final Move testMove = new Move(moveStart, moveEnd);
        assertEquals(testPiece.checkIfKingMe(testPiece, testMove), true);
    }

    @Test
    void testEquals() {
        Piece testPiece = new Piece(RED, Piece.pieceType.SINGLE);
        assertFalse(testPiece.equals(new String("not piece")));
        assertTrue(testPiece.equals(testPiece));
    }
    @Test
    void testHashCode() {
        Piece testPiece = new Piece(RED, Piece.pieceType.SINGLE);
        assertEquals(testPiece.hashCode(), testPiece.hashCode());
    }
}