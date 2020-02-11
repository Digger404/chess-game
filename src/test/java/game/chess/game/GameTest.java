package game.chess.game;

import game.chess.CheckService;
import game.chess.Conditions;
import game.chess.board.Board;
import game.chess.board.Move;
import game.chess.board.MoveService;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GameTest {
    @Mock
    MoveService moveService;

    @Mock
    CheckService checkService;
    @InjectMocks
    Game game;

    @Test
    void move() {
        Move a2a4 = Move.valueOf("a2a4");

        when(moveService.move(eq(a2a4), any(State.class)))
                .thenAnswer(invocation -> Either.right(((State) invocation.getArgument(1)).getBoard().move(a2a4)));
        when(checkService.isCheck(any(Board.class))).thenReturn(false);

        assertThat(game.move(a2a4)).is(Conditions.newCondition(Either::isRight));
    }

    @Test
    void noMove() {
        Move a2a4 = Move.valueOf("a2a2");

        when(moveService.move(eq(a2a4), any(State.class)))
                .thenReturn(Either.left(new RuntimeException()));

        assertThat(game.move(a2a4)).is(Conditions.newCondition(Either::isLeft));
    }
}