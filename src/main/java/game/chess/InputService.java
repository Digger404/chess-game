package game.chess;

import game.chess.board.Move;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@Slf4j
@Service
class InputService {

    Optional<String> getPath(String file) {
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

    @SneakyThrows(FileNotFoundException.class)
    List<Move> getMoves(String path) {

        return new BufferedReader(new FileReader(path)).lines()
                .map(Move::valueOf)
                .collect(Collectors.toUnmodifiableList());
    }
}
