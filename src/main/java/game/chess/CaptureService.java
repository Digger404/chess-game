package game.chess;

import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CaptureService {

    public boolean canCapture(Board board, Piece.Color color, Coordinate coordinate) {
        return board.getPieces(color).entrySet().stream()
                .anyMatch(pieceEntry -> canCapture(board, coordinate, pieceEntry.getKey(), pieceEntry.getValue()));
    }

    boolean canCapture(Board board, Coordinate from, Coordinate to) {
        return board.findPiece(from)
                .filter(piece -> piece.legalMove(board, from, to))
                .isPresent();
    }

    boolean canCaptureByBlockable(Board board, Piece.Color color, Coordinate coordinate) {
        return Figurine.getBlockables().stream()
                .map(figurine -> Piece.getPiece(figurine, color))
                .anyMatch(piece -> canCapture(board, coordinate, piece, board.getCoordinates(piece)));
    }

    private boolean canCapture(Board board, Coordinate coordinate, Piece piece, Set<Coordinate> coordinates) {
        return coordinates.stream()
                .anyMatch(from -> piece.legalMove(board, from, coordinate));
    }

}
