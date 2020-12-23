package com.example.TopScore.Shared.ApiResponse;

import java.util.List;

public class PlayerHistoryDto {
    private ScoreDto playerHighestScore;
    private ScoreDto playerLowestScore;
    private Double playerAverageScore;
    private List<ScoreDto> playerScoreHistory;

    public PlayerHistoryDto(ScoreDto playerHighestScore,
                            ScoreDto playerLowestScore,
                            List<ScoreDto> playerScoreHistory,
                            Double playerAverageScore) {
        this.playerHighestScore = playerHighestScore;
        this.playerLowestScore = playerLowestScore;
        this.playerScoreHistory = playerScoreHistory;
        this.playerAverageScore = playerAverageScore;
    }

    public ScoreDto getPlayerHighestScore() {
        return playerHighestScore;
    }

    public ScoreDto getPlayerLowestScore() {
        return playerLowestScore;
    }

    public List<ScoreDto> getPlayerScoreHistory() {
        return playerScoreHistory;
    }

    public Double getPlayerAverageScore() {
        return playerAverageScore;
    }
}
