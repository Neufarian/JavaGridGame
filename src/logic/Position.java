/**
 * Utility class that represents a coordinate on the game board.
 * Used for tracking figure positions and movement calculations.
 */

package logic;
public class Position {

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    };

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

    public Position getPosition(Direction direction) {
        switch (direction) {
            case UP: return new Position(row - 1, column);
            case DOWN: return new Position(row + 1, column);
            case LEFT: return new Position(row, column - 1);
            case RIGHT: return new Position(row, column + 1);
            default: return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || !(obj instanceof Position)) return false;
        Position p = (Position)obj;
        return this.row == p.row && this.column == p.column;
    }
    
}
