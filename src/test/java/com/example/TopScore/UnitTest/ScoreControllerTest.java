package com.example.TopScore.UnitTest;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Exception.ScoreNotFoundException;
import com.example.TopScore.Services.Interfaces.ScoreService;
import com.example.TopScore.Web.ScoreController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {

    @MockBean
    ScoreService scoreService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void GetScoreById_GivenValidId_ReturnScoreForThisId() throws Exception {
        // Arrange
        long idToDelete = 1;
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());
        when(scoreService.getScoreById(anyLong())).thenReturn(CompletableFuture.completedFuture(entity));

        // Act
        // Assert
        mvc.perform(get("/score/" + idToDelete))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(entity.getPlayer())));
    }

    @Test
    public void GetScoreById_GivenInvalidId_ReturnNotFound() throws Exception {
        // Arrange
        long idToDelete = 1;
        when(scoreService.getScoreById(anyLong())).thenThrow(new ScoreNotFoundException(idToDelete));

        // Act
        // Assert
        mvc.perform(get("/score/" + idToDelete))
                .andExpect(status().isNotFound());
    }
}
