package com.example.TopScore.Services.Interfaces;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ScoreService
{
    CompletableFuture<ScoreEntity> AddScore(ScoreEntity user)  throws InterruptedException;

    void DeleteScoreById(long scoreId);

    CompletableFuture<ScoreEntity> getScoreById(long userId) throws InterruptedException;

    CompletableFuture<Page<ScoreEntity>> getScoreList(Pageable page) throws InterruptedException, ExecutionException;

    CompletableFuture<PlayerHistoryDto> getPlayerScoreHistoryByName(String player) throws InterruptedException, ExecutionException;
}
