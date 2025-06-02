# World Cup Scoreboard
Simple Java library that simulates a live Football World Cup scoreboard.  
It supports basic operations such as starting a game, updating scores, finishing a game, and displaying a ranked summary of ongoing matches.


## Tech Stack

- Java 21
- JUnit 5
- Maven

## Assumptions

- Use Test-Driven Development (TDD) approach
- Use in-memory store solution

## Validations

- The match cannot start with null, blank or equal strings for the home and away teams.
- When updating results, teams cannot receive negative integer as their score.
- Due to specific of live scoreboard, only one team's score can be increased during a score update and only by 1.
- When updating results, score can be adjusted which means system can revert previous update operation (VAR systems supported 😉).
System cannot revert 2 update actions in a row. 
- Games with the same total score will be returned ordered by the most recently added to system.
- Team can have only 1 game in progress.