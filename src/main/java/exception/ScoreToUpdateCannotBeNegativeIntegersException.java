package exception;

public class ScoreToUpdateCannotBeNegativeIntegersException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Provided score to update cannot be negative integers";

    public ScoreToUpdateCannotBeNegativeIntegersException() {
        super(EXCEPTION_MESSAGE);
    }
}
