package game.chess.moverules;

import game.chess.IntStreams;
import game.chess.board.Board;
import game.chess.board.Coordinate;

import java.util.List;
import java.util.stream.Collectors;

public enum Vertical implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return IntStreams.rangeClosed(from.getZeroIndexRow(), to.getZeroIndexRow()).skip(1)
                .mapToObj(row -> Coordinate.fromZeroIndex(from.getZeroIndexColumn(), row))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return MoveRules.sameVertical(from, to);
    }
}
