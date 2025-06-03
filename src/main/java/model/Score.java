package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Score {

    @Getter
    private Integer homeTeamScore;

    @Getter
    private Integer awayTeamScore;

    public Score() {
        this.homeTeamScore = 0;
        this.awayTeamScore = 0;
    }

    public Integer getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }
}
