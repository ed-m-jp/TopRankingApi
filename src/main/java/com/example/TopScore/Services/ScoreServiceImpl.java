package com.example.TopScore.Services;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.DataAccess.Repository.ScoreRepository;
import com.example.TopScore.Exception.InvalidPageableParameterException;
import com.example.TopScore.Exception.PlayerHistoryNotFoundException;
import com.example.TopScore.Exception.ScoreNotFoundException;
import com.example.TopScore.Services.Interfaces.ScoreService;
import com.example.TopScore.Shared.ApiResponse.PlayerHistoryDto;
import com.example.TopScore.Web.Mapper.ScoreMapper;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

//Service to manage everything related to score
@Service
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository repository;

    @Autowired
    public ScoreServiceImpl(ScoreRepository repository)
    {
        this.repository = repository;
    }

    //Add new score in the system
    @Async
    public CompletableFuture<ScoreEntity> AddScore(ScoreEntity score)
    {
        return CompletableFuture.completedFuture(repository.save(score));
    }

    //Delete a Score from the system
    @Async
    public void DeleteScoreById(long scoreId)
    {
        if(repository.existsById(scoreId) == false)
            throw new ScoreNotFoundException(scoreId);

        repository.deleteById(scoreId);
    }

    //Get score by score id
    @Async
    public CompletableFuture<ScoreEntity> getScoreById(long scoreId)
    {
        return CompletableFuture.completedFuture(repository.findById(scoreId).orElseThrow(() -> new ScoreNotFoundException(scoreId)));
    }

    //Get paginated score list
    @Async
    public CompletableFuture<Page<ScoreEntity>> getScoreList(Pageable page)
    {
        try
        {
            Page<ScoreEntity> pageResult = repository.findAll(page);

            return CompletableFuture.completedFuture(pageResult);
        }
        catch(PropertyReferenceException | IllegalArgumentException e) {
            throw new InvalidPageableParameterException(page);
        }
    }

    //get score list for desired player
    @Async
    public CompletableFuture<PlayerHistoryDto> getPlayerScoreHistoryByName(@NotNull String player)
    {
        List<ScoreEntity> history = repository.findRecordsByName(player).orElseThrow(() -> new PlayerHistoryNotFoundException(player));
        ScoreEntity highestScore = history.stream().max(comparing(ScoreEntity::getScore)).orElse(null);
        ScoreEntity lowestScore = history.stream().min(comparing(ScoreEntity::getScore)).orElse(null);
        double totalScore = history.stream().mapToDouble(ScoreEntity::getScore).sum();

        assert highestScore != null;
        assert lowestScore != null;
        PlayerHistoryDto dto = new PlayerHistoryDto(ScoreMapper.ScoreToDtoMap(highestScore),
                ScoreMapper.ScoreToDtoMap(lowestScore),
                history.stream().map(ScoreMapper::ScoreToDtoMap).collect(Collectors.toList()),
                totalScore / history.size());

        return CompletableFuture.completedFuture(dto);
    }
}
