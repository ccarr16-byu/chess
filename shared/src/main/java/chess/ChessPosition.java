package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(ChessPosition copy) {
        this.row = copy.getRow();
        this.col = copy.getColumn();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public String toString() {
        return String.format("[ChessPosition] row: %d, col: %d\n", this.getRow(), this.getColumn());
    }

    @Override
    public int hashCode() {
        return (this.row * 10) + this.col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessPosition obj2 = (ChessPosition)obj;
        return obj2.getRow() == this.getRow() && obj2.getColumn() == this.getColumn();
    }
}
