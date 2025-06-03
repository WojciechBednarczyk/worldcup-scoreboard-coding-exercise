package exception;

public class MatchScoreCannotBeAdjustedTwiceInARowException extends RuntimeException {

    public MatchScoreCannotBeAdjustedTwiceInARowException(Long matchId) {
        super(String.format("Score of match with ID %s cannot be adjusted twice in a row", matchId));
    }
}
