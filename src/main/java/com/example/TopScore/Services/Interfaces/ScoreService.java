package com.example.TopScore.Services.Interfaces;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ScoreService
{
    //Add new score in the system
    CompletableFuture<ScoreEntity> AddScore(ScoreEntity user)  throws InterruptedException;

    //Delete a Score from the system
    void DeleteScoreById(long scoreId);

    //Get score by score id
    CompletableFuture<ScoreEntity> getScoreById(long userId) throws InterruptedException;

    //Get paginated score list
    CompletableFuture<Page<ScoreEntity>> getScoreList(Pageable page) throws InterruptedException, ExecutionException;

    //get score list for desired player
    CompletableFuture<PlayerHistoryDto> getPlayerScoreHistoryByName(String player) throws InterruptedException, ExecutionException;
}
