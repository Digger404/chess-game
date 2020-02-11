package game.chess.board;

import game.chess.assertions.PreAssertionService;
import game.chess.game.State;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveService {
    private final PreAssertionService preAssertionService;

    public Either<RuntimeException, Board> move(Move move, State lastState) {
        return preAssertionService.assertLegal(lastState, move)
                .<Either<RuntimeException, Board>>map(Either::left)
                .orElseGet(() -> Either.right(lastState.getBoard().move(move)));
    }
}
