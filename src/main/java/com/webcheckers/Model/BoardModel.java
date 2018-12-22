package com.webcheckers.Model;

import com.webcheckers.Appl.MoveList;
import com.webcheckers.Appl.SavedGameList;

import java.util.Deque;
import java.util.LinkedList;

/**
 * The backend representation of the game board
 */
public class BoardModel {

    //region Constants

    private static final int BOARD_SIZE = 8;
    private final SavedGameList savedGames;

    //endregion

    //region Attributes


    private Player whitePlayer;         /** The player for the white team */
    private Player redPlayer;           /** The player for the red team */

    private Space[][] board;            /** The board representation*/
    private Space[][] pendingBoard;     /** The temporary board representation for unsubmitted moves*/
    private Piece.color active;         /** The color of the active player*/
    private Player activePlayer;        /** The player whose turn it is*/

    private MoveList moveList;          /** The ordered list of all moves made in a game **/
    private Deque<Move> pendingMoves;   /** The Deque of moves that have yet to be submitted*/
    private boolean isJumping = false;  /** Bool to track if the pending move is jumping */
    private boolean isMoving = false;   /** Bool to track if the pending move is moving */

    //endregion

    //region Constructors

    public BoardModel(Player whitePlayer, Player redPlayer, final SavedGameList savedGames) {
        this.whitePlayer = whitePlayer;
        this.redPlayer = redPlayer;
        this.savedGames = savedGames;
        this.active = Piece.color.RED;
        this.activePlayer = redPlayer;
        pendingMoves = null;
        InitializeSpaces();
        PopulateBoard();

//         populateLongJumpTestBoard();     // multi-jump test
//         populateNoMoveBoard();           // no move test
//         populateJumpTestBoard();         // king jump
    }

    //endregion

    //region Private Methods

    /**
     * Initializes the board with correct number and color of spaces
     */
    private void InitializeSpaces() {
        board = new Space[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if(row % 2 == 0) { //this row starts on a light space
                    if(col % 2 == 0) //this column is a light space
                        board[row][col] = new Space(col, true);
                    else // this column is a dark space
                        board[row][col] = new Space(col, false);
                }
                else { //this row starts on a dark space
                    if(col % 2 == 1) //this column is a light space
                        board[row][col] = new Space(col, true);
                    else // this column is a dark space
                        board[row][col] = new Space(col, false);
                }
            }
        }
    }

    /**
     * Populates the board with starting pieces
     * Must call InitializeSpaces() prior to calling PopulateBoard()
     */
    private void PopulateBoard() {
        // assert (board != null); // Board must be initialized first with InitializeSpaces
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if(row < 3) { // these rows need to have red pieces on black spaces
                    if (board[row][col].isValid()) {
                        board[row][col].placePiece(new Piece(Piece.color.RED));
                    }
                }
                else if (row > 4) { // these rows need to have white pieces on black spaces
                    if (board[row][col].isValid()) {
                        board[row][col].placePiece(new Piece(Piece.color.WHITE));
                    }
                }
            }
        }
    }

    // region TEST BOARDS
    /**
     * TEST BOARD
     */
    /**
     * use for mocking up smaller samples
     */
    private void populateJumpTestBoard() {
        board[1][2].placePiece(new Piece(Piece.color.RED));
        board[2][1].placePiece(new Piece(Piece.color.RED));
        board[1][4].placePiece(new Piece(Piece.color.RED));
        board[4][3].placePiece(new Piece(Piece.color.WHITE));
    }
    /**
     * use for mocking up smaller samples
     */
    private void populateLongJumpTestBoard() {

        board[2][5].placePiece(new Piece(Piece.color.RED));
        board[4][3].placePiece(new Piece(Piece.color.RED));
        board[5][2].placePiece(new Piece(Piece.color.RED));
        board[7][0].placePiece(new Piece(Piece.color.WHITE));
    }

    /**
     * use for mocking up smaller samples
     */
    private void populateNoMoveBoard() {

        board[0][1].placePiece(new Piece(Piece.color.RED));
        board[1][6].placePiece(new Piece(Piece.color.RED));
        board[1][0].placePiece(new Piece(Piece.color.WHITE));
        board[1][2].placePiece(new Piece(Piece.color.WHITE));
        board[2][1].placePiece(new Piece(Piece.color.WHITE));
        board[2][3].placePiece(new Piece(Piece.color.WHITE));
        board[3][4].placePiece(new Piece(Piece.color.WHITE));
    }

    // endregion


    /**
     * Creates a new pending board with the pending moves
     */
    private void updatePendingBoard() {
        pendingBoard = new Space[BOARD_SIZE][BOARD_SIZE];
        // Initialize new pending board
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                pendingBoard[r][c] = new Space(board[r][c]);
            }
        }
        if (pendingMoves != null && !pendingMoves.isEmpty()) {
            for (Move move : pendingMoves) {
                makeMove(move, true);
            }
        }
    }

    //endregion

    //region Public Methods


    /**
     * @return the 2D representation of the board spaces
     */
    public Space[][] getBoard() {
        return board;
    }

    /**
     * @param position the position of the space to be returned
     * @return the space at the given position
     */
    public Space getSpace(Position position) {
        return getRow(position.getRow())[position.getCell()];
    }

    public Space getSpacePendingMoves(Position position) {
        if (pendingBoard != null) {
            return pendingBoard[position.getRow()][position.getCell()];
        }
        return getSpace(position);
    }

    /**
     * @param index index of the row to be retrieved
     * @return 1D array of spaces in a row at given index
     */
    public Space[] getRow(int index) {
        if (index < 0 || index > 7) {
            throw new IllegalArgumentException("Index must be between 0 and 7");
        }
        return board[index];
    }


    /**
     * @param index index of the row to be retrieved
     * @return 1D array of spaces in a row at a given index reversed
     */
    public Space[] getRowReversed(int index) {
        Space[] row = getRow(index);
        Space[] reverseRow = new Space[BOARD_SIZE];
        int j = BOARD_SIZE;
        for (int i = 0; i < BOARD_SIZE; i++) {
            j--;
            reverseRow[i] = row[j];
        }
        return reverseRow;
    }



    /**
     * @return the red player of the game
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * @return the white player of the game
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * @return the color of the player whose turn it is
     */
    public Piece.color getActive() {
        return this.active;
    }

    /**
     * @return the player whose turn it is
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * @return true if the pending move is a jump
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * @return True if a piece is moving
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Changed the active color and the active player
     */
    public void changeTurns() {
        active = active == Piece.color.RED ? Piece.color.WHITE : Piece.color.RED;
        activePlayer = activePlayer == redPlayer ? whitePlayer : redPlayer;
    }

    /**
     * Makes a move on a board
     * @param move the move to be represented on the board
     * @param isPending True if it is a pending move, false if it is a submitted move
     */
    private void makeMove(Move move, boolean isPending) {
        Space[][] boardToChange = isPending ? pendingBoard : board;
        Position start = move.getStart();
        Position end = move.getEnd();
        int rowDiff = Math.abs(start.getRow() - end.getRow());
        Piece mover = boardToChange[move.getStart().getRow()][move.getStart().getCell()].getPiece();
        boardToChange[move.getStart().getRow()][move.getStart().getCell()].removePiece();
        boardToChange[move.getEnd().getRow()][move.getEnd().getCell()].placePiece(mover);
        if (rowDiff == 2) { // If jump
            isJumping = true;
            Position jumpedPos = new Position( (start.getRow() + end.getRow()) / 2, (start.getCell() + end.getCell()) / 2);
            boardToChange[jumpedPos.getRow()][jumpedPos.getCell()].removePiece();
        }
        isMoving = true;
    }

    /**
     * Finalizes pending moves
     */
    public void makeMoves() {
        if (savedGames != null && moveList == null) {
            moveList = new MoveList(redPlayer.getName(), whitePlayer.getName());
        }
        if (pendingMoves != null) {
            for (Move move : pendingMoves) {
                makeMove(move, false);
                Piece piece = board[move.getEnd().getRow()][move.getEnd().getCell()].getPiece();
                piece.checkIfKingMe(piece, move);
                if (savedGames != null) {
                    moveList.addMove(move);
                }
            }
            if (savedGames != null) {
                savedGames.addGameToSavedGames(moveList);
            }
            resetPendingMoves();
        }
    }

    /**
     * Undoes the most recent pending move
     * @return true if successful
     */
    public boolean backUpMove() {
        if (pendingMoves != null) {
            pendingMoves.removeLast();
            updatePendingBoard();
            if (pendingMoves.isEmpty()) {
                isJumping = false;
                isMoving = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if there is a winner by checking if there is any remaining pieces for the opponent
     * @return the winning player, or null if there is no winner
     */
    public Player getWinner() {
        boolean redWins = true;
        boolean whiteWins = true;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = getSpace(new Position(r, c)).getPiece();
                if (piece != null) {
                    if (piece.getColor() == Piece.color.RED) {
                        whiteWins = false;
                    }
                    else if (piece.getColor() == Piece.color.WHITE) {
                        redWins = false;
                    }
                }
            }
        }
        if (whiteWins) {
            return whitePlayer;
        } else if (redWins) {
            return redPlayer;
        }
        return null;
    }

    /**
     * Adds a pending move to the deque of pending moves
     * @param move the move to add
     */
    public void addPendingMove(Move move) {
        if (pendingMoves == null) {
            pendingMoves = new LinkedList<>();
        }
        if(move != null) {
            pendingMoves.add(move);
        }
        updatePendingBoard();
    }

    /**
     * resets all pending moves and resets board to start of turn state
     */
    public void resetPendingMoves() {
        pendingMoves = null;
        isJumping = false;
        isMoving = false;
        updatePendingBoard();
    }

    /**
     * @return a deque of pending moves
     */
    public Deque<Move> getPendingMoves() {
        return pendingMoves;
    }



    //endregion
}
