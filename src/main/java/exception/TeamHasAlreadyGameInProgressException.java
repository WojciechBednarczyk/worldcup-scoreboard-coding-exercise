package exception;

public class TeamHasAlreadyGameInProgressException extends RuntimeException {

    public TeamHasAlreadyGameInProgressException(String teamName) {
        super(String.format("The game cannot be started since team %s has already game in progress", teamName));
    }
}
