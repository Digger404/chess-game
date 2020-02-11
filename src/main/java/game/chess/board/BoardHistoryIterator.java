package game.chess.board;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.Objects;

@AllArgsConstructor(staticName = "create")
class BoardHistoryIterator implements Iterator<Board> {
    private Board board;

    @Override
    public boolean hasNext() {
        return Objects.nonNull(board);
    }

    @Override
    public Board next() {
        Board next = board;

        board = board.isInitialState() ? null : board.getPreviousState();

        return next;
    }
}
