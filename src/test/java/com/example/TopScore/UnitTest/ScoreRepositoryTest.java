package com.example.TopScore.UnitTest;

import com.example.TopScore.DataAccess.Entities.ScoreEntity;
import com.example.TopScore.DataAccess.Repository.ScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionSystemException;

import java.util.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ScoreRepositoryTest
{
    @Autowired
    private ScoreRepository repository;

    @BeforeEach
    public void initEach(){
        repository.deleteAll();
    }

    @Test
    public void Save_GivenCorrectEntity_ReturnCreateEntity() {
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());

        // Act
        ScoreEntity result = repository.save(entity);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getPlayer(), entity.getPlayer());
        Assertions.assertEquals(result.getNormalizedPlayer(), entity.getNormalizedPlayer());
        Assertions.assertEquals(result.getScore(), entity.getScore());
        Assertions.assertEquals(result.getTime(), entity.getTime());
    }

    @Test
    public void Save_GivenEmptyPlayer_ThrowException() {
        // Arrange
        ScoreEntity entity = new ScoreEntity("", "".toUpperCase(), 1, new Date());

        // Act
        // Assert
        Assertions.assertThrows(TransactionSystemException.class,() -> repository.save(entity));
    }

    @Test
    public void Save_GivenNullPlayer_ThrowException() {
        // Arrange
        ScoreEntity entity = new ScoreEntity(null, "".toUpperCase(), 1, new Date());

        // Act
        // Assert
        Assertions.assertThrows(TransactionSystemException.class,() -> repository.save(entity));
    }

    @Test
    public void Save_GivenNegativeScore_ThrowException(){
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), -1, new Date());

        // Act
        // Assert
        Assertions.assertThrows(TransactionSystemException.class,() -> repository.save(entity));
    }

    @Test
    public void ExistsById_GivenValidId_ReturnTrue(){
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());

        // Act
        ScoreEntity result = repository.save(entity);

        // Assert
        Assertions.assertTrue(repository.existsById(result.getId()));
    }

    @Test
    public void ExistsById_GivenInvalidId_ReturnFalse()
    {
        // Arrange
        long fakeId = 1;

        // Act
        // Assert
        Assertions.assertFalse(repository.existsById(fakeId));
    }

    @Test
    public void FindById_GivenInvalidId_ReturnFalse() {
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());

        // Act
        ScoreEntity result = repository.save(entity);
        Optional<ScoreEntity> findById = repository.findById(result.getId());

        // Assert
        Assertions.assertFalse(findById.isEmpty());
        Assertions.assertEquals(findById.get(), result);
    }

    @Test
    public void FindById_GivenInvalidId_ReturnScoreForThisId()
    {
        // Arrange
        long fakeId = 1;

        // Act
        Optional<ScoreEntity> findById = repository.findById(fakeId);

        // Assert
        Assertions.assertTrue(findById.isEmpty());
    }

    @Test
    public void DeleteById_GivenValidId_DeleteScoreForThisId()
    {
        // Arrange
        ScoreEntity entity = new ScoreEntity("test", "test".toUpperCase(), 1, new Date());

        // Act
        ScoreEntity result = repository.save(entity);

        // Assert
        Assertions.assertDoesNotThrow(() ->repository.deleteById(result.getId()));
    }

    @Test
    public void DeleteById_GivenInvalidId_ThrowException()
    {
        // Arrange
        long fakeId = 1;

        // Act
        // Assert
        Assertions.assertThrows(EmptyResultDataAccessException.class,() -> repository.deleteById(fakeId));
    }

    @Test
    public void FindRecordsByName_GivenValidName_ReturnRecordsForThisName()
    {
        // Arrange
        String nameToSearch = "test1";
        String nameToSearch2 = "tESt1";
        List<ScoreEntity> entities = new ArrayList<>();
        entities.add(new ScoreEntity(nameToSearch, nameToSearch.toUpperCase(), 1, new Date()));
        entities.add(new ScoreEntity(nameToSearch2, nameToSearch2.toUpperCase(), 10, new Date()));
        entities.add(new ScoreEntity("test2", "test2".toUpperCase(), 1, new Date()));
        Iterable<ScoreEntity> result = repository.saveAll(entities);

        // Act
        Optional<List<ScoreEntity>> records = repository.findRecordsByName(nameToSearch.toUpperCase());

        // Assert
        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(records.get().size(), 2);
    }

    @Test
    public void FindRecordsByName_GivenInvalidName_ReturnEmpty()
    {
        // Arrange
        String nameToSearch = "test1";

        // Act
        Optional<List<ScoreEntity>> records = repository.findRecordsByName(nameToSearch.toUpperCase());

        // Assert
        Assertions.assertTrue(records.isEmpty());
    }

    @Test
    public void FindAll_GivenValidPage_ReturnPage()
    {
        // Arrange
        List<ScoreEntity> entities = new ArrayList<>();
        entities.add(new ScoreEntity("test1", "test1".toUpperCase(), 1, new Date()));
        entities.add(new ScoreEntity("test1", "test1".toUpperCase(), 10, new Date()));
        entities.add(new ScoreEntity("test2", "test2".toUpperCase(), 1, new Date()));
        Iterable<ScoreEntity> result = repository.saveAll(entities);
        Pageable page = PageRequest.of(0, 10, Sort.by("score").descending());

        // Act
        Page<ScoreEntity> records = repository.findAll(page);

        // Assert
        Assertions.assertFalse(records.isEmpty());
        Assertions.assertEquals(records.get().count(), entities.size());
    }

    @Test
    public void FindAll_GivenValidButOutOfRangePage_ReturnPageWithEmptyContent()
    {
        // Arrange
        List<ScoreEntity> entities = new ArrayList<>();
        entities.add(new ScoreEntity("test1", "test1".toUpperCase(), 1, new Date()));
        entities.add(new ScoreEntity("test1", "test1".toUpperCase(), 10, new Date()));
        entities.add(new ScoreEntity("test2", "test2".toUpperCase(), 1, new Date()));
        Iterable<ScoreEntity> result = repository.saveAll(entities);
        Pageable page = PageRequest.of(20, 10, Sort.by("score").descending());

        // Act
        Page<ScoreEntity> records = repository.findAll(page);

        // Assert
        Assertions.assertTrue(records.isEmpty());
    }

    @Test
    public void FindAll_GivenInvalidSortExpression_ThrowPropertyReferenceException()
    {
        // Arrange
        Pageable page = PageRequest.of(0, 10, Sort.by("notValid").descending());

        // Act
        // Assert
        Assertions.assertThrows(PropertyReferenceException.class,() -> repository.findAll(page));
    }
}
