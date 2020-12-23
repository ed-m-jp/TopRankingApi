package com.example.TopScore.DataAccess.Entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "score_index", columnList = "score"),
        @Index(name = "time_index", columnList = "score")
})
public class ScoreEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Size(min=1)
    private String player;

    @Column(nullable = false)
    @Size(min=1)
    private String normalizedPlayer;

    @Column(nullable = false)
    @Min(value = 1)
    private Integer score;
    private Date time;

    public ScoreEntity() {
    }

    public ScoreEntity(String name, String normalizedName, int score, Date time) {
        this.player = name;
        this.normalizedPlayer = normalizedName;
        this.score = score;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getNormalizedPlayer() {
        return normalizedPlayer;
    }

    public void setNormalizedPlayer(String normalizedPlayer) {
        this.normalizedPlayer = normalizedPlayer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ScoreEntity score = (ScoreEntity) o;
        return player.equals(score.player) &&
                Objects.equals(id, score.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, score, time);
    }

    @Override
    public String toString() {
        return "Score{" + "id=" + id +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", time=" + time +
                '}';
    }
}