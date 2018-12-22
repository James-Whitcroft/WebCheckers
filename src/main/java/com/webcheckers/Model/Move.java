package com.webcheckers.Model;

import java.awt.*;

/**
 * Stores information for a move
 */
public class Move {

    //region Attributes

    private final Position start;
    private final Position end;

    //endregion

    //region Constructor

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    //endregion

    //region Public Methods

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    //endregion
}
