package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Match {

    @Getter
    private String homeTeam;

    @Getter
    private String awayTeam;

    @Getter
    @Setter
    private Score score;

    @Getter
    @Setter
    private Score previousScore;

    @Getter
    private LocalDateTime createTimestamp;

    @Getter
    @Setter
    private boolean isLastUpdateActionReverted;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = new Score();
        this.previousScore = null;
        this.createTimestamp = LocalDateTime.now();
        this.isLastUpdateActionReverted = false;
    }

    public void updateHomeTeamScore() {
        previousScore = new Score(score.getHomeTeamScore(), score.getAwayTeamScore());
        isLastUpdateActionReverted = false;
        score = new Score(score.getHomeTeamScore() + 1, score.getAwayTeamScore());
    }

    public void updateAwayTeamScore() {
        previousScore = new Score(score.getHomeTeamScore(), score.getAwayTeamScore());
        isLastUpdateActionReverted = false;
        score = new Score(score.getHomeTeamScore(), score.getAwayTeamScore() + 1);
    }
}
