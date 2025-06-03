package service;

import exception.MatchScoreCannotBeAdjustedTwiceInARowException;
import model.Match;
import model.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreboardServiceTest {

    private ScoreboardService service;

    @BeforeEach
    void setUp() {
        var scoreboardTestData = prepareTestData();
        service = new ScoreboardService(scoreboardTestData);
    }

    @Test
    void shouldStartMatchAndAddItToScoreboard() {
        // given
        var homeTeamName = "Poland";
        var awayTeamName = "England";

        // when
        var result = service.startMatch(homeTeamName, awayTeamName);

        // then
        assertThat(result).hasSize(5);
        assertThat(result.keySet()).containsExactly(1L,2L,3L,4L,5L);
        assertThat(result.get(5L).getHomeTeam()).isEqualTo(homeTeamName);
        assertThat(result.get(5L).getAwayTeam()).isEqualTo(awayTeamName);
    }

    @Test
    void shouldFinishMatchAndRemoveItFromScoreboard() {
        // given
        var matchId = 2L;
        // when
        var result = service.finishMatch(matchId);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.keySet()).doesNotContain(matchId);
    }

    @Test
    void shouldUpdateHomeTeamScoreAndUpdateScoreboard() {
        // given
        var matchId = 2L;
        var expectedActualHomeTeamScore = 3;
        var expectedPreviousHomeTeamScore = 2;
        var expectedPreviousAwayTeamScore = 2;

        // when
        var result = service.updateHomeTeamScore(matchId);

        // then
        assertThat(result.keySet()).containsExactly(2L,1L,3L,4L);
        assertThat(result.get(2L).getScore().getHomeTeamScore()).isEqualTo(expectedActualHomeTeamScore);
        assertThat(result.get(2L).getPreviousScore().getHomeTeamScore()).isEqualTo(expectedPreviousHomeTeamScore);
        assertThat(result.get(2L).getPreviousScore().getAwayTeamScore()).isEqualTo(expectedPreviousAwayTeamScore);
    }

    @Test
    void shouldUpdateAwayTeamScoreAndUpdateScoreboard() {
        // given
        var matchId = 4L;
        var expectedActualAwayTeamScore = 1;
        var expectedPreviousHomeTeamScore = 1;
        var expectedPreviousAwayTeamScore = 0;
        // when
        var result = service.updateAwayTeamScore(matchId);

        // then
        assertThat(result.keySet()).containsExactly(1L,2L,4L,3L);
        assertThat(result.get(4L).getScore().getAwayTeamScore()).isEqualTo(expectedActualAwayTeamScore);
        assertThat(result.get(4L).getPreviousScore().getHomeTeamScore()).isEqualTo(expectedPreviousHomeTeamScore);
        assertThat(result.get(4L).getPreviousScore().getAwayTeamScore()).isEqualTo(expectedPreviousAwayTeamScore);
    }

    @Test
    void shouldAdjustMatchScoreAndUpdateScoreboard() {
        // given
        var matchId = 3L;
        var expectedActualHomeTeamScore = 1;
        var expectedActualAwayTeamScore = 0;

        // when
        var result = service.adjustMatchScore(matchId);

        // then
        assertThat(result.keySet()).containsExactly(1L,2L,4L,3L);
        assertThat(result.get(3L).getScore().getHomeTeamScore()).isEqualTo(expectedActualHomeTeamScore);
        assertThat(result.get(3L).getScore().getAwayTeamScore()).isEqualTo(expectedActualAwayTeamScore);
        assertThat(result.get(3L).getPreviousScore()).isNull();
        assertThat(result.get(3L).isLastUpdateActionReverted()).isTrue();
    }

    @Test
    void shouldNotBeAbleToAdjustMatchScoreAndThrowException() {
        // given
        var matchId = 4L;

        // when
        // then
        assertThrows(MatchScoreCannotBeAdjustedTwiceInARowException.class, () -> service.adjustMatchScore(matchId));
    }

    @Test
    void shouldGetActualScoreboard() {
        // given
        var expectedResult = """
                1. TeamA - TeamB: 2 - 2
                2. TeamC - TeamD: 2 - 2
                3. TeamE - TeamF: 1 - 1
                4. TeamG - TeamH: 1 - 0""";

        // when
        var result = service.getActualScoreboard();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateScore() {
        // given
        var matchId = 3L;
        var homeTeamScore = 3;
        var awayTeamScore = 5;
        var expectedPreviousHomeTeamScore = 1;
        var expectedPreviousAwayTeamScore = 1;

        // when
        var result = service.updateScore(matchId, homeTeamScore, awayTeamScore);

        // then
        assertThat(result.keySet()).containsExactly(3L,1L,2L,4L);
        assertThat(result.get(3L).getScore().getHomeTeamScore()).isEqualTo(homeTeamScore);
        assertThat(result.get(3L).getScore().getAwayTeamScore()).isEqualTo(awayTeamScore);
        assertThat(result.get(3L).getPreviousScore().getHomeTeamScore()).isEqualTo(expectedPreviousHomeTeamScore);
        assertThat(result.get(3L).getPreviousScore().getAwayTeamScore()).isEqualTo(expectedPreviousAwayTeamScore);
    }

    private LinkedHashMap<Long, Match> prepareTestData() {
        var scoreboardData = new LinkedHashMap<Long, Match>();
        var scoreTestData1 = new Score(2, 2);
        var previousScoreTestData1 = new Score(2, 1);
        var scoreTestData2 = new Score(2, 2);
        var previousScoreTestData2 = new Score(1, 2);
        var scoreTestData3 = new Score(1, 1);
        var previousScoreTestData3 = new Score(1,0);
        var scoreTestData4 = new Score(1, 0);
        var previousScoreTestData4 = new Score();
        var matchDate1 = LocalDateTime.of(2025,6,3,12,0);
        var matchDate2 = LocalDateTime.of(2025,6,3,14,0);
        var matchDate3 = LocalDateTime.of(2025,6,3,12,0);
        var matchDate4 = LocalDateTime.of(2025,6,3,10,0);

        var matchTestData1 = new Match("TeamA", "TeamB", scoreTestData1, previousScoreTestData1, matchDate1, false);
        var matchTestData2 = new Match("TeamC", "TeamD", scoreTestData2, previousScoreTestData2, matchDate2, false);
        var matchTestData3 = new Match("TeamE", "TeamF", scoreTestData3, previousScoreTestData3, matchDate3, false);
        var matchTestData4 = new Match("TeamG", "TeamH", scoreTestData4, previousScoreTestData4, matchDate4, true);
        scoreboardData.put(1L, matchTestData1);
        scoreboardData.put(2L, matchTestData2);
        scoreboardData.put(3L, matchTestData3);
        scoreboardData.put(4L, matchTestData4);
        return scoreboardData;
    }
}
