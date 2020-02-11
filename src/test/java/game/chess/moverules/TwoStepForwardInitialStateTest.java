package game.chess.moverules;

import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwoStepForwardInitialStateTest {
    private final TwoStepForwardInitialState twoStepForwardInitialState = TwoStepForwardInitialState.INSTANCE;

    @Mock
    Board board;

    @Test
    void walk() {
        Coordinate to = Coordinate.valueOf("b4");
        Assertions.assertThat(twoStepForwardInitialState.walk(Coordinate.valueOf("b2"), to))
                .containsExactly(Coordinate.valueOf("b3"), to);
    }

    @Test
    void isApplicable() {
        Coordinate from = Coordinate.valueOf("b2");
        Coordinate to = Coordinate.valueOf("b4");

        when(board.getPiece(from)).thenReturn(Piece.WHITE_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.empty());
        when(board.historyIterator()).thenReturn(Collections.singleton(Board.initialState()).iterator());

        assertThat(twoStepForwardInitialState.isApplicable(board, from, to)).isTrue();
    }

    @Test
    void isNotApplicable() {

        Coordinate from = Coordinate.valueOf("b3");
        Coordinate to = Coordinate.valueOf("b5");

        when(board.getPiece(from)).thenReturn(Piece.WHITE_PAWN);
        when(board.findPiece(to)).thenReturn(Optional.empty());
        when(board.historyIterator()).thenReturn(Collections.singleton(Board.initialState()).iterator());

        assertThat(twoStepForwardInitialState.isApplicable(board, from, to)).isFalse();
    }
}