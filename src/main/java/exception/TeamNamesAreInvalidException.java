package exception;

public class TeamNamesAreInvalidException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Team names are invalid. They cannot be blank, null or equal";

    public TeamNamesAreInvalidException() {
        super(EXCEPTION_MESSAGE);
    }
}
