package game.chess.moverules;

import game.chess.IntStreams;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class VerticalTest {
    private final Vertical vertical = Vertical.INSTANCE;

    @Mock
    Board board;

    @Test
    void walkA3A7() {

        Coordinate from = Coordinate.valueOf("a3");
        Coordinate to = Coordinate.valueOf("a7");

        Assertions.assertThat(vertical.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(4, 8)
                        .mapToObj(i -> Coordinate.create('a', i))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void walkG4G6() {

        Coordinate from = Coordinate.valueOf("g4");
        Coordinate to = Coordinate.valueOf("g6");

        Assertions.assertThat(vertical.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(5, 7)
                        .mapToObj(i -> Coordinate.create('g', i))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void isApplicable() {
        assertThat(vertical.isApplicable(board, Coordinate.valueOf("b1"), Coordinate.valueOf("b5")))
                .isTrue();
    }

    @Test
    void isNotApplicable() {
        assertThat(vertical.isApplicable(board, Coordinate.valueOf("a1"), Coordinate.valueOf("c3")))
                .isFalse();
    }

    @Test
    void walk() {
    }

    @Test
    void possibleMoves() {
        Coordinate from = Coordinate.valueOf("d5");
        System.out.println(Board.initialState());
        IntStreams.rangeClosed(0, from.getZeroIndexRow() + from.getZeroIndexColumn());

    }
}