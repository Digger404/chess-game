package game.chess.game;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import game.chess.CheckService;
import game.chess.board.Board;
import game.chess.board.Move;
import game.chess.board.MoveService;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Game {
    private final CheckService checkService;
    private final MoveService moveService;

    @Getter
    private final List<State> history = Lists.newArrayList(State.initialState());

    public Either<RuntimeException, State> move(Move move) {
        State lastState = getLastState();

        return moveService.move(move, lastState)
                .flatMap(board -> checkService.assertNotKingInCheckPosition(board, lastState.isCheck())
                        .<Either<RuntimeException, Board>>map(Either::left)
                        .orElseGet(() -> Either.right(board)))
                .map(this::newState);
    }

    private State newState(Board board) {
        boolean check = checkService.isCheck(board);
        State newState = State.builder()
                .board(board)
                .check(check)
                .checkMate(check && checkService.isCheckMate(board))
                .build();

        history.add(newState);

        return newState;
    }

    private State getLastState() {
        return Iterables.getLast(history);
    }

    public String currentState() {
        return getLastState().toString();
    }
}
