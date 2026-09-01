# Tennis Ladder Management System

A role-based web application for managing tennis ladder rankings, player challenges, match results, and external tournament results.

I built this project to model situations I have encountered as a competitive tennis player and coach. It provides separate player and coach workflows while automatically maintaining an ordered ladder after match results are recorded.

## Features

### Player Features

- View current ladder rankings
- Request a match against a player ranked up to three positions higher
- Track pending and approved challenges
- View completed ladder and tournament matches

### Coach Features

- Approve or reject challenge requests
- Record match winners and scores
- Add results from external tournaments
- Add new players to the ladder
- Access protected coach-only pages and actions

### Ranking System

When a lower-ranked player defeats a higher-ranked player:

1. The winner takes the defeated player’s position.
2. Players between the two positions move down one place.
3. The result is saved in match history.

If the higher-ranked player wins, the rankings remain unchanged.

Tournament matches can occur between any two players and are not restricted by the normal three-position challenge rule.

## Security

The application uses Spring Security with two roles:

- `PLAYER` — can view the ladder and request challenges
- `COACH` — can manage players, approve requests and record results

Coach routes are protected on the server. A player cannot access them by manually entering a coach URL.

Passwords are hashed with BCrypt, and deployment passwords can be supplied through environment variables.

## Technology

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven
- HTML and CSS
- Git and GitHub

## Application Structure

```text
Browser
   |
Controllers
   |
Service and ranking logic
   |
JPA repositories
   |
H2 database
```

The service layer contains the ranking algorithm and uses database transactions so ranking changes and match results are saved together.

## Running the Project Locally

### Requirements

- Java 25 or a compatible JDK
- Git

### Setup

Clone the repository:

```bash
git clone https://github.com/shriyandagg/tennis-ladder.git
cd tennis-ladder
```

Start the application with the included Maven wrapper:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

### Local Demo Accounts

Player account:

```text
Username: player
Password: player123
```

Coach account:

```text
Username: coach
Password: coach123
```

For non-demo environments, set private passwords before starting the application:

```bash
export PLAYER_PASSWORD="your-player-password"
export COACH_PASSWORD="your-coach-password"
./mvnw spring-boot:run
```

## Data Storage

The application uses a file-based H2 database during local development. Players, rankings, challenges and match results remain available after restarting the application.

Local database files are excluded from Git.

## Current Status

The main player, coach, challenge, tournament-result, ranking and security workflows are functional.

Planned improvements include:

- Individual player accounts
- Automated ranking tests
- PostgreSQL production database
- Public deployment
- Expanded player statistics
- Improved score validation

## Author

**Shriyan Daggumalli**

Computer Science student at The University of Texas at Dallas

[GitHub Profile](https://github.com/shriyandagg)
