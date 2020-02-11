package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;

import java.util.List;

public interface MoveRule {
    List<Coordinate> walk(Coordinate from, Coordinate to);

    boolean isApplicable(Board board, Coordinate from, Coordinate to);

    default Board move(Board board, Coordinate from, Coordinate to) {
        return board.moveOnePiece(Move.of(from, to));
    }
}
