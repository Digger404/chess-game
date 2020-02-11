package game.chess.moverules;

import game.chess.Figurine;
import game.chess.Piece;
import game.chess.board.Board;
import game.chess.board.Coordinate;
import game.chess.board.Move;

import java.util.List;

public enum EnPasseant implements MoveRule {
    INSTANCE;

    @Override
    public List<Coordinate> walk(Coordinate from, Coordinate to) {
        return List.of(to);
    }

    @Override
    public boolean isApplicable(Board board, Coordinate from, Coordinate to) {
        if (board.isInitialState()) return false;

        Move lastMove = board.getLastMove();
        Piece piece = board.getPiece(from);

        return MoveRules.isOneStepForwardDiagonal(board, from, to) &&
                isTwoStepsMove(lastMove) &&
                isAdjacent(from, lastMove) &&
                MoveRules.sameHorizontal(lastMove.getTo(), from) &&
                isPawnColor(board.getPiece(lastMove.getTo()), piece.getColor().change());
    }

    @Override
    public Board move(Board board, Coordinate from, Coordinate to) {
        Coordinate capturingStep = Coordinate.fromZeroIndex(to.getZeroIndexColumn(), from.getZeroIndexRow());
        return board.moveOnePiece(Move.of(from, capturingStep))
                .moveOnePiece(Move.of(capturingStep, to));
    }


    private boolean isAdjacent(Coordinate from, Move lastMove) {
        return Math.abs(lastMove.getTo().getZeroIndexColumn() - from.getZeroIndexColumn()) == 1;
    }

    private boolean isPawnColor(Piece piece, Piece.Color color) {
        return piece.equals(Piece.getPiece(Figurine.PAWN, color));
    }

    private boolean isTwoStepsMove(Move lastMove) {
        return Math.abs(lastMove.getFrom().getZeroIndexRow() - lastMove.getTo().getZeroIndexRow()) == 2;
    }
}
