package com.example.TopScore.IntegrationTest;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Exception.InvalidPageableParameterException;
import com.example.TopScore.Exception.PlayerHistoryNotFoundException;
import com.example.TopScore.Exception.ScoreNotFoundException;
import com.example.TopScore.Services.Interfaces.ScoreService;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import com.example.TopScore.Shared.ApiResponse.ScoreDto;
import com.example.TopScore.Web.Mapper.ScoreMapper;
import com.example.TopScore.Web.ScoreController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScoreController.class)
public class ScoreControllerIntegrationTest {

    @MockBean
    ScoreService scoreService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Test
    public void GetScoreById_GivenValidId_ReturnScoreForThisId() throws Exception {
        // Arrange
        long idToSearch = 1;
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());
        when(scoreService.getScoreById(anyLong())).thenReturn(CompletableFuture.completedFuture(entity));

        // Act
        // Assert
        mvc.perform(get("/score/" + idToSearch))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.player").value(entity.getPlayer()));
    }

    @Test
    public void GetScoreById_GivenInvalidId_ReturnNotFound() throws Exception {
        // Arrange
        long idToSearch = 1;
        when(scoreService.getScoreById(anyLong())).thenThrow(new ScoreNotFoundException(idToSearch));

        // Act
        // Assert
        mvc.perform(get("/score/" + idToSearch))
                .andExpect(status().isNotFound());
    }

    @Test
    public void AddScoreToSystem_GivenValidRequest_ReturnCreated() throws Exception {
        // Arrange
        ScoreDto request = new ScoreDto((long)1, "test", 1, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        when(scoreService.AddScore(any(ScoreEntity.class))).thenReturn(CompletableFuture.completedFuture(ScoreMapper.ScoreToEntityMap(request)));

        // Act
        // Assert
        mvc.perform(post("/score/add")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.player").value(request.getPlayer()));
    }

    @Test
    public void AddScoreToSystem_GivenInvalidPlayerFieldInRequest_ReturnCreated() throws Exception {
        // Arrange
        ScoreDto request = new ScoreDto((long)1, "", 1, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        when(scoreService.AddScore(any(ScoreEntity.class))).thenReturn(CompletableFuture.completedFuture(ScoreMapper.ScoreToEntityMap(request)));

        // Act
        // Assert
        mvc.perform(post("/score/add")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void DeleteScoreById_GivenValidId_ReturnNoContent() throws Exception {
        // Arrange
        long idToDelete = 1;
        doNothing().when(scoreService).DeleteScoreById(anyLong());

        // Act
        // Assert
        mvc.perform(delete("/score/" + idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    public void DeleteScoreById_GivenInvalidId_ReturnNoContent() throws Exception {
        // Arrange
        long idToDelete = 1;
        doThrow(new ScoreNotFoundException(idToDelete)).when(scoreService).DeleteScoreById(anyLong());

        // Act
        // Assert
        mvc.perform(delete("/score/" + idToDelete))
                .andExpect(status().isNotFound());
    }

    @Test
    public void GetScores_GivenValidRequest_ReturnCreated() throws Exception {
        // Arrange
        Pageable page = PageRequest.of(0, 10);
        List<ScoreEntity> scores = new ArrayList<>();
        ScoreDto request = new ScoreDto((long)1, "test", 1, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        scores.add(ScoreMapper.ScoreToEntityMap(request));
        Page<ScoreEntity> scorePage = new PageImpl<>(scores);
        when(scoreService.getScoreList(any(Pageable.class))).thenReturn(CompletableFuture.completedFuture(scorePage));

        // Act
        // Assert
        mvc.perform(get("/score/list?page=" + page.getPageNumber()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    public void GetScores_GivenInvalidSortRequest_ReturnUnprocessableEntity() throws Exception {
        // Arrange
        Pageable page = PageRequest.of(0, 10, Sort.by("test"));
        List<ScoreEntity> scores = new ArrayList<>();
        Page<ScoreEntity> scorePage = new PageImpl<>(scores);
        when(scoreService.getScoreList(any(Pageable.class))).thenThrow(new InvalidPageableParameterException(page));

        // Act
        // Assert
        mvc.perform(get("/score/list?page=" + page.getPageNumber()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenValidPlayer_ReturnPlayerHistory() throws Exception {
        // Arrange
        String player = "test";
        ScoreDto low = new ScoreDto((long)1, player, 1, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        ScoreDto mid1 = new ScoreDto((long)2, player, 2, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        ScoreDto mid2 = new ScoreDto((long)3, player, 3, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        ScoreDto mid3 = new ScoreDto((long)4, player, 4, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
        ScoreDto High = new ScoreDto((long)5, player, 5, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));

        List<ScoreDto> dtoList = new ArrayList<>();
        dtoList.add(low);
        dtoList.add(mid1);
        dtoList.add(mid2);
        dtoList.add(mid3);
        dtoList.add(High);

        PlayerHistoryDto dto = new PlayerHistoryDto(High, low, dtoList, (dtoList.stream().mapToDouble(ScoreDto::getScore).sum() / dtoList.size()));
        when(scoreService.getPlayerScoreHistoryByName(anyString())).thenReturn(CompletableFuture.completedFuture(dto));

        // Act
        // Assert
        mvc.perform(get("/score/history/" + player))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerHighestScore.id").value(High.getId()))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerHighestScore.score").value(High.getScore()))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerLowestScore.id").value(low.getId()))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerLowestScore.score").value(low.getScore()));
    }

    @Test
    public void GetPlayerScoreHistoryByName_GivenInvalidPlayer_ReturnNotFound() throws Exception {
        // Arrange
        String player = "test";
        when(scoreService.getPlayerScoreHistoryByName(anyString())).thenThrow(new PlayerHistoryNotFoundException(player));

        // Act
        // Assert
        mvc.perform(get("/score/history/" + player))
                .andExpect(status().isNotFound());
    }
}
