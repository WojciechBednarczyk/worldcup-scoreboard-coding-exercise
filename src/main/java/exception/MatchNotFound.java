package exception;

public class MatchNotFound extends RuntimeException {

    public MatchNotFound(Long matchId) {
        super(String.format("Match with ID %s not found", matchId));
    }
}
