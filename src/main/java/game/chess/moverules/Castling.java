package game.chess.moverules;

import game.chess.CaptureService;
import game.chess.Figurine;
import game.chess.IntStreams;
import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static game.chess.moverules.MoveRules.isInitialState;

public enum Castling implements MoveRule {
    INSTANCE;

    @Component
    @RequiredArgsConstructor
    private static class ServicesInjector {
        private final CaptureService captureService;

        @PostConstruct
        public void postConstruct() {
            INSTANCE.captureService = captureService;
        }
    }

    private CaptureService captureService;

    private final static Map<Coordinate, Coordinate> LINK_ROOK = Map.of(
            Coordinate.valueOf("c1"), Coordinate.valueOf("a1"),
            Coordinate.valueOf("g1"), Coordinate.valueOf("h1"),
            Coordinate.valueOf("c8"), Coordinate.valueOf("a8"),
            Coordinate.valueOf("g8"), Coordinate.valueOf("h8"));

    private final static Map<Coordinate, Coordinate> LINK_ROOK_MOVE_COORDINATE = Map.of(
            Coordinate.valueOf("c1"), Coordinate.valueOf("d1"),
            Coordinate.valueOf("g1"), Coordinate.valueOf("f1"),
            Coordinate.valueOf("c8"), Coordinate.valueOf("d8"),
            Coordinate.valueOf("g8"), Coordinate.valueOf("f8"));

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        Coordinate linkRook = LINK_ROOK.get(to);

        return IntStreams.range(from.getZeroIndexColumn(), linkRook.getZeroIndexColumn())
                .skip(1)
                .mapToObj(column -> Coordinate.fromZeroIndex(column, from.getZeroIndexRow()))
                .collect(Collectors.toUnmodifiableList());
    }


    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        return !board.isInitialState() &&
                board.getPiece(from).getFigurine().equals(Figurine.KING) &&
                isInitialState(board, from) &&
                MoveRules.sameHorizontal(from, to) &&
                LINK_ROOK.containsKey(to) &&
                rookInInitialState(board, LINK_ROOK.get(to)) &&
                safeKingPath(board, from, to);
    }

    @Override
    public Board move(Board board, Coordinate from, Coordinate to) {
        return board.moveOnePiece(Move.of(from, to))
                .moveOnePiece(Move.of(LINK_ROOK.get(to), LINK_ROOK_MOVE_COORDINATE.get(to)));
    }

    private boolean rookInInitialState(Board board, Coordinate linkRook) {
        return board.findPiece(linkRook).filter(rook -> isInitialState(board, linkRook)).isPresent();
    }

    private boolean safeKingPath(Board board, Coordinate from, Coordinate to) {
        Piece.Color kingColor = board.getPiece(from).getColor();

        return IntStreams.rangeClosed(from.getZeroIndexColumn(), to.getZeroIndexColumn())
                .mapToObj(column -> Coordinate.fromZeroIndex(column, from.getZeroIndexRow()))
                .noneMatch(coordinate -> captureService.canCapture(board, kingColor.change(), coordinate));
    }
}
