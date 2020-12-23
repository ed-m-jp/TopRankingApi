package com.example.TopScore.Shared.ApiResponse;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Valid
public class ScoreDto {
    private Long id;

    @NotBlank(message = "Player is mandatory")
    private String player;

    @NotNull(message = "Time is mandatory")
    @Min(value = 1, message = "Score can not be negative or 0")
    private Integer score;

    @NotBlank(message = "Time is mandatory")
    private String time;

    public ScoreDto(Long id, String player, Integer score, String time)
    {
        this.id = id;
        this.player = player;
        this.score = score;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
