package chess;

import java.util.List;
import java.util.ArrayList;

public class EnPassantTracker {
    private ChessPosition vulnerablePawn;
    private ChessPosition killDestination;
    private List<ChessPosition> attackingSquares;
    private boolean enPassantAllowed = false;

    public EnPassantTracker() {
    }

    public EnPassantTracker(ChessPosition forwardedPawn, ChessGame.TeamColor color) {
        enPassantAllowed = true;
        vulnerablePawn = forwardedPawn;
        attackingSquares = new ArrayList<>();
        if (forwardedPawn.getColumn() > 1) {
            attackingSquares.add(new ChessPosition(forwardedPawn.getRow(), forwardedPawn.getColumn() - 1));
        }
        if (forwardedPawn.getColumn() < 8) {
            attackingSquares.add(new ChessPosition(forwardedPawn.getRow(), forwardedPawn.getColumn() + 1));
        }
        if (color == ChessGame.TeamColor.WHITE) {
            killDestination = new ChessPosition(vulnerablePawn.getRow() - 1, vulnerablePawn.getColumn());
        } else {
            killDestination = new ChessPosition(vulnerablePawn.getRow() + 1, vulnerablePawn.getColumn());
        }
    }

    public boolean isEnPassantAllowed() {
        return enPassantAllowed;
    }

    public ChessPosition getVulnerablePawn() {
        return vulnerablePawn;
    }

    public ChessPosition getKillDestination() {
        return killDestination;
    }

    public List<ChessPosition> getAttackingSquares() {
        return attackingSquares;
    }

    public void setEnPassantAllowed() {
        enPassantAllowed = false;
    }
}
