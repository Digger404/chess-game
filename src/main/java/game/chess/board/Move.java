package game.chess.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class Move {
    private final Coordinate from;
    private final Coordinate to;

    public static Move valueOf(String move) {
        assert move.length() == 4;
        return new Move(Coordinate.create(move.charAt(0), Integer.parseInt(move.substring(1, 2))),
                Coordinate.create(move.charAt(2), Integer.parseInt(move.substring(3))));
    }

    @Override
    public String toString() {
        return String.format("%s%s", from, to);
    }
}
