package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Match {

    @Getter
    private String homeTeam;

    @Getter
    private String awayTeam;

    private Score score;

    private Score previousScore;

    private LocalDateTime createTimestamp;

    private boolean isLastUpdateActionReverted;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = new Score();
        this.previousScore = null;
        this.createTimestamp = LocalDateTime.now();
        this.isLastUpdateActionReverted = false;
    }
}
