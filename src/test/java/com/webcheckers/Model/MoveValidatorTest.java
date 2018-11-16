package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@Tag("Model-tier")
class MoveValidatorTest {

    private BoardModel model;

    private void mockPiece(Position position, Piece.pieceType type, Piece.color color) {


        Piece mockPiece = mock(Piece.class);
        when(mockPiece.getColor()).thenReturn(color);
        when(mockPiece.getType()).thenReturn(type);

        Space mockSpace = mock(Space.class);
        when(mockSpace.getPiece()).thenReturn(mockPiece);
        when(mockSpace.isValid()).thenReturn(false);

        when(model.getSpacePendingMoves(position)).thenReturn(mockSpace);
        when(model.getSpace(position)).thenReturn(mockSpace);
    }

    private void mockSpace(Position position) {
        Space mockSpace = mock(Space.class);
        when(mockSpace.getPiece()).thenReturn(null);
        when(mockSpace.isValid()).thenReturn(true);

        when(model.getSpacePendingMoves(position)).thenReturn(mockSpace);
    }

    @BeforeEach
    void initializeTests() {
        model = mock(BoardModel.class);
        when(model.getSpacePendingMoves(any())).thenReturn(new Space(0, true));
        when(model.getSpace(any())).thenReturn(new Space(0, true));
    }

    @Test
    void simpleMoveRedSuccessTest() {
        Position start = new Position(2, 2);
        Position end = new Position(3, 3);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "Simple move should have succeeded");
    }

    @Test
    void simpleMoveWhiteSuccessTest() {
        Position start = new Position(7, 7);
        Position end = new Position(6, 6);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "Simple move should have succeeded");
    }

    @Test
    void wrongWaySimpleMoveFailTest() {
        Position start = new Position(2, 2);
        Position end = new Position(3, 3);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move));
    }

    @Test
    void blockedSimpleMoveFailTest() {
        Position start = new Position(5,5);
        Position end = new Position(4,4);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockPiece(end, Piece.pieceType.SINGLE, Piece.color.WHITE);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move));
    }

    @Test
    void backwardsMoveRedSuccessTest() {
        Position start = new Position(2,0);
        Position end = new Position(1,1);
        mockPiece(start, Piece.pieceType.KING, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "Backwards move should have succeeded");
    }

    @Test
    void backwardsMoveWhiteSuccess() {
        Position start = new Position(4,2);
        Position end = new Position(5,3);
        mockPiece(start, Piece.pieceType.KING, Piece.color.WHITE);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "Backwards move should have succeeded");
    }

    @Test
    void notKingBackwardsMoveFailTest() {
        Position start = new Position(2,0);
        Position end = new Position(1,1);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Backwards move should have failed");
    }

    @Test
    void simpleJumpSuccessTest() {
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        Position jumped = new Position(2, 2);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "Simple jump should have succeeded");
    }

    @Test
    void wrongTeamSimpleJumpFailTest() {
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        Position jumped = new Position(2, 2);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to jump a friendly piece");
    }

    @Test
    void noPieceSimpleJumpFailTest() {
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        Position jumped = new Position(2, 2);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(jumped);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to jump a friendly piece");
    }

    @Test
    void backwardsJumpSuccessTest() {
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        Position jumped = new Position(2, 2);
        mockPiece(start, Piece.pieceType.KING, Piece.color.WHITE);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertTrue(MoveValidator.validateMove(model, move), "King should be able to jump backwards");
    }

    @Test
    void notKingBackwardsJumpFailTest() {
        Position start = new Position(1, 1);
        Position end = new Position(3, 3);
        Position jumped = new Position(2, 2);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Single should not be able to jump backwards");
    }

    @Test
    void moveTooManyRowsTest() {
        Position start = new Position(2, 2);
        Position end = new Position(5, 3);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to move > 2 rows");
    }

    @Test
    void moveNoRowsTest() {
        Position start = new Position(2, 2);
        Position end = new Position(2, 4);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to move 0 rows");
    }

    @Test
    void moveTooManyCellsTest() {
        Position start = new Position(2, 2);
        Position end = new Position(3, 5);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to move > 2 cells");
    }

    @Test
    void moveNoCellsTest() {
        Position start = new Position(2, 2);
        Position end = new Position(4, 2);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        Move move = new Move(start, end);
        assertFalse(MoveValidator.validateMove(model, move), "Should not be able to move 0 cells");
    }

    @Test
    void teamHasMoveTrueTest() {
        mockPiece(new Position(6, 5), Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(new Position(7, 4), Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockPiece(new Position(7, 6), Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockPiece(new Position(2, 3), Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(new Position(3, 2));
        mockSpace(new Position(3, 4));
        assertTrue(MoveValidator.teamHasMove(model, Piece.color.RED));
    }

    @Test
    void teamHasMoveFalseTest() {
        mockPiece(new Position(6, 5), Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(new Position(7, 4), Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockPiece(new Position(7, 6), Piece.pieceType.SINGLE, Piece.color.WHITE);
        assertFalse(MoveValidator.teamHasMove(model, Piece.color.RED), "Red Team should have a valid move");
    }

    @Test
    void teamHasMoveBackwardsTrueTest() {
        mockPiece(new Position(7, 6), Piece.pieceType.KING, Piece.color.RED);
        mockSpace(new Position(6, 5));
        mockSpace(new Position(6, 7));
        assertTrue(MoveValidator.teamHasMove(model, Piece.color.RED));
    }

    @Test
    void teamHasJumpTest(){
        mockPiece(new Position(4, 6), Piece.pieceType.KING, Piece.color.RED);
        mockPiece(new Position(3, 5), Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(new Position(2, 4));
        Position start = new Position(1, 2);
        Position end = new Position(2, 1);
        Move move = new Move(start, end);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        assertFalse(MoveValidator.validateMove(model, move), "Should force the jump and not allow the simple move");
    }

    @Test
    void pieceHasJumpSimpleTrue() {
        Position start = new Position(3, 4);
        Position jumped = new Position(4, 5);
        Position end = new Position(5, 6);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(end);
        assertTrue(MoveValidator.pieceHasJump(model, start), "Piece should have a simple jump");
    }

    @Test
    void pieceHasJumpBackwardsTrue() {
        Position start = new Position(5, 6);
        Position jumped = new Position(4, 5);
        Position end = new Position(3, 4);
        mockPiece(start, Piece.pieceType.KING, Piece.color.RED);
        mockPiece(jumped, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(end);
        assertTrue(MoveValidator.pieceHasJump(model, start), "Piece should have a backwards jump");
    }

    @Test
    void pieceHasJumpFalseSingle() {
        Position piecePos = new Position(3,6);
        Position forwardRight = new Position(4, 5);
        Position forwardLeft = new Position(4, 7);
        Position backRight = new Position(2, 5);
        Position backRightJump = new Position(1, 4);
        mockPiece(piecePos, Piece.pieceType.SINGLE, Piece.color.RED);
        mockPiece(forwardLeft, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(forwardRight);
        mockPiece(backRight, Piece.pieceType.SINGLE, Piece.color.WHITE);
        mockSpace(backRightJump);
        assertFalse(MoveValidator.pieceHasJump(model, piecePos), "Piece should not have a jump");
    }

    @Test
    void pieceHasJumpFalseKing() {
        Position start = new Position(5, 4);
        mockPiece(start, Piece.pieceType.KING, Piece.color.RED);
        assertFalse(MoveValidator.pieceHasJump(model, start), "Piece should not have a jump");
    }

    @Test
    void pieceHasMoveSimpleTrue() {
        Position start = new Position(3, 4);
        Position end = new Position(4, 5);
        mockPiece(start, Piece.pieceType.SINGLE, Piece.color.RED);
        mockSpace(end);
        //assertTrue(MoveValidator.pieceHasMove(model, start), "Piece should have a simple jump");
    }

    @Test
    void pieceHasMoveBackwardsTrue() {

    }

    @Test
    void pieceHasMoveFalseSingle() {

    }

    @Test
    void pieceHasMoveFalseKing() {

    }

}