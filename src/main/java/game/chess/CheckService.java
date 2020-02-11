package game.chess;

import com.google.common.collect.Iterables;
import game.chess.assertions.IllegalMoveException;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;
import io.vavr.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CheckService {
    private final CaptureService captureService;

    public Optional<RuntimeException> assertNotKingInCheckPosition(Board board, boolean wasKingInCheck) {
        if (isKingInCheckPosition(board, wasKingInCheck)) {
            return Optional.of(new IllegalMoveException("King is in a check position"));
        }

        return Optional.empty();
    }

    private boolean isKingInCheckPosition(Board board, boolean wasKingInCheck) {

        Coordinate lastMove = board.getLastMove().getTo();
        Piece.Color color = board.getPiece(lastMove).getColor();

        Coordinate kingCoordinate = getKingCoordinate(board, color);

        if (shouldAllPiecesBeTested(wasKingInCheck, lastMove, kingCoordinate)) {
            return captureService.canCapture(board, color.change(), kingCoordinate);
        }
        return captureService.canCaptureByBlockable(board, color.change(), kingCoordinate);
    }

    private boolean shouldAllPiecesBeTested(boolean wasKingInCheck, Coordinate to, Coordinate kingCoordinate) {
        return wasKingInCheck || isKingMove(to, kingCoordinate);
    }


    public boolean isCheck(Board nextBoardState) {

        Coordinate lastMove = nextBoardState.getLastMove().getTo();
        Piece.Color currentColor = nextBoardState.getPiece(lastMove).getColor();

        Coordinate kingCoordinates = Iterables.getOnlyElement(nextBoardState.getCoordinates(Piece.getPiece(Figurine.KING, currentColor.change())));

        return captureService.canCapture(nextBoardState, lastMove, kingCoordinates)
                || captureService.canCaptureByBlockable(nextBoardState, currentColor, kingCoordinates);
    }


    private boolean isKingMove(Coordinate to, Coordinate kingCoordinate) {
        return kingCoordinate.equals(to);
    }

    private Coordinate getKingCoordinate(Board board, Piece.Color color) {
        return Iterables.getOnlyElement(board.getCoordinates(Piece.getPiece(Figurine.KING, color)));
    }

    public boolean isCheckMate(Board board) {
        Piece.Color turn = board.getNextTurn();

        Piece king = Piece.getPiece(Figurine.KING, turn);
        Coordinate kingCoordinate = Iterables.getOnlyElement(board.getCoordinates(king));

        return !canMoveKing(board, king, kingCoordinate) && !canBeBlocked(board, turn, kingCoordinate);
    }

    private boolean canBeBlocked(Board board, Piece.Color turn, Coordinate kingCoordinate) {
        List<Coordinate> attackingCoordinates = attackingPieces(board, turn.change(), kingCoordinate);

        return attackingCoordinates.size() == 1 &&
                canBeBlocked(board, turn, getAttackingPath(board, kingCoordinate, Iterables.getOnlyElement(attackingCoordinates)));
    }

    private Set<Coordinate> getAttackingPath(Board board, Coordinate kingCoordinate, Coordinate attackingPiece) {
        return Stream.concat(Stream.of(attackingPiece), board.getPiece(attackingPiece)
                .walk(board, attackingPiece, kingCoordinate)
                .stream()
                .filter(coordinate -> !kingCoordinate.equals(coordinate)))
                .collect(Collectors.toUnmodifiableSet());
    }

    private boolean canBeBlocked(Board board, Piece.Color turn, Set<Coordinate> attackingPath) {
        return board.getPieces(turn).entrySet().stream()
                .anyMatch(pieceEntry -> attackingPath.stream()
                        .anyMatch(to -> canBlockCheckMate(board, pieceEntry.getValue(), pieceEntry.getKey(), to)));
    }

    private boolean canBlockCheckMate(Board board, Set<Coordinate> coordinates, Piece piece, Coordinate to) {
        return legalMovesTo(board, coordinates, piece, to)
                .anyMatch(from -> !isKingInCheckPosition(board.move(Move.of(from, to)), true));
    }

    private Stream<Coordinate> legalMovesTo(Board board, Set<Coordinate> coordinates, Piece piece, Coordinate to) {
        return coordinates.stream()
                .filter(from -> piece.legalMove(board, from, to));
    }

    private List<Coordinate> attackingPieces(Board board, Piece.Color turn, Coordinate kingCoordinate) {
        return board.getPieces(turn.change()).entrySet().stream()
                .flatMap(pieceEntry -> legalMovesTo(board, pieceEntry.getValue(), pieceEntry.getKey(), kingCoordinate))
                .collect(Collectors.toUnmodifiableList());
    }

    private boolean canMoveKing(Board board, Piece king, Coordinate kingCoordinate) {
        return getKingMoves(kingCoordinate)
                .filter(to -> king.legalMove(board, kingCoordinate, to))
                .anyMatch(to -> !isKingInCheckPosition(board.move(Move.of(kingCoordinate, to)), true));
    }

    private Stream<Coordinate> getKingMoves(Coordinate kingCoordinate) {
        return IntStream.range(0, 9)
                .mapToObj(i -> Tuple.of((i / 3) - 1 + kingCoordinate.getZeroIndexColumn(), ((i % 3) - 1) + kingCoordinate.getZeroIndexRow()))
                .filter(coordinate -> coordinate._1 >= 0 && coordinate._1 < 8)
                .filter(coordinate -> coordinate._2 >= 0 && coordinate._2 < 8)
                .map(coordinate -> Coordinate.fromZeroIndex(coordinate._1, coordinate._2))
                .filter(coordinate -> !kingCoordinate.equals(coordinate));
    }
}
