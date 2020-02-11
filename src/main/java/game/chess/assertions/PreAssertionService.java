package game.chess.assertions;

import game.chess.board.Move;
import game.chess.game.State;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

@Service
public class PreAssertionService {
    private final List<PreAssertion> preAssertions;

    public PreAssertionService(List<PreAssertion> preAssertions) {
        this.preAssertions = preAssertions.stream().sorted(comparingInt(PreAssertion::getOrder)).collect(Collectors.toUnmodifiableList());
    }

    public Optional<RuntimeException> ensureLegal(State lastState, Move move) {
        return preAssertions.stream()
                .map(preAssertion -> preAssertion.assertLegal(lastState, move))
                .flatMap(Optional::stream)
                .findFirst();
    }
}
