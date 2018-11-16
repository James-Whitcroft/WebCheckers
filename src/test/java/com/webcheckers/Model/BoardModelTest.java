package com.webcheckers.Model;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class BoardModelTest {

    private Player red;
    private Player white;
    private BoardModel CuT;

    @BeforeEach
    void testSetup() {
        red = mock(Player.class);
        white = mock(Player.class);
        CuT = new BoardModel(white, red);
    }

    @Test
    void getBoardTest() {
        assertNotNull(CuT.getBoard(), "CuT is null");
        Space[][] testModel = new Space[8][8]; // Correct board configuration
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(row % 2 == 0) //this row starts on a light space
                {
                    if(col % 2 == 0) //this column is a light space
                        testModel[row][col] = new Space(col, true);
                    else // this column is a dark space
                        testModel[row][col] = new Space(col, false);
                }
                else //this row starts on a dark space
                {
                    if(col % 2 == 1) //this column is a light space
                        testModel[row][col] = new Space(col, true);
                    else // this column is a dark space
                        testModel[row][col] = new Space(col, false);
                }
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(row < 3) // these rows need to have red pieces on black spaces
                {
                    if (testModel[row][col].isValid()) {
                        testModel[row][col].placePiece(new Piece(Piece.color.RED));
                    }
                }
                else if (row > 4) // these rows need to have white pieces on black spaces
                {
                    if (testModel[row][col].isValid()) {
                        testModel[row][col].placePiece(new Piece(Piece.color.WHITE));
                    }
                }
            }
        }

        for (int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                assertEquals(testModel[row][col], CuT.getBoard()[row][col],
                        "Space mismatch at (" + row + ", " + col + ")");
            }
        }
    }

    @Test
    void getRowIndexTooLow() {
        assertThrows(IllegalArgumentException.class, () -> {CuT.getRow(-1); }, "Should throw an illegal argument exception for index < 0");
    }

    @Test
    void getRowIndexTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {CuT.getRow(8); }, "Should throw an illegal argument exception for index > 7");
    }

    @Test
    void getRowTest() {
        Space[] rowList = new Space[] {
                new Space(0, true),
                new Space(1, false, new Piece(Piece.color.RED)),
                new Space(2, true),
                new Space(3, false, new Piece(Piece.color.RED)),
                new Space(4, true),
                new Space(5, false, new Piece(Piece.color.RED)),
                new Space(6, true),
                new Space(7, false, new Piece(Piece.color.RED))
        };

        for (int i = 0; i < 8; i++) {
            assertEquals(rowList[i], CuT.getRow(0)[i], "Rows mismatched");
        }
    }

    @Test
    void getRowReversedTest() {
        Space[] rowList = new Space[] {
                new Space(7, false, new Piece(Piece.color.RED)),
                new Space(6, true),
                new Space(5, false, new Piece(Piece.color.RED)),
                new Space(4, true),
                new Space(3, false, new Piece(Piece.color.RED)),
                new Space(2, true),
                new Space(1, false, new Piece(Piece.color.RED)),
                new Space(0, true)
        };
        for (int i = 0; i < 8; i++) {
            assertEquals(rowList[i], CuT.getRowReversed(0)[i], "Rows mismatched");
        }
    }

    @Test
    void getRedPlayerTest() {
        assertSame(red, CuT.getRedPlayer(), "Red player was not retrieved properly");
    }

    @Test
    void getWhitePlayerTest() {
        assertSame(white, CuT.getWhitePlayer(), "White player was not retrieved properly");
    }

    @Test
    void getActiveTest() {
        assertSame(CuT.getActive(), Piece.color.RED, "Active player was not red");
    }

    @Test
    void getSpaceTest() {
        Position position = new Position(0, 7);
        Piece piece = new Piece(Piece.color.RED, Piece.pieceType.SINGLE);
        Space space = new Space(7, false);
        space.placePiece(piece);

        assertEquals(space, CuT.getSpace(position), "Spaces should be equal");
    }

    @Test
    void getSpacePendingMovesTest() {
        Position start = new Position(2, 3);
        Position end = new Position(3, 4);
        Move move = new Move(start, end);
        CuT.addPendingMove(move);
        Piece piece = new Piece(Piece.color.RED, Piece.pieceType.SINGLE);
        Space space = new Space(4, false);
        space.placePiece(piece);
        assertEquals(space, CuT.getSpacePendingMoves(end), "Pending Move space should be equal");
    }

    @Test
    void getSpacePMNoPendingMovesTest() {
        Position position = new Position(0, 7);
        Piece piece = new Piece(Piece.color.RED, Piece.pieceType.SINGLE);
        Space space = new Space(7, false);
        space.placePiece(piece);
        CuT.resetPendingMoves();
        assertEquals(space, CuT.getSpacePendingMoves(position), "Spaces should be equal");
    }

    @Test
    void changeTurnsTest() {
        assertEquals(Piece.color.RED, CuT.getActive());
        assertSame(red, CuT.getActivePlayer());
        CuT.changeTurns();
        assertEquals(Piece.color.WHITE, CuT.getActive());
        assertSame(white, CuT.getActivePlayer());
    }

    @Test
    void isJumpingTest() {
        assertFalse(CuT.isJumping(), "Should not be jumping yet");
        CuT.getBoard()[3][4].placePiece(new Piece(Piece.color.WHITE));
        Position start = new Position(2, 3);
        Position end = new Position(4, 5);
        Move move = new Move(start, end);
        CuT.addPendingMove(move);
        assertTrue(CuT.isJumping(), "Should be jumping");
    }

    @Test
    void isMovingTest() {
        assertFalse(CuT.isMoving(), "Should not be moving");
        Position start = new Position(2, 3);
        Position end = new Position(3, 4);
        Move move = new Move(start, end);
        CuT.addPendingMove(move);
        assertTrue(CuT.isMoving(), "Should be moving");
    }

    @Test
    void finalizeMovesTest() {
        Position start = new Position(2, 3);
        Position end = new Position(3, 4);
        Move move = new Move(start, end);
        CuT.addPendingMove(move);
        Piece piece = new Piece(Piece.color.RED, Piece.pieceType.SINGLE);
        Space space = new Space(4, false);
        space.placePiece(piece);
        CuT.makeMoves();
        assertEquals(space, CuT.getSpace(end), "Final Move space should be equal");
    }

    @Test
    void backupMoveTest() {
        Position start = new Position(2, 3);
        Position end = new Position(3, 4);
        Move move = new Move(start, end);
        CuT.addPendingMove(move);
        Piece piece = new Piece(Piece.color.RED, Piece.pieceType.SINGLE);
        Space space = new Space(4, false);
        space.placePiece(piece);
        CuT.backUpMove();
        assertNotEquals(space, CuT.getSpacePendingMoves(end), "Final Move space should not be equal");
    }

    @Test
    void backupMoveNoPendingTest() {
        assertFalse(CuT.backUpMove());
    }

    @Test
    void getWinnerTestNoWinner() {
        assertNull(CuT.getWinner(), "No winner should return null");
    }

    @Test
    void getWinnerTestRedWin() {
        for (int r = 5; r < 8; r++) {
            for (int c = 0; c < 8; c ++) {
                CuT.getBoard()[r][c].removePiece();
            }
        }
        assertSame(red, CuT.getWinner());
    }

    @Test
    void getWinnerTestWhiteWin() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 8; c ++) {
                CuT.getBoard()[r][c].removePiece();
            }
        }
        assertSame(white, CuT.getWinner());
    }
}