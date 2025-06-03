package validator;

import exception.TeamHasAlreadyGameInProgressException;
import exception.TeamNamesAreInvalidException;
import model.Match;

import java.util.Map;
import java.util.Objects;

public class ScoreboardValidator {

    private ScoreboardValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean areTeamNamesValid(String homeTeamName, String awayTeamName) {
        if (Objects.isNull(homeTeamName) || Objects.isNull(awayTeamName) || homeTeamName.isBlank() || awayTeamName.isBlank()) {
            throw new TeamNamesAreInvalidException();
        }
        var formatedHomeTeamName = formatTeamName(homeTeamName);
        var formatedAwayTeamName = formatTeamName(awayTeamName);
        if (formatedHomeTeamName.equals(formatedAwayTeamName)) {
            throw new TeamNamesAreInvalidException();
        }
        return true;
    }

    public static boolean validateIfBothTeamsCanStartGame(String homeTeamName, String awayTeamName, Map<Long, Match> scoreboardData){
        var formatedHomeTeamName = formatTeamName(homeTeamName);
        var formatedAwayTeamName = formatTeamName(awayTeamName);

        scoreboardData.values().forEach(match -> {
            if (formatedHomeTeamName.equals(formatTeamName(match.getHomeTeam())) || formatedHomeTeamName.equals(formatTeamName(match.getAwayTeam()))) {
                throw new TeamHasAlreadyGameInProgressException(homeTeamName);
            }
            if (formatedAwayTeamName.equals(formatTeamName(match.getHomeTeam())) || formatedAwayTeamName.equals(formatTeamName(match.getAwayTeam()))) {
                throw new TeamHasAlreadyGameInProgressException(awayTeamName);
            }
        });
        return true;
    }
    private static String formatTeamName(String teamName) {
        return teamName.replaceAll("\\s", "").toLowerCase();
    }
}
