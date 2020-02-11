package game.chess.game;

import game.chess.board.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class State {
    private static final State INITIAL = State.builder().board(Board.initialState()).build();
    private final Board board;
    private final boolean check;
    private final boolean checkMate;

    @Override
    public String toString() {
        return (check ? "CHECK" : "") +
                (checkMate ? " MATE" : "") +
                "\n" + board.toString();
    }

    static State initialState() {
        return INITIAL;
    }
}