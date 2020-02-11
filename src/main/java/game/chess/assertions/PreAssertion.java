package game.chess.assertions;

import game.chess.board.Move;
import game.chess.game.State;

import java.util.Optional;

interface PreAssertion {
    Optional<RuntimeException> assertLegal(State lastState, Move move);

    int getOrder();
}
