package game.chess;

import org.assertj.core.api.Condition;

import java.util.function.Predicate;

public class Conditions {

    public static <T> Condition<T> newCondition(Predicate<T> predicate) {
        return new Condition<>(predicate, null);
    }
}
