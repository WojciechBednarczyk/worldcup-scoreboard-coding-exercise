package validator;

import exception.TeamHasAlreadyGameInProgressException;
import exception.TeamNamesAreInvalidException;
import model.Match;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreboardValidatorTest {

    private static Stream<Arguments> provideValuesToValidateTeamNames(){
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("Poland", "Poland"),
                Arguments.of(" Poland ", "Poland"),
                Arguments.of("Poland", "poLaNd"),
                Arguments.of("Poland", null),
                Arguments.of("Poland", "")
        );
    }

    private static Stream<Arguments> provideValuesToValidateIfBothTeamsCanStartGame(){
        return Stream.of(
                Arguments.of("TeamA", "Poland"),
                Arguments.of("Poland", "TeamA"),
                Arguments.of("Poland", "teamA"),
                Arguments.of("Poland", "  teamA  ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValuesToValidateTeamNames")
    void shouldThrowExceptionWhenTeamNamesAreBlankOrNullOrEqual(String homeTeamName, String awayTeamName) {
        // given
        // when
        // then
        assertThrows(TeamNamesAreInvalidException.class,() -> ScoreboardValidator.areTeamNamesValid(homeTeamName,awayTeamName));
    }

    @Test
    void shouldReturnTrueWhenTeamNamesAreValid() {
        // given
        var homeTeamName = "Poland";
        var awayTeamName = "England";

        // when
        var result = ScoreboardValidator.areTeamNamesValid(homeTeamName, awayTeamName);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideValuesToValidateIfBothTeamsCanStartGame")
    void shouldThrowExceptionWhenOneOfTheTeamsAlreadyHasGameInProgress(String homeTeamName, String awayTeamName) {
        // given
        var scoreboardData = new HashMap<Long,Match>();
        var match = new Match("TeamA", "TeamB");
        scoreboardData.put(1L, match);

        // when
        // then
        assertThrows(TeamHasAlreadyGameInProgressException.class,() -> ScoreboardValidator.validateIfBothTeamsCanStartGame(homeTeamName,awayTeamName, scoreboardData));
    }

    @Test
    void shouldReturnTrueWhenBothTeamsCanStartGame() {
        // given
        var scoreboardData = new HashMap<Long, Match>();
        var match = new Match("TeamA", "TeamB");
        scoreboardData.put(1L, match);
        var homeTeamName = "Poland";
        var awayTeamName = "England";

        // when
        var result = ScoreboardValidator.validateIfBothTeamsCanStartGame(homeTeamName, awayTeamName, scoreboardData);

        // then
        assertThat(result).isTrue();
    }
}
