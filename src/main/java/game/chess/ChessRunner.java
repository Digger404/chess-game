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

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChessRunner implements CommandLineRunner, ExitCodeGenerator {
    private final GameFactory gameFactory;
    private final InputService inputService;

    @Getter
    private int exitCode;

    @Override
    public void run(String... args) {
        exitCode = call(args);
    }

    private int call(String[] args) {
        if (args.length != 1) {
            log.info("usage: chessapp moves_file_path");
            return ExitCode.ARGUMENT_ERROR.getCode();
        }

        return loadFileAndPlay(args[0]);
    }

    private int loadFileAndPlay(String filename) {
        return inputService.getPath(filename)
                .map(inputService::getMoves)
                .map(this::playGame)
                .orElseGet(() -> {
                    log.error("couldn't load the file: " + filename);
                    return ExitCode.FILE_ERROR.getCode();
                });
    }


    private int playGame(List<Move> moves) {

        Game game = gameFactory.newGame();

        return moves.stream()
                .map(game::move)
                .peek(result -> {
                    result.peek(state -> log.info(state.toString()));
                    result.peekLeft(error -> log.error(error.getMessage()));
                })
                .collect(toUnmodifiableList())
                .stream()
                .allMatch(Either::isRight) ? ExitCode.SUCCESS.getCode() : ExitCode.ILLEGAL_MOVE_ERROR.getCode();
    }
}
