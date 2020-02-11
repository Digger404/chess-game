package game.chess;

import game.chess.board.Move;
import game.chess.game.Game;
import game.chess.game.GameFactory;
import io.vavr.control.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChessRunner implements CommandLineRunner, ExitCodeGenerator {
    private final GameFactory gameFactory;
    private final InputService inputService;

    private final Map<Integer, Function<String[], Integer>> modes =
            Map.of(0, args -> interactive(),
                    1, args -> file(args[0]));


    @Getter
    private int exitCode;

    @Override
    public void run(String... args) {
        exitCode = call(args);
    }

    private int call(String[] args) {
        return Optional.ofNullable(modes.get(args.length).apply(args))
                .orElseGet(() -> {
                    log.info("usage: chessapp [moves_file_path]");
                    return ExitCode.ARGUMENT_ERROR.getCode();
                });
    }


    private int playGame(Stream<Move> moves) {
        Game game = gameFactory.newGame();
        log.info(game.currentState());

        return moves
                .map(game::move)
                .peek(result -> {
                    result.peek(state -> log.info(state.toString()));
                    result.peekLeft(error -> log.error(error.getMessage()));
                })
                .collect(toUnmodifiableList())
                .stream()
                .allMatch(Either::isRight) ? ExitCode.SUCCESS.getCode() : ExitCode.ILLEGAL_MOVE_ERROR.getCode();
    }


    private Integer file(String arg) {
        return inputService.getMoves(arg)
                .map(this::playGame)
                .orElseGet(() -> {
                    log.error("couldn't load the file: " + arg);
                    return ExitCode.FILE_ERROR.getCode();
                });
    }

    private int interactive() {
        return playGame(inputService.streamInput());
    }
}
