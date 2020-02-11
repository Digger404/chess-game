package game.chess;

import game.chess.board.Move;
import io.vavr.collection.Iterator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@Slf4j
@Service
class InputService {

    Stream<Move> streamInput() {
        Scanner in = new Scanner(System.in);
        return Streams.sequentialStream(new Iterator<Move>() {
            @Override
            public boolean hasNext() {
                return in.hasNext();
            }

            @Override
            public Move next() {
                String move = in.next();
                try {
                    return Move.valueOf(move);

                } catch (Exception e) {
                    log.warn("Not valid move:" + move);
                }
                return null;
            }
        }).filter(Objects::nonNull);
    }

    Optional<Stream<Move>> getMoves(String path) {
        return getPath(path)
                .map(this::mapLines);
    }

    @SneakyThrows(FileNotFoundException.class)
    private Stream<Move> mapLines(String p) {
        return new BufferedReader(new FileReader(p)).lines()
                .map(Move::valueOf);
    }

    private Optional<String> getPath(String file) {
        try {
            return Optional.of(ResourceUtils.getFile(CLASSPATH_URL_PREFIX + file).getAbsolutePath());
        } catch (FileNotFoundException e) {
            try {
                return Optional.of(ResourceUtils.getFile(file))
                        .filter(File::exists)
                        .map(File::getAbsolutePath);
            } catch (FileNotFoundException ex) {
                return Optional.empty();
            }
        }
    }
}
