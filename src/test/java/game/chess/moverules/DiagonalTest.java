package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DiagonalTest {
    private final Diagonal diagonal = Diagonal.INSTANCE;

    @Mock
    Board board;

    @Test
    void walkA1H8() {

        Coordinate from = Coordinate.valueOf("a1");
        Coordinate to = Coordinate.valueOf("h8");

        Assertions.assertThat(diagonal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(1, 8).mapToObj(i -> Coordinate.create((char) ('a' + i), 1 + i))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void walkA8H1() {

        Coordinate from = Coordinate.valueOf("a8");
        Coordinate to = Coordinate.valueOf("h1");

        Assertions.assertThat(diagonal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(1, 8).mapToObj(i -> Coordinate.create((char) ('a' + i), 8 - i))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void walkH8A1() {

        Coordinate from = Coordinate.valueOf("h8");
        Coordinate to = Coordinate.valueOf("a1");

        Assertions.assertThat(diagonal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(1, 8).mapToObj(i -> Coordinate.create((char) ('h' - i), 8 - i))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void walkH1A8() {

        Coordinate from = Coordinate.valueOf("h1");
        Coordinate to = Coordinate.valueOf("a8");

        Assertions.assertThat(diagonal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(1, 8).mapToObj(i -> Coordinate.create((char) ('h' - i), 1 + i))
                        .collect(Collectors.toUnmodifiableList()));
    }


    @Test
    void isApplicable() {

        Coordinate from = Coordinate.valueOf("a3");
        Coordinate to = Coordinate.valueOf("b4");

        Assertions.assertThat(diagonal.isApplicable(board, from, to)).isTrue();
    }

    @Test
    void isNotApplicable() {

        Coordinate from = Coordinate.valueOf("e3");
        Coordinate to = Coordinate.valueOf("b4");

        Assertions.assertThat(diagonal.isApplicable(board, from, to)).isFalse();

    }
}