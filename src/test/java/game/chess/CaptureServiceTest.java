package game.chess;

import com.google.common.collect.Iterables;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CaptureServiceTest {

    private final CaptureService captureService = new CaptureService();

    @Test
    void canCapture() {
        assertThat(captureService.canCapture(
                Board.initialState(),
                Piece.Color.WHITE,
                Coordinate.valueOf("c3")))
                .isTrue();
    }

    @Test
    void canNotCapture() {
        assertThat(captureService.canCapture(
                Board.initialState(),
                Piece.Color.WHITE,
                Iterables.getOnlyElement(Piece.BLACK_KING.getInitialCoordinates())))
                .isFalse();
    }

    @Test
    void canNotCaptureByBlockable() {
        assertThat(captureService.canCaptureByBlockable(
                Board.initialState(),
                Piece.Color.WHITE,
                Iterables.getOnlyElement(Piece.BLACK_KING.getInitialCoordinates())))
                .isFalse();
    }

    @Test
    void canCaptureByBlockable() {
        assertThat(captureService.canCaptureByBlockable(
                Board.initialState().move(Move.valueOf("e2e4")),
                Piece.Color.WHITE,
                Coordinate.valueOf("c4")))
                .isTrue();
    }
}