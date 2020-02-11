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
class CapturingOneStepForwardDiagonalTest {
    private final CapturingOneStepForwardDiagonal capturingOneStepForwardDiagonal = CapturingOneStepForwardDiagonal.INSTANCE;

    @Mock
    Board board;

    @Test
    void walk() {

        Coordinate to = Coordinate.valueOf("b3");

        Assertions.assertThat(capturingOneStepForwardDiagonal.walk(Coordinate.create('a', 2), to))
                .containsExactly(to);
    }

    @Test
    void isApplicableWhitePawn() {

        Coordinate to = Coordinate.valueOf("b3");
        Coordinate from = Coordinate.valueOf("a2");

        when(board.getPiece(from)).thenReturn(Piece.WHITE_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.of(Piece.BLACK_PAWN));

        assertThat(capturingOneStepForwardDiagonal.isApplicable(board, from, to)).isTrue();
    }

    @Test
    void isApplicableBlackPawn() {

        Coordinate to = Coordinate.valueOf("f3");
        Coordinate from = Coordinate.valueOf("e4");

        when(board.getPiece(from)).thenReturn(Piece.BLACK_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.of(Piece.WHITE_BISHOP));

        assertThat(capturingOneStepForwardDiagonal.isApplicable(board, from, to)).isTrue();
    }

    @Test
    void isNotCapturing() {

        Coordinate to = Coordinate.valueOf("f3");
        Coordinate from = Coordinate.valueOf("e4");

        when(board.findPiece(to)).thenReturn(Optional.empty());

        assertThat(capturingOneStepForwardDiagonal.isApplicable(board, from, to)).isFalse();
    }

    @Test
    void isNotOneStepY() {

        Coordinate to = Coordinate.valueOf("f3");
        Coordinate from = Coordinate.valueOf("e3");

        when(board.getPiece(from)).thenReturn(Piece.BLACK_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.of(Piece.WHITE_BISHOP));

        assertThat(capturingOneStepForwardDiagonal.isApplicable(board, from, to)).isFalse();
    }

    @Test
    void isNotOneStepX() {

        Coordinate to = Coordinate.valueOf("f3");
        Coordinate from = Coordinate.valueOf("a4");

        when(board.getPiece(from)).thenReturn(Piece.BLACK_BISHOP);
        when(board.findPiece(to)).thenReturn(Optional.of(Piece.WHITE_PAWN));

        assertThat(capturingOneStepForwardDiagonal.isApplicable(board, from, to)).isFalse();
    }

}