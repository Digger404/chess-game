package game.chess.assertions;

import game.chess.Piece;
import game.chess.board.Move;
import game.chess.game.State;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
public class RightColor implements PreAssertion {
    private final int order = 1;

    @Override
    public Optional<RuntimeException> assertLegal(State lastState, Move move) {

        Piece piece = lastState.getBoard().getPiece(move.getFrom());

        if (!piece.getColor().equals(lastState.getBoard().getNextTurn())) {
            return Optional.of(new IncorrectTurnException("Incorrect color:" + piece.getColor()));
        }

        return Optional.empty();
    }
}
