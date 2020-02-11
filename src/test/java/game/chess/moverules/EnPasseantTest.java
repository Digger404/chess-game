package game.chess.moverules;

import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnPasseantTest {

    @Mock
    Board board;

    private final EnPasseant enPasseant = EnPasseant.INSTANCE;

    @Test
    void walk() {
        Coordinate to = Coordinate.valueOf("b6");

        Assertions.assertThat(enPasseant.walk(Coordinate.valueOf("a5"), to)).isEqualTo(List.of(to));
    }

    @Test
    void isApplicable() {
        when(board.getLastMove()).thenReturn(Move.valueOf("b7b5"));
        when(board.getPiece(Coordinate.valueOf("b5"))).thenReturn(Piece.BLACK_PAWN);
        when(board.getPiece(Coordinate.valueOf("a5"))).thenReturn(Piece.WHITE_PAWN);

        assertThat(enPasseant.isApplicable(board, Coordinate.valueOf("a5"), Coordinate.valueOf("b6")))
                .isTrue();
    }

    @Test
    void move() {
        when(board.moveOnePiece(Move.valueOf("a5b5"))).thenReturn(board);
        when(board.moveOnePiece(Move.valueOf("b5b6"))).thenReturn(board);

        Assertions.assertThat(enPasseant.move(board, Coordinate.valueOf("a5"), Coordinate.valueOf("b6")))
                .isEqualTo(board);
    }
}