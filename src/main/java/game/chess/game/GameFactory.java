package game.chess.game;

import game.chess.CheckService;
import game.chess.board.MoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameFactory {
    private final CheckService checkService;
    private final MoveService moveService;

    public Game newGame() {
        return Game.builder()
                .checkService(checkService)
                .moveService(moveService)
                .build();
    }
}
