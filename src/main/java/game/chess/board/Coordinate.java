package game.chess.board;

import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode
public class Coordinate {
    private final char column;
    private final int row;

    private Coordinate(char c, int row) {
        char column = Character.toLowerCase(c);
        Objects.checkIndex(row - 1, Board.SIZE);
        Objects.checkIndex(column - 'a', Board.SIZE);
        this.column = column;
        this.row = row;
    }

    public static Coordinate fromZeroIndex(int column, int row) {
        return Coordinate.create((char) ('a' + column), row + 1);
    }

    public int getZeroIndexColumn() {
        return column - 'a';
    }

    public int getZeroIndexRow() {
        return row - 1;
    }

    public static Coordinate create(char column, int row) {
        return new Coordinate(column, row);
    }

    public static Coordinate valueOf(String s) {
        assert s.length() == 2;
        return new Coordinate(s.charAt(0), Integer.parseInt(s.substring(1)));
    }

    @Override
    public String toString() {
        return String.format("%c%d", column, row);
    }

}
