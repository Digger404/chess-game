package game.chess;

import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @EnumSource(Piece.class)
    @ParameterizedTest
    void initialStatePieces(Piece piece) {
        Board initialState = Board.initialState();

        assertThat(initialState.getCoordinates(piece)).isEqualTo(piece.getInitialCoordinates());
    }

    @EnumSource(Piece.class)
    @ParameterizedTest
    void initialStateCoordinates(Piece piece) {
        Board initialState = Board.initialState();

        assertThat(piece.getInitialCoordinates())
                .allMatch(c -> initialState.findPiece(c).orElseThrow().equals(piece));
    }

    @Test
    void getPiece() {
        Assertions.assertThat(Board.initialState().findPiece(Coordinate.valueOf("e1"))).isPresent().get()
                .isEqualTo(Piece.WHITE_KING);
    }

    @Test
    void move() {
        Assertions.assertThat(Board.initialState()
                .move("a2a4").findPiece(Coordinate.valueOf("a4"))).isPresent()
                .get().isEqualTo(Piece.WHITE_PAWN);
    }

    @Test
    void capture() {
        assertThat(Board.initialState()
                .move("e2e4")
                .move("e7e5")
                .move("f2f4")
                .move("e5f4").getCoordinates(Piece.WHITE_PAWN))
                .hasSize(7);
    }

    @Test
    void testToString() {
        assertThat(Board.initialState()
                .move("a2a4")
                .toString()).isEqualTo(
                "8\tr\tn\tb\tq\tk\tb\tn\tr\n" +
                        "7\tp\tp\tp\tp\tp\tp\tp\tp\n" +
                        "6\t-\t*\t-\t*\t-\t*\t-\t*\n" +
                        "5\t*\t-\t*\t-\t*\t-\t*\t-\n" +
                        "4\tP\t*\t-\t*\t-\t*\t-\t*\n" +
                        "3\t*\t-\t*\t-\t*\t-\t*\t-\n" +
                        "2\t-\tP\tP\tP\tP\tP\tP\tP\n" +
                        "1\tR\tN\tB\tQ\tK\tB\tN\tR\n" +
                        "\n" +
                        "\ta\tb\tc\td\te\tf\tg\th");
    }

    @Test
    void getTurn() {

        Board initialState = Board.initialState();
        Board e2e4 = initialState.move("e2e4");
        Board e7a5 = e2e4.move("e7e5");

        Assertions.assertThat(initialState.getNextTurn()).isEqualTo(Piece.Color.WHITE);
        Assertions.assertThat(e2e4.getNextTurn()).isEqualTo(Piece.Color.BLACK);
        Assertions.assertThat(e7a5.getNextTurn()).isEqualTo(Piece.Color.WHITE);
    }

    @Test
    void getCoordinates() {
        assertThat(Board.initialState().move("b1a3").getCoordinates(Piece.WHITE_KNIGHT))
                .containsExactlyInAnyOrder(Coordinate.valueOf("a3"), Coordinate.valueOf("g1"));
    }

    @EnumSource(Piece.Color.class)
    @ParameterizedTest
    void getPieces(Piece.Color color) {
        Assertions.assertThat(Board.initialState().getPieces(color))
                .isEqualTo(Arrays.stream(Piece.values()).filter(piece -> piece.getColor().equals(color))
                        .collect(toMap(Function.identity(), Piece::getInitialCoordinates)));
    }
}