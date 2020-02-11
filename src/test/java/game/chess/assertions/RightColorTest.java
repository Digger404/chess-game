package game.chess.assertions;

import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Move;
import game.chess.game.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RightColorTest {
    private final RightColor rightColor = new RightColor();

    @Mock
    State state;
    @Mock
    Board board;

    @Test
    void assertLegal() {
        Move b2c3 = Move.valueOf("b2c3");
        when(state.getBoard()).thenReturn(board);
        when(board.getPiece(b2c3.getFrom())).thenReturn(Piece.WHITE_PAWN);
        when(board.getNextTurn()).thenReturn(Piece.Color.WHITE);

        assertThat(rightColor.assertLegal(state, b2c3)).isEmpty();
    }

    @Test
    void assertIllegal() {
        Move b2c3 = Move.valueOf("b2c3");
        when(state.getBoard()).thenReturn(board);
        when(board.getPiece(b2c3.getFrom())).thenReturn(Piece.WHITE_PAWN);
        when(board.getNextTurn()).thenReturn(Piece.Color.BLACK);

        assertThat(rightColor.assertLegal(state, b2c3))
                .containsInstanceOf(IncorrectTurnException.class);
    }
}