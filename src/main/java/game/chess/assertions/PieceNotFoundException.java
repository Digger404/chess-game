package game.chess.assertions;

import game.chess.board.Coordinate;

public class PieceNotFoundException extends RuntimeException {
    public PieceNotFoundException(Coordinate from) {
        super("Piece not found:" + from);
    }
}
