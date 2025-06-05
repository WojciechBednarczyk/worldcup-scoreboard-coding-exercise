package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Score {

    @Getter
    private int homeTeamScore;

    @Getter
    private int awayTeamScore;

    public Score() {
        this.homeTeamScore = 0;
        this.awayTeamScore = 0;
    }

    public int getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }
}
