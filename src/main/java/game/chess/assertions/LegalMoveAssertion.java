package game.chess.assertions;

import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Move;
import game.chess.game.State;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
class LegalMoveAssertion implements PreAssertion {
    private final int order = 2;

    @Override
    public Optional<RuntimeException> assertLegal(State lastState, Move move) {

        Board board = lastState.getBoard();
        Piece piece = board.getPiece(move.getFrom());

        if (!piece.legalMove(board, move.getFrom(), move.getTo())) {
            return Optional.of(new IllegalMoveException("Illegal move:" + move));
        }

        return Optional.empty();
    }
}
