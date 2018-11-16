package com.webcheckers.Model;

import java.util.Objects;

public class Position {

    //region Attributes

    private int row;
    private int cell;

    //endregion

    //region Constructor

    public Position(int row, int cell) {
        if ( row > 7 || row < 0 || cell > 7 || cell < 0) {
            throw new IllegalArgumentException("row and cell values must be between 0 and 7 inclusive");
        }

        this.row = row;
        this.cell = cell;
    }

    //endregion

    //region Public Methods

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return this.row == ((Position) obj).row && this.cell == ((Position) obj).cell;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, cell);
    }

    //endregion
}
