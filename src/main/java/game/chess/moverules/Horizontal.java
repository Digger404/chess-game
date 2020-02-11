package game.chess.moverules;

import game.chess.IntStreams;
import game.chess.board.Board;
import game.chess.board.Coordinate;

import java.util.List;
import java.util.stream.Collectors;

import static game.chess.moverules.MoveRules.sameHorizontal;

public enum Horizontal implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return IntStreams.rangeClosed(from.getZeroIndexColumn(), to.getZeroIndexColumn()).skip(1)
                .mapToObj(column -> Coordinate.fromZeroIndex(column, from.getZeroIndexRow()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return sameHorizontal(from, to);
    }
}
