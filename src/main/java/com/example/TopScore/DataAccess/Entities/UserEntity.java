package com.example.TopScore.DataAccess.Entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "score_index", columnList = "score"),
        @Index(name = "time_index", columnList = "score")
})
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String player;
    private String normalizedPlayer;
    private Integer score;
    private Date time;

    public UserEntity() {
    }

    public UserEntity(String name, String normalizedName, int score, Date time) {
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

        UserEntity user = (UserEntity) o;
        return player.equals(user.player) &&
                Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, score, time);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Country{");
        sb.append("id=").append(id);
        sb.append(", player='").append(player).append('\'');
        sb.append(", score=").append(score);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}