package com.example.TopScore.Web;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Services.Interfaces.ScoreService;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import com.example.TopScore.Shared.ApiResponse.ScoreDto;
import com.example.TopScore.Web.Mapper.ScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/score")
public class ScoreController
{
    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService)
    {
        this.scoreService = scoreService;
    }

    /*
        Get score by score id

        Parameters:
             scoreId -> The value for the score you want to look up

        Response:
            ScoreDto ->
                - id = Score id
                - player = Player Name
                - score = score for the record
                - time = time of the record

        Response Code:
            200 -> return the score for this id
            404 -> no score found for this id
     */
    @GetMapping(path = "/{scoreId}", produces = "application/json")
    public ResponseEntity<ScoreDto> getScoreById(@PathVariable("scoreId") Long scoreId) throws InterruptedException, ExecutionException
    {
        CompletableFuture<ScoreEntity> user = scoreService.getScoreById(scoreId);

        CompletableFuture.allOf(user).join();

        return ResponseEntity.ok(ScoreMapper.ScoreToDtoMap(user.get()));
    }

    /*
        Add a new score in the system

        Parameters:
             score -> new ScoreDto to add to the system
                - ScoreDto ->
                    - id = Score id
                    - player = Player Name
                    - score = score for the record
                    - time = time of the record "dd/MM/yyyy hh:mm:ss"

        Response Code:
            201 -> return the url location of the created user as well as the data
    */
    @PostMapping(path ="/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addScoreToSystem(@Valid @RequestBody ScoreDto score) throws InterruptedException, ExecutionException
    {
        CompletableFuture<ScoreEntity> addedScore = scoreService.AddScore(ScoreMapper.ScoreToEntityMap(score));

        CompletableFuture.allOf(addedScore).join();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(addedScore.get().getId())
                .toUri();

        return ResponseEntity.created(location).body(ScoreMapper.ScoreToDtoMap(addedScore.get()));
    }

    /*
        Delete score by score id

        Parameters:
             scoreId -> Score to delete id

        Response Code:
            204 -> score deleted
            404 -> no score found for this id
    */
    @DeleteMapping(path ="/{scoreId}", produces = "application/json")
    public ResponseEntity<Void> deleteScoreById(@PathVariable("scoreId") Long scoreId) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> delete = CompletableFuture.runAsync(() -> scoreService.DeleteScoreById(scoreId));

        delete.get();
        return ResponseEntity.noContent().build();
    }

    /*
            Get list of score with pagination

            Parameters:
                 page -> The value of the desired page
                 size -> The desired size per page
                 sort -> Filter on any field
            Response:
                Page -> A paginated list of scoreDto
                    - ScoreDto ->
                        - id = Score id
                        - player = Player Name
                        - score = score for the record
                        - time = time of the record
            Response Code:
                200 -> return the page of score
                422 -> The sort expression did not match the current object please sure on existing fields
    */
    @GetMapping(path = "/list", produces = "application/json")
    public Page<ScoreDto> getScores(Pageable pageable) throws InterruptedException, ExecutionException
    {
        CompletableFuture<Page<ScoreEntity>> page = scoreService.getScoreList(pageable);

        CompletableFuture.allOf(page).join();
        return page.get().map(ScoreMapper::ScoreToDtoMap);
    }

    /*
            Get score history for player

            Parameters:
                 player -> The player for whom we want the history
            Response:
                PlayerHistoryDto -> the score history for the requested player
                    - playerHighestScore = Best score for the player
                    - playerLowestScore = Lowest score for the player
                    - playerScoreHistory = List of all the player's score
                    - playerAverageScore = average score for the player
            Response Code:
                200 -> return the history of scores for the player
                404 -> player not found
    */
    @GetMapping(path = "/history/{player}", produces = "application/json")
    public ResponseEntity<PlayerHistoryDto>  getPlayerScoreHistoryByName(@PathVariable("player") String player) throws InterruptedException, ExecutionException
    {
        CompletableFuture<PlayerHistoryDto> history = scoreService.getPlayerScoreHistoryByName(player);

        CompletableFuture.allOf(history).join();

        return ResponseEntity.ok(history.get());
    }
}