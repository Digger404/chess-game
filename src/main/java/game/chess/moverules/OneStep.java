package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;

import java.util.List;

public enum OneStep implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return List.of(to);
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return !to.equals(from) &&
                Math.abs(from.getZeroIndexColumn() - to.getZeroIndexColumn()) <= 1 &&
                Math.abs(from.getZeroIndexRow() - to.getZeroIndexRow()) <= 1;
    }
}
