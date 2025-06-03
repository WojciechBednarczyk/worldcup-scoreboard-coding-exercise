package service;

import exception.MatchNotFound;
import exception.MatchScoreCannotBeAdjustedTwiceInARowException;
import model.Match;
import model.Score;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardService {

    private Long idSequence;

    private LinkedHashMap<Long, Match> scoreboardData;

    public ScoreboardService() {
        this.idSequence = 1L;
        this.scoreboardData = new LinkedHashMap<>();
    }

    ScoreboardService(LinkedHashMap<Long,Match> scoreboardData) {
        this.idSequence = scoreboardData.size() + 1L;
        this.scoreboardData = scoreboardData;
    }

    public Map<Long, Match> startMatch(String homeTeamName, String awayTeamName) {
        var match = new Match(homeTeamName, awayTeamName);
        scoreboardData.put(idSequence, match);
        idSequence += 1;
        return scoreboardData;
    }

    public Map<Long, Match> finishMatch(Long matchId) {
        var removedMatch = scoreboardData.remove(matchId);
        if (Objects.isNull(removedMatch)){
            throw new MatchNotFound(matchId);
        }
        return scoreboardData;
    }

    public Map<Long, Match> updateHomeTeamScore(Long matchId) {
        var match = scoreboardData.get(matchId);
        if (Objects.isNull(match)){
            throw new MatchNotFound(matchId);
        }
        scoreboardData.get(matchId).updateHomeTeamScore();
        sortScoreBoard();
        return scoreboardData;
    }

    public Map<Long, Match> updateAwayTeamScore(Long matchId) {
        var match = scoreboardData.get(matchId);
        if (Objects.isNull(match)){
            throw new MatchNotFound(matchId);
        }
        match.updateAwayTeamScore();
        sortScoreBoard();
        return scoreboardData;
    }

    public Map<Long, Match> adjustMatchScore(Long matchId) {
        var match = scoreboardData.get(matchId);
        if (Objects.isNull(match)){
            throw new MatchNotFound(matchId);
        }
        if (!match.isLastUpdateActionReverted()) {
            match.setScore(match.getPreviousScore());
            match.setPreviousScore(null);
            match.setLastUpdateActionReverted(true);
        } else {
            throw new MatchScoreCannotBeAdjustedTwiceInARowException(matchId);
        }
        sortScoreBoard();
        return scoreboardData;
    }

    public Map<Long, Match> updateScore(long matchId, int homeTeamScore, int awayTeamScore) {
        var match = scoreboardData.get(matchId);
        if (Objects.isNull(match)){
            throw new MatchNotFound(matchId);
        }
        match.setPreviousScore(match.getScore());
        match.setScore(new Score(homeTeamScore, awayTeamScore));
        match.setLastUpdateActionReverted(false);
        sortScoreBoard();
        return scoreboardData;
    }

    private void sortScoreBoard() {
        scoreboardData = scoreboardData.entrySet()
                .stream()
                .sorted(Comparator.comparingInt((Map.Entry<Long, Match> matchEntry) -> matchEntry.getValue().getScore().getTotalScore())
                        .reversed()
                        .thenComparing(matchEntry -> matchEntry.getValue().getCreateTimestamp()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public String getActualScoreboard() {
        var actualScoreboard = new ArrayList<String>();
        scoreboardData.forEach((matchId, match) -> {
            var scoreboardRow = String.format("%s. %s - %s: %s - %s",
                    matchId,
                    match.getHomeTeam(),
                    match.getAwayTeam(),
                    match.getScore().getHomeTeamScore(),
                    match.getScore().getAwayTeamScore()
            );
            actualScoreboard.add(scoreboardRow);
        });
        return String.join("\n", actualScoreboard);
    }
}
