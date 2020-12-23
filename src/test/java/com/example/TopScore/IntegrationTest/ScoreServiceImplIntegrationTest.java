package com.example.TopScore.IntegrationTest;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.DataAccess.Repository.ScoreRepository;
import com.example.TopScore.Exception.PlayerHistoryNotFoundException;
import com.example.TopScore.Exception.ScoreNotFoundException;
import com.example.TopScore.Services.ScoreServiceImpl;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ScoreServiceImplIntegrationTest {
    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ScoreServiceImpl scoreService;

    @BeforeEach
    public void initEach()
    {
        scoreRepository.deleteAll();
    }

    @Test
    public void AddScore_GivenValidEntity_ReturnCreateEntity() throws ExecutionException, InterruptedException {
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());

        // Act
        CompletableFuture<ScoreEntity> result = scoreService.AddScore(entity);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertEquals(result.get().getPlayer(), entity.getPlayer());
        Assertions.assertEquals(result.get().getNormalizedPlayer(), entity.getNormalizedPlayer());
        Assertions.assertEquals(result.get().getScore(), entity.getScore());
        Assertions.assertEquals(result.get().getTime(), entity.getTime());
    }

    @Test
    public void DeleteScoreById_GivenValidId_ReturnVoid() {
        // Arrange
        ScoreEntity entity = scoreRepository.save(new ScoreEntity("test", "test".toUpperCase(), 1, new Date()));

        // Act
        // Assert
        Assertions.assertDoesNotThrow(() ->
                scoreService.DeleteScoreById(entity.getId()));
    }

    @Test
    public void DeleteScoreById_GivenInvalidId_Throw() {
        // Arrange
        long idToDelete = 1;

        // Act
        // Assert
        Assertions.assertThrows(ScoreNotFoundException.class, () ->
                scoreService.DeleteScoreById(idToDelete));
    }

    @Test
    public void GetScoreById_GivenValidId_ReturnEntity() throws ExecutionException, InterruptedException {
        // Arrange
        ScoreEntity entity = scoreRepository.save(new ScoreEntity("test", "test".toUpperCase(), 1, new Date()));

        // Act
        CompletableFuture<ScoreEntity> result = scoreService.getScoreById(entity.getId());
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertEquals(result.get().getId(), entity.getId());
        Assertions.assertEquals(result.get().getPlayer(), entity.getPlayer());
        Assertions.assertEquals(result.get().getNormalizedPlayer(), entity.getNormalizedPlayer());
        Assertions.assertEquals(result.get().getScore(), entity.getScore());
    }

    @Test
    public void GetScoreById_GivenInvalidId_ThrowScoreNotFoundException() {
        // Arrange
        long idToDelete = 1;

        // Act
        // Assert
        Assertions.assertThrows(ScoreNotFoundException.class, () ->
        {
            CompletableFuture<ScoreEntity> result = scoreService.getScoreById(idToDelete);
            CompletableFuture.allOf(result).join();
        });
    }

    @Test
    public void GetScoreList_GivenValidPage_ReturnPage() throws ExecutionException, InterruptedException {
        // Arrange
        scoreRepository.save(new ScoreEntity("test1", "test1".toUpperCase(), 1, new Date()));
        scoreRepository.save(new ScoreEntity("test2", "test2".toUpperCase(), 12, new Date()));
        scoreRepository.save(new ScoreEntity("test3", "test3".toUpperCase(), 13, new Date()));
        scoreRepository.save(new ScoreEntity("test4", "test4".toUpperCase(), 14, new Date()));
        Pageable page = PageRequest.of(0, 10);

        // Act
        CompletableFuture<Page<ScoreEntity>> result = scoreService.getScoreList(page);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertFalse(result.get().isEmpty());
        Assertions.assertEquals(result.get().stream().count(), 4);
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenValidPlayer_ReturnPage() throws ExecutionException, InterruptedException {
        // Arrange
        String player = "test";
        ScoreEntity high = new ScoreEntity("test", "test".toUpperCase(), 13, new Date());
        ScoreEntity low = new ScoreEntity("tESt", "tESt".toUpperCase(), 1, new Date());
        scoreRepository.save(low);
        scoreRepository.save(new ScoreEntity("tesT", "tesT".toUpperCase(), 12, new Date()));
        scoreRepository.save(high);
        scoreRepository.save(new ScoreEntity("test2", "test2".toUpperCase(), 14, new Date()));

        // Act
        CompletableFuture<PlayerHistoryDto> result = scoreService.getPlayerScoreHistoryByName(player);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertEquals(result.get().getPlayerScoreHistory().size(), 3);
        Assertions.assertEquals(result.get().getPlayerHighestScore().getScore(), high.getScore());
        Assertions.assertEquals(result.get().getPlayerLowestScore().getScore(), low.getScore());
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenInvalidPlayer_ThrowPlayerHistoryNotFoundException() {
        // Arrange
        String player = "test";

        // Act
        // Assert
        Assertions.assertThrows(PlayerHistoryNotFoundException.class, () ->
        {
            CompletableFuture<PlayerHistoryDto> result = scoreService.getPlayerScoreHistoryByName(player);
            CompletableFuture.allOf(result).join();
        });
    }
}
