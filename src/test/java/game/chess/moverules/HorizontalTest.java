package game.chess.moverules;

import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class HorizontalTest {
    private final Horizontal horizontal = Horizontal.INSTANCE;

    @Mock
    Board board;

    @Test
    void walkA3H3() {

        Coordinate from = Coordinate.valueOf("a3");
        Coordinate to = Coordinate.valueOf("h3");

        Assertions.assertThat(horizontal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range(1, 8)
                        .mapToObj(i -> Coordinate.create((char) ('a' + i), 3))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void walkG4B4() {

        Coordinate from = Coordinate.valueOf("g4");
        Coordinate to = Coordinate.valueOf("b4");

        Assertions.assertThat(horizontal.walk(from, to))
                .containsExactlyElementsOf(IntStream.range('b' - 'a', 'g' - 'a')
                        .mapToObj(i -> Coordinate.create((char) ('g' - i), 4))
                        .collect(Collectors.toUnmodifiableList()));
    }

    @Test
    void isApplicable() {
        assertThat(horizontal.isApplicable(board, Coordinate.valueOf("b1"), Coordinate.valueOf("c1")))
                .isTrue();
    }

    @Test
    void isNotApplicable() {
        assertThat(horizontal.isApplicable(board, Coordinate.valueOf("a1"), Coordinate.valueOf("c3")))
                .isFalse();
    }
}