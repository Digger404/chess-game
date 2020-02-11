package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;

import java.util.List;

import static game.chess.moverules.MoveRules.isCapturing;
import static game.chess.moverules.MoveRules.isOneStepForwardDiagonal;

public enum CapturingOneStepForwardDiagonal implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return List.of(to);
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return isCapturing(board, to) &&
                isOneStepForwardDiagonal(board,from,to);
    }
}
