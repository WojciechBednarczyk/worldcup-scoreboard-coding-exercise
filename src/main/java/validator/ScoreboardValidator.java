package validator;

import exception.ScoreToUpdateCannotBeNegativeIntegersException;
import exception.ScoreToUpdateCannotBeSmallerThanActualScoreException;
import exception.TeamHasAlreadyGameInProgressException;
import exception.TeamNamesAreInvalidException;
import model.Match;
import model.Score;

import java.util.Map;
import java.util.Objects;

public class ScoreboardValidator {

    private ScoreboardValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void areTeamNamesValid(String homeTeamName, String awayTeamName) {
        if (Objects.isNull(homeTeamName) || Objects.isNull(awayTeamName) || homeTeamName.isBlank() || awayTeamName.isBlank()) {
            throw new TeamNamesAreInvalidException();
        }
        var formatedHomeTeamName = formatTeamName(homeTeamName);
        var formatedAwayTeamName = formatTeamName(awayTeamName);
        if (formatedHomeTeamName.equals(formatedAwayTeamName)) {
            throw new TeamNamesAreInvalidException();
        }
    }

    public static void validateIfBothTeamsCanStartGame(String homeTeamName, String awayTeamName, Map<Long, Match> scoreboardData){
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
    }
    private static String formatTeamName(String teamName) {
        return teamName.replaceAll("\\s", "").toLowerCase();
    }

    public static void validateIfScoreToUpdateIsValid(int homeTeamScore, int awayTeamScore, Score actualScore) {
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new ScoreToUpdateCannotBeNegativeIntegersException();
        }
        var actualHomeTeamScore = actualScore.getHomeTeamScore();
        var actualAwayTeamScore = actualScore.getAwayTeamScore();
        if (homeTeamScore < actualHomeTeamScore || awayTeamScore < actualAwayTeamScore) {
            throw new ScoreToUpdateCannotBeSmallerThanActualScoreException();
        }
    }
}
