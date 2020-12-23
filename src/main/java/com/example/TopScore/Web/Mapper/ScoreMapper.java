package com.example.TopScore.Web.Mapper;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.Exception.TimeFormatInvalidException;
import com.example.TopScore.Shared.ApiResponse.ScoreDto;

import java.text.SimpleDateFormat;

public final class ScoreMapper {

    public static ScoreEntity ScoreToEntityMap(ScoreDto dto)
    {
        ScoreEntity entity = new ScoreEntity();
        entity.setScore(dto.getScore());
        entity.setPlayer(dto.getPlayer());
        entity.setNormalizedPlayer(dto.getPlayer().toUpperCase());
        try
        {
            entity.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(dto.getTime()));
        }
        catch (Exception ex)
        {
            throw new TimeFormatInvalidException();
        }

        return entity;
    }

    public static ScoreDto ScoreToDtoMap(ScoreEntity entity)
    {
        return new ScoreDto(entity.getId(),
                entity.getPlayer(),
                entity.getScore(),
                new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(entity.getTime()));
    }
}
