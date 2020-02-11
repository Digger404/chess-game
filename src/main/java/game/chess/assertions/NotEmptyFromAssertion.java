package game.chess.assertions;

import game.chess.Piece;
import game.chess.board.Move;
import game.chess.game.State;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
public class NotEmptyFromAssertion implements PreAssertion {
    private final int order = 0;

    @Override
    public Optional<RuntimeException> assertLegal(State lastState, Move move) {

        Optional<Piece> piece = lastState.getBoard().findPiece(move.getFrom());

        if (piece.isEmpty()) {
            return Optional.of(new PieceNotFoundException(move.getFrom()));
        }

        return Optional.empty();
    }
}
