# Tennis Scoreboard

![Java](https://img.shields.io/badge/Java-25-orange)
![Jakarta Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.1-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-7.3-yellow)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36)

Backend API for a tennis scoreboard application.

The application manages players, creates tennis matches, calculates scores according to tennis rules, and stores completed match results.

**Frontend repository:**
[github.com/toshikator/TennisScoreboardFront](https://github.com/toshikator/TennisScoreboardFront)

## About the Project

Tennis Scoreboard is a Java web application built with Jakarta Servlets without Spring.

The backend provides an HTTP API used by a separate frontend application. It is responsible for:

* creating and retrieving players;
* starting matches between existing players;
* processing points during active matches;
* calculating games, sets, deuce, advantage, and tie-breaks;
* storing active matches in memory;
* saving completed matches to PostgreSQL;
* filtering and paginating players and match history;
* returning data and errors in JSON format.

## Features

* Player creation and retrieval
* Player search by first name and last name
* Paginated player lists
* Match creation between existing players
* Live match score updates
* Standard tennis point calculation
* Deuce and advantage handling
* Tie-break handling
* Best-of-three-sets matches
* Completed match history
* Match filtering by player
* Paginated match history
* Request validation
* JSON responses
* Application logging
* Unit-tested scoring logic
* Thread-safe active match storage

## Technologies

| Technology          | Purpose                             |
| ------------------- | ----------------------------------- |
| Java 25             | Main programming language           |
| Jakarta Servlet 6.1 | HTTP request and response handling  |
| Hibernate ORM 7.3   | Persistence and database operations |
| Jakarta Persistence | Entity mapping                      |
| PostgreSQL          | Persistent data storage             |
| Jackson             | JSON serialization                  |
| Log4j2              | Application logging                 |
| JUnit               | Automated testing                   |
| Maven               | Dependency and build management     |

## Architecture

The application follows a layered architecture:

```text
HTTP Request
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Hibernate
     │
     ▼
PostgreSQL
```

### Main Packages

```text
pro.bukhman
├── controller
├── exception
├── initializing
├── model
│   ├── dto
│   └── entity
├── ongoingMatchStorage
├── repo
├── service
├── util
└── validation
```

* **Controller** — accepts HTTP requests and returns JSON responses.
* **Service** — contains application and tennis scoring logic.
* **Repository** — provides access to persistent data.
* **Entity** — represents database entities.
* **DTO** — represents data returned by the API.
* **Validation** — validates request parameters.
* **Ongoing match storage** — stores active matches in memory.
* **Exception** — contains application-specific exceptions.
* **Initializing** — initializes application resources.

## Match Lifecycle

1. Two existing players are selected.
2. The backend creates a new match and assigns it a UUID.
3. The active match is stored in memory.
4. Points are added through the score endpoint.
5. The backend calculates points, games, sets, and tie-breaks.
6. When the match finishes, its result is saved to PostgreSQL.
7. The completed match becomes available in the match history.

## Tennis Scoring

The application implements the following rules:

* A match is played as the best of three sets.
* The first player to win two sets wins the match.
* Regular games use the `0–15–30–40` scoring system.
* A player must win a regular game by two points.
* Deuce and advantage are supported.
* A set is normally won at six games with a two-game lead.
* A tie-break begins when the game score reaches `6–6`.
* A tie-break is won at seven points with a two-point lead.

## API Endpoints

### Players

#### Get a player

```http
GET /player?id={playerId}
```

Returns a player by ID.

#### Create a player

```http
POST /player
```

Parameters:

| Parameter   | Description         |
| ----------- | ------------------- |
| `firstName` | Player's first name |
| `lastName`  | Player's last name  |

#### Get all players

```http
GET /players
```

#### Search players by first name

```http
GET /players?firstName={firstName}
```

#### Search players by last name

```http
GET /players?lastName={lastName}
```

#### Search players by full name

```http
GET /players?firstName={firstName}&lastName={lastName}
```

#### Get players with pagination

```http
GET /players?page={page}&limit={limit}
```

| Parameter | Description                |
| --------- | -------------------------- |
| `page`    | Requested page number      |
| `limit`   | Number of players per page |

---

### Active Matches

#### Create a match

```http
POST /new-match
```

Parameters:

| Parameter   | Description        |
| ----------- | ------------------ |
| `player1Id` | First player's ID  |
| `player2Id` | Second player's ID |

Example response:

```json
{
  "matchId": "08190361-3e93-47d3-8521-d09ee2d8996d"
}
```

#### Get the current match score

```http
GET /match-score?match_id={matchId}
```

| Parameter  | Description              |
| ---------- | ------------------------ |
| `match_id` | UUID of the active match |

#### Award a point

```http
POST /match-score
```

Parameters:

| Parameter             | Description                        |
| --------------------- | ---------------------------------- |
| `match_id`            | UUID of the active match           |
| `player_for_score_id` | ID of the player who won the point |

The response contains the updated match state.

---

### Completed Matches

#### Get all completed matches

```http
GET /matches
```

#### Get a match by ID

```http
GET /matches?id={matchId}
```

#### Search matches by player first name

```http
GET /matches?firstName={firstName}
```

#### Search matches by player last name

```http
GET /matches?lastName={lastName}
```

#### Search matches by player full name

```http
GET /matches?firstName={firstName}&lastName={lastName}
```

#### Get completed matches with pagination

```http
GET /matches?page={page}&limit={limit}
```

| Parameter | Description                |
| --------- | -------------------------- |
| `page`    | Requested page number      |
| `limit`   | Number of matches per page |

## Related Repository

* [Tennis Scoreboard Frontend](https://github.com/toshikator/TennisScoreboardFront)
