# Top Score Api

An Api to manage score for player

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

## API Documentation

### getScoreById
```
Endpoint : GET "/score/{scoreId}"

Short description:

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
```

### addScoreToSystem
```
Endpoint : POST "/score/add"
Content-Type: application/json
Body:
{
  "player": "playerName",
  "score": "score",
  "time": "time" // format "dd/MM/yyyy hh:mm:ss"
}

Short description:

    Add a new score in the system

        Parameters:
             score -> new ScoreDto to add to the system
                - ScoreDto ->
                    - id = Score id
                    - player = Player Name
                    - score = score for the record
                    - time = time of the record with format "dd/MM/yyyy hh:mm:ss"

        Response Code:
            201 -> return the url location of the created user as well as the data
```

### deleteScoreById
```
Endpoint : DELETE "/score/{scoreId}"

Short description:

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
```

### getScores
```
Endpoint : GET "/score/list?{page=&size=&sort=}"

Short description:

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
```

### getPlayerScoreHistoryByName
```
Endpoint : GET "/score/history/{player}"

Short description:

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
```

## Authors

* **Maire Edward**




    Instructions on how to build and run your app.
    Instruction on how to run unit and integration tests.
    Quick documentation of your API.

