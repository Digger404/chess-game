package game.chess.board;

import com.google.common.collect.Maps;
import game.chess.IntStreams;
import game.chess.Piece;
import game.chess.Streams;
import game.chess.assertions.PieceNotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class Board {
    static final int SIZE = 8;

    private static final Board INITIAL_STATE = new Board(
            Piece.Color.WHITE,
            Arrays.stream(Piece.values())
                    .collect(toMap(Function.identity(), Piece::getInitialCoordinates, Streams::throwingMerger, () -> Maps.newEnumMap(Piece.class))),
            Arrays.stream(Piece.values())
                    .collect(() -> new Piece[SIZE][SIZE],
                            (board, piece) -> piece.getInitialCoordinates()
                                    .forEach(coordinate -> board[coordinate.getZeroIndexRow()][coordinate.getZeroIndexColumn()] = piece),
                            Streams::throwingMerger));
    @Getter
    private final Piece.Color nextTurn;
    private final Map<Piece, Set<Coordinate>> pieces;
    private final Piece[][] board;

    private final Move move;
    private final Board previousState;

    private Board(Piece.Color nextTurn, Map<Piece, Set<Coordinate>> pieces, Piece[][] board) {
        this(nextTurn, pieces, board, null, null);
    }

    private Board(Piece.Color nextTurn, Map<Piece, Set<Coordinate>> pieces, Piece[][] board, Board previousState, Move move) {
        this.nextTurn = nextTurn;
        this.pieces = pieces;
        this.board = board;
        this.previousState = previousState;
        this.move = move;
    }

    public boolean isInitialState() {
        return Objects.isNull(move);
    }

    public Move getLastMove() {
        return Optional.ofNullable(move)
                .orElseThrow(NoSuchElementException::new);
    }

    public Board getPreviousState() {
        return Optional.ofNullable(previousState)
                .orElseThrow(NoSuchElementException::new);
    }

    public Iterator<Board> historyIterator() {
        return BoardHistoryIterator.create(this);
    }

    public static Board initialState() {
        return INITIAL_STATE;
    }

    public Optional<Piece> findPiece(Coordinate c) {
        return Optional.ofNullable(board[c.getZeroIndexRow()][c.getZeroIndexColumn()]);
    }

    public Piece getPiece(Coordinate c) {
        return findPiece(c).orElseThrow(() -> new PieceNotFoundException(c));
    }

    public Set<Coordinate> getCoordinates(Piece piece) {
        return Optional.ofNullable(pieces.get(piece)).orElse(Collections.emptySet());
    }

    public Board move(String move) {
        return move(Move.valueOf(move));
    }

    public Board move(Move move) {
        return getPiece(move.getFrom())
                .move(this, move.getFrom(), move.getTo());
    }

    public Board moveOnePiece(Move move) {
        return new Board(
                nextTurn.change(),
                movePieces(move),
                moveArray(move),
                this,
                move);
    }

    public Map<Piece, Set<Coordinate>> getPieces(Piece.Color color) {
        return pieces.entrySet().stream()
                .filter(entry -> entry.getKey().getColor().equals(color))
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Piece, Set<Coordinate>> movePieces(Move move) {

        Map<Piece, Set<Coordinate>> newPieces = Maps.newEnumMap(pieces);

        findPiece(move.getTo()).ifPresent(capturedPiece -> remove(newPieces, capturedPiece, move.getTo()));
        move(newPieces, getPiece(move.getFrom()), move);

        return Collections.unmodifiableMap(newPieces);
    }

    private Piece[][] moveArray(Move move) {

        Piece[][] newState = Arrays.stream(board).map(Piece[]::clone).toArray(Piece[][]::new);

        int fromRow = move.getFrom().getZeroIndexRow();
        int toRow = move.getTo().getZeroIndexRow();
        int toColumn = move.getTo().getZeroIndexColumn();
        int fromColumn = move.getFrom().getZeroIndexColumn();

        newState[toRow][toColumn] = newState[fromRow][fromColumn];
        newState[fromRow][fromColumn] = null;

        return newState;
    }

    private void move(Map<Piece, Set<Coordinate>> pieces, Piece piece, Move move) {

        pieces.compute(piece, (p, coordinates) -> Stream.concat(
                Stream.of(move.getTo()),
                filter(requireNonNull(coordinates), move.getFrom()))
                .collect(toUnmodifiableSet()));
    }

    private void remove(Map<Piece, Set<Coordinate>> pieces, Piece capturedPiece, Coordinate to) {

        Set<Coordinate> coordinates = filter(pieces.get(capturedPiece), to).collect(toUnmodifiableSet());

        if (coordinates.isEmpty()) {
            pieces.remove(capturedPiece);
        } else {
            pieces.replace(capturedPiece, coordinates);
        }
    }

    private Stream<Coordinate> filter(Set<Coordinate> coordinates, Coordinate coordinate) {
        return coordinates.stream().filter(c -> !coordinate.equals(c));
    }

    @Override
    public String toString() {
        return Stream.concat(IntStreams.rangeClosed(SIZE - 1, 0)
                        .mapToObj(r -> (r + 1) + "\t" + IntStream.range(0, SIZE)
                                .mapToObj(c -> Optional.ofNullable(board[r][c])
                                        .map(Piece::getCode)
                                        .orElseGet(() -> (c + r) % 2 == 0 ? Piece.Color.BLACK.getCode() : Piece.Color.WHITE.getCode()))
                                .map(String::valueOf)
                                .collect(joining("\t"))),
                Stream.of(IntStream.range(0, SIZE).mapToObj(value -> (char) ('a' + value)).map(String::valueOf).collect(joining("\t", "\n\t", ""))))
                .collect(joining("\n"));
    }
}
