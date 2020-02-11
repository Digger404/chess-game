package game.chess.moverules;

import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OneStepForwardTest {
    private final OneStepForward oneStepForward = OneStepForward.INSTANCE;

    @Mock
    Board board;

    @Test
    void walk() {
        Coordinate to = Coordinate.valueOf("c2");
        Assertions.assertThat(oneStepForward.walk(Coordinate.valueOf("b2"), to)).containsExactly(to);
    }

    @Test
    void isApplicableWhitePawn() {

        Coordinate from = Coordinate.valueOf("d3");
        Coordinate to = Coordinate.valueOf("d4");

        when(board.getPiece(from)).thenReturn(Piece.WHITE_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.empty());

        assertThat(oneStepForward.isApplicable(board, from, to)).isTrue();
    }

    @Test
    void isApplicableBlackPawn() {

        Coordinate from = Coordinate.valueOf("d4");
        Coordinate to = Coordinate.valueOf("d3");

        when(board.getPiece(from)).thenReturn(Piece.BLACK_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.empty());

        assertThat(oneStepForward.isApplicable(board, from, to)).isTrue();
    }


    @Test
    void isNotApplicable() {

        Coordinate from = Coordinate.valueOf("d4");
        Coordinate to = Coordinate.valueOf("d2");

        when(board.getPiece(from)).thenReturn(Piece.BLACK_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.empty());

        assertThat(oneStepForward.isApplicable(board, from, to)).isFalse();
    }
}