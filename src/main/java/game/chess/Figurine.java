package game.chess;

import game.chess.moverules.CapturingOneStepForwardDiagonal;
import game.chess.moverules.Castling;
import game.chess.moverules.Diagonal;
import game.chess.moverules.EnPasseant;
import game.chess.moverules.Horizontal;
import game.chess.moverules.L;
import game.chess.moverules.MoveRule;
import game.chess.moverules.OneStep;
import game.chess.moverules.OneStepForward;
import game.chess.moverules.TwoStepForwardInitialState;
import game.chess.moverules.Vertical;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Figurine {
    ROOK('R', true, Set.of(Vertical.INSTANCE, Horizontal.INSTANCE)),
    KNIGHT('N', false, Set.of(L.INSTANCE)),
    BISHOP('B', true, Set.of(Diagonal.INSTANCE)),
    KING('K', false, Set.of(OneStep.INSTANCE, Castling.INSTANCE)),
    QUEEN('Q', true, Set.of(Vertical.INSTANCE, Horizontal.INSTANCE, Diagonal.INSTANCE)),
    PAWN('P', false, Set.of(OneStepForward.INSTANCE, TwoStepForwardInitialState.INSTANCE, CapturingOneStepForwardDiagonal.INSTANCE, EnPasseant.INSTANCE));

    @Getter
    private final static Set<Figurine> blockables = Arrays.stream(values()).filter(Figurine::isBlockable).collect(Collectors.toUnmodifiableSet());

    private final char code;
    private final boolean blockable;
    private final Set<MoveRule> legalMoveRules;

}