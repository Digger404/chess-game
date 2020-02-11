package game.chess.assertions;

class IncorrectTurnException extends RuntimeException {
    IncorrectTurnException(String message) {
        super(message);
    }
}
