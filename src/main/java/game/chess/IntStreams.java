package game.chess;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntStreams {

    public static IntStream rangeClosed(int startInclusive, int endInclusive) {
        if (startInclusive > endInclusive) {
            return IntStream.iterate(startInclusive, value -> value >= endInclusive, i -> i - 1);
        }
        return IntStream.rangeClosed(startInclusive, endInclusive);
    }

    public static IntStream range(int startInclusive, int endInclusive) {
        if (startInclusive > endInclusive) {
            return IntStream.iterate(startInclusive, value -> value > endInclusive, i -> i - 1);
        }
        return IntStream.range(startInclusive, endInclusive);
    }
}
