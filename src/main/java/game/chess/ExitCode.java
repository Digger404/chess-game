package game.chess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExitCode {
    SUCCESS(0), ARGUMENT_ERROR(1), FILE_ERROR(2), ILLEGAL_MOVE_ERROR(3);
    private final int code;
}
