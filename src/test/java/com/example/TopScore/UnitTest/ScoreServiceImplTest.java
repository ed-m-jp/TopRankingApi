package com.example.TopScore.UnitTest;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.DataAccess.Repository.ScoreRepository;
import com.example.TopScore.Exception.PlayerHistoryNotFoundException;
import com.example.TopScore.Exception.ScoreNotFoundException;
import com.example.TopScore.Services.ScoreServiceImpl;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ScoreServiceImplTest {
    @Mock
    ScoreRepository scoreRepository;

    @InjectMocks
    ScoreServiceImpl scoreService;

    @Test
    public void AddScore_GivenValidEntity_ReturnCreateEntity() throws ExecutionException, InterruptedException {
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());
        when(scoreRepository.save(any(ScoreEntity.class))).thenReturn(new ScoreEntity(entity.getPlayer(), entity.getNormalizedPlayer(),entity.getScore(), entity.getTime()));
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
        long idToDelete = 1;
        when(scoreRepository.existsById(anyLong())).thenReturn(true);

        // Act
        // Assert
        Assertions.assertDoesNotThrow(() ->
                scoreService.DeleteScoreById(idToDelete));
    }

    @Test
    public void GetScoreById_GivenValidId_ReturnEntity() throws ExecutionException, InterruptedException {
        // Arrange
        long idToSearch = 1;
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());
        when(scoreRepository.findById(idToSearch)).thenReturn(java.util.Optional.of(entity));

        // Act
        CompletableFuture<ScoreEntity> result = scoreService.getScoreById(idToSearch);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertEquals(result.get().getPlayer(), entity.getPlayer());
        Assertions.assertEquals(result.get().getNormalizedPlayer(), entity.getNormalizedPlayer());
        Assertions.assertEquals(result.get().getScore(), entity.getScore());
        Assertions.assertEquals(result.get().getTime(), entity.getTime());
    }

    @Test
    public void GetScoreById_GivenInvalidId_ThrowScoreNotFoundException() {
        // Arrange
        long idToDelete = 1;
        when(scoreRepository.findById(idToDelete)).thenReturn(java.util.Optional.empty());

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
        Pageable page = PageRequest.of(0, 10);
        List<ScoreEntity> scores = new ArrayList<>();
        Page<ScoreEntity> scorePage = new PageImpl<>(scores);
        when(scoreRepository.findAll(page)).thenReturn(scorePage);

        // Act
        CompletableFuture<Page<ScoreEntity>> result = scoreService.getScoreList(page);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertTrue(result.get().isEmpty());
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenValidPlayer_ReturnPage() throws ExecutionException, InterruptedException {
        // Arrange
        String player = "test";
        ArrayList<ScoreEntity> playerList = new ArrayList<>();
        ScoreEntity highScore = new ScoreEntity(player, player.toUpperCase(), 15, new Date());
        ScoreEntity lowScore = new ScoreEntity(player, player.toUpperCase(), 1, new Date());
        playerList.add(lowScore);
        playerList.add(new ScoreEntity(player, player.toUpperCase(), 10, new Date()));
        playerList.add(highScore);
        when(scoreRepository.findRecordsByName(anyString())).thenReturn(java.util.Optional.of(playerList));

        // Act
        CompletableFuture<PlayerHistoryDto> result = scoreService.getPlayerScoreHistoryByName(player);
        CompletableFuture.allOf(result).join();

        // Assert
        Assertions.assertTrue(result.isDone());
        Assertions.assertEquals(result.get().getPlayerScoreHistory().size(), playerList.size());
        Assertions.assertEquals(result.get().getPlayerHighestScore().getScore(), highScore.getScore());
        Assertions.assertEquals(result.get().getPlayerLowestScore().getScore(), lowScore.getScore());
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenInvalidPlayer_ThrowPlayerHistoryNotFoundException() {
        // Arrange
        String player = "test";
        when(scoreRepository.findRecordsByName(anyString())).thenReturn(java.util.Optional.empty());

        // Act
        // Assert
        Assertions.assertThrows(PlayerHistoryNotFoundException.class, () ->
        {
            CompletableFuture<PlayerHistoryDto> result = scoreService.getPlayerScoreHistoryByName(player);
            CompletableFuture.allOf(result).join();
        });
    }
}