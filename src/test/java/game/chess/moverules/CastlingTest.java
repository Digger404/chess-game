package game.chess.moverules;

import game.chess.CheckService;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = Castling.class)
@SpringBootTest
class CastlingTest {
    @SpyBean
    CheckService checkService;

    private final Castling castling = Castling.INSTANCE;

    @Test
    void walk() {
        Assertions.assertThat(castling.walk(Coordinate.valueOf("e1"), Coordinate.valueOf("c1")))
                .containsExactlyInAnyOrder(
                        Coordinate.valueOf("b1"),
                        Coordinate.valueOf("c1"),
                        Coordinate.valueOf("d1"));
    }

    @Test
    void isApplicable() {
        Assertions.assertThat(castling.isApplicable(Board.initialState()
                        .move("e2e4")
                        .move("e7e5")
                        .move("f1c4")
                        .move("g8f6"),
                Coordinate.valueOf("e1"), Coordinate.valueOf("g1")))
                .isTrue();
    }

    @Test
    void noSafePath() {
        Assertions.assertThat(castling.isApplicable(Board.initialState()
                        .move("e2e4")
                        .move("e7e5")
                        .move("f1c4")
                        .move("b8c6")
                        .move("g1h3")
                        .move("f8c5")
                        .move("f2f4")
                        .move("f7f5"),
                Coordinate.valueOf("e1"), Coordinate.valueOf("g1")))
                .isFalse();
    }

    @Test
    void noTowerInitialState() {
        Assertions.assertThat(castling.isApplicable(Board.initialState()
                        .move("e2e4")
                        .move("e7e5")
                        .move("f1c4")
                        .move("b8c6")
                        .move("g1h3")
                        .move("f8c5")
                        .move("h1g1")
                        .move("f7f5")
                        .move("g1h1")
                        .move("f5f4"),
                Coordinate.valueOf("e1"), Coordinate.valueOf("g1")))
                .isFalse();
    }

    @Test
    void noKingInitialState() {
        Assertions.assertThat(castling.isApplicable(Board.initialState()
                        .move("e2e4")
                        .move("e7e5")
                        .move("f1c4")
                        .move("b8c6")
                        .move("g1h3")
                        .move("f8c5")
                        .move("e1f1")
                        .move("f7f5")
                        .move("f1e1")
                        .move("f5f4"),
                Coordinate.valueOf("e1"), Coordinate.valueOf("g1")))
                .isFalse();
    }

}