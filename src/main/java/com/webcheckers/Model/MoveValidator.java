package com.webcheckers.Model;

/**
 * Validates moves and determines whether or not they are legal in a game of American Checkers
 */
public class MoveValidator {

    //region Public Methods

    /**
     * Determines if a move is valid and legal
     * @param model the board model on which the move is occurring
     * @param move the move to be validated
     * @return true if it is a valid move, false otherwise
     */
    public static boolean validateMove(BoardModel model, Move move) {
        final Space start = model.getSpacePendingMoves(move.getStart());
        final Space end = model.getSpacePendingMoves(move.getEnd());
        final Piece piece = start.getPiece();

        if (!end.isValid()) { // If the destination is a valid space (open and dark)
            return false;
        }

        int rowDiff = Math.abs(move.getEnd().getRow() - move.getStart().getRow());
        if (rowDiff > 2 || rowDiff < 1) { // Check if piece is only moving 1 or 2 rows
            return false;
        }
        int cellDiff = Math.abs(move.getEnd().getCell() - move.getStart().getCell());

        if (cellDiff == 1) { // move
            if (model.isMoving()) {
                return false;
            }
            if (isMovingForward(piece.getColor(), move)) {
                return validateSimpleMove(move, model, piece);
            } else {
                return validateBackwardsMove(move, model, piece);
            }
        } else if (cellDiff == 2) { // jump
            if (model.isMoving() && !model.isJumping()) {
                return false;
            }
            if (isMovingForward(piece.getColor(), move)) {
                return validateSimpleJump(move, model, piece);
            } else {
                return validateBackwardsJump(move, model, piece);
            }
        } else { // error
            return false;
        }
    }

    /**
     * Used to check whether a team has a valid move to determine whether or not they lose from being unable to move
     * @param model the model to be tested
     * @param team the team to check if they have a move
     * @return true if one or more moves are available
     */
    public static boolean teamHasMove(BoardModel model, Piece.color team) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position pos = new Position(r,c);
                Piece piece = model.getSpace(pos).getPiece();
                if (piece != null && piece.getColor() == team && pieceHasMove(model, pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if a single piece has an available jump
     * @param model the model to be checked
     * @param piecePos the position of the piece to be checked
     * @return true if the piece has an available jump
     */
    public static boolean pieceHasJump(BoardModel model, Position piecePos) {
        Piece piece = model.getSpacePendingMoves(piecePos).getPiece();
        int teamMod = piece.getColor() == Piece.color.RED ? 1 : -1; // multiplier for row movement to determine forward direction
        int forwardRow = piecePos.getRow() + 2 * teamMod;
        int rightCell = piecePos.getCell() + 2;
        int leftCell = piecePos.getCell() - 2;
        boolean leftValid = leftCell >= 0 && leftCell < 8;
        boolean rightValid = rightCell >= 0 && rightCell < 8;

        if (forwardRow >= 0 && forwardRow < 8) {
            if (rightValid) {
                Move forwardRight = new Move(piecePos, new Position(forwardRow, rightCell));
                if (validateSimpleJump(forwardRight, model, piece)) {
                    return true;
                }
            }
            if (leftValid) {
                Move forwardLeft = new Move( piecePos, new Position(forwardRow, leftCell));
                if (validateSimpleJump(forwardLeft, model, piece)) {
                    return true;
                }
            }
        }
        int backRow = piecePos.getRow() - 2 * teamMod;
        if (backRow >= 0 && backRow < 8) {
            if (piece.getType() == Piece.pieceType.KING) {
                if (rightValid) {
                    Move backRight = new Move(piecePos, new Position(backRow, rightCell));
                    if (validateBackwardsJump(backRight, model, piece)) {
                        return true;
                    }
                }
                if (leftValid) {
                    Move backLeft = new Move(piecePos, new Position(backRow, leftCell));
                    if (validateBackwardsJump(backLeft, model, piece)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //endregion

    //region Private Methods

    /**
     * Determines whether a piece is moving forward relative to its player
     * @param color the color of the moving piece
     * @param move the move object to be examined
     * @return true if the piece is moving forward
     */
    private static boolean isMovingForward(Piece.color color, Move move) {
        int rowDiff = move.getEnd().getRow() - move.getStart().getRow();

        if (color == Piece.color.RED) {
            return rowDiff > 0;
        }
        else if (color == Piece.color.WHITE) {
            return rowDiff < 0;
        }
        return false; // Should never reach this
    }

    /**
     * Validates a simple move (forwards, no jump)
     * @param model the board model on which the move is occurring
     * @param piece the piece which is moving
     * @return true if the move is valid
     */
    private static boolean validateSimpleMove(Move move, BoardModel model, Piece piece) {
        Space space = model.getSpacePendingMoves(move.getEnd());
        return space.isValid() && !isJumpAvailable(model, piece.getColor());
    }

    /**
     * Validates a backwards move (backwards, no jump)
     * @param model the board model on which the move is occurring
     * @param piece the piece which is moving
     * @return true if the move is valid
     */
    private static boolean validateBackwardsMove(Move move, BoardModel model, Piece piece) {
        Space space = model.getSpacePendingMoves(move.getEnd());
        return space.isValid() && piece.getType() == Piece.pieceType.KING && !isJumpAvailable(model, piece.getColor());
    }

    /**
     * Validates a simple jump (forwards, jump)
     * @param move the move which is being examined
     * @param model the board model on which the move is occurring
     * @param piece the piece which is moving
     * @return true if the move is valid
     */
    private static boolean validateSimpleJump(Move move, BoardModel model, Piece piece) {
        Position start = move.getStart();
        Position end = move.getEnd();
        Space space = model.getSpacePendingMoves(end);
        if (!space.isValid()) {
            return false;
        }

        Position jumpedPos = new Position((start.getRow() + end.getRow()) / 2,
                (start.getCell() + end.getCell()) / 2);
        if (model.getSpacePendingMoves(jumpedPos).getPiece() != null &&
                model.getSpacePendingMoves(jumpedPos).getPiece().getColor() != piece.getColor()) { // if the jumped piece is on the other team
                return true;
        }
        return false;
    }

    /**
     * Validates a backwards jump (backwards, jump)
     * @param move the move which is being examined
     * @param model the board model on which the move is occurring
     * @param piece the piece which is moving
     * @return true if the move is valid
     */
    private static boolean validateBackwardsJump(Move move, BoardModel model, Piece piece) {
        if (piece.getType() != Piece.pieceType.KING) {
            return false;
        }
        Position start = move.getStart();
        Position end = move.getEnd();
        Space space = model.getSpacePendingMoves(end);
        if (!space.isValid()) {
            return false;
        }

        Position jumpedPos = new Position((start.getRow() + end.getRow()) / 2,
                (start.getCell() + end.getCell()) / 2);
        if (model.getSpacePendingMoves(jumpedPos).getPiece() != null &&
                model.getSpacePendingMoves(jumpedPos).getPiece().getColor() != piece.getColor()) { // if the jumped piece is on the other team
            return true;
        }
        return false;
    }

    /**
     * Determines if there is an available jump to make
     * @param model the boardModel which will be checked
     * @param teamColor the team that may have an available jump
     * @return true if jump is available
     */
    private static boolean isJumpAvailable(BoardModel model, Piece.color teamColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position position = new Position(r, c);
                Space space = model.getSpacePendingMoves(position);
                if (space.getPiece() != null && space.getPiece().getColor() == teamColor && pieceHasJump(model, position)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if one piece has an available move or jump
     * @param model the model to be checked
     * @param piecePos the position of the piece to be checked
     * @return true if the piece has a valid move or jump
     */
    private static boolean pieceHasMove(BoardModel model, Position piecePos) {
        Piece piece = model.getSpacePendingMoves(piecePos).getPiece();
        int teamMod = piece.getColor() == Piece.color.RED ? 1 : -1; // multiplier for row movement to determine forward direction
        int forwardRow = piecePos.getRow() + 1 * teamMod;
        int rightCell = piecePos.getCell() + 1;
        int leftCell = piecePos.getCell() - 1;
        boolean leftValid = leftCell >= 0 && leftCell < 8;
        boolean rightValid = rightCell >= 0 && rightCell < 8;

        if (forwardRow >= 0 && forwardRow < 8) {
            if (rightValid) {
                Move forwardRight = new Move(piecePos, new Position(forwardRow, rightCell));
                if (validateSimpleMove(forwardRight, model, piece)) {
                    return true;
                }
            }
            if (leftValid) {
                Move forwardLeft = new Move(piecePos, new Position(forwardRow, leftCell));
                if (validateSimpleMove(forwardLeft, model, piece)) {
                    return true;
                }
            }
        }
        int backRow = piecePos.getRow() - 1 * teamMod;
        if (backRow >= 0 && backRow < 8) {
            if (piece.getType() == Piece.pieceType.KING) {
                if (rightValid) {
                    Move backRight = new Move(piecePos, new Position(backRow, rightCell));
                    if (validateBackwardsMove(backRight, model, piece)) {
                        return true;
                    }
                }
                if (leftValid) {
                    Move backLeft = new Move(piecePos, new Position(backRow, leftCell));
                    if (validateBackwardsMove(backLeft, model, piece)) {
                        return true;
                    }
                }
            }
        }
        return pieceHasJump(model, piecePos);
    }

    //endregion

}
