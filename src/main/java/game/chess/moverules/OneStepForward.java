package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;

import java.util.List;

public enum OneStepForward implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return List.of(to);
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return !MoveRules.isCapturing(board, to) &&
                MoveRules.sameVertical(from, to) &&
                MoveRules.stepsForwards(board, from, 1) == to.getZeroIndexRow();
    }
}
