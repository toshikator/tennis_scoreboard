package pro.bukhman.model;

import pro.bukhman.exception.MatchIsAlreadyFinishedException;
import pro.bukhman.exception.MatchIsNotFinishedYetException;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.entity.OngoingMatchSnapshot;
import pro.bukhman.model.entity.Player;

import java.time.LocalDateTime;

public class OngoingMatch {

    private static final int GAMES_TO_WIN_SET = 6;
    private static final int DEFAULT_POINTS_TO_WIN_GAME = 4;
    private static final int DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE = 2;
    private static final int SETS_TO_WIN_MATCH = 2;
    private final PlayerDto player1;
    private final PlayerDto player2;
    private final LocalDateTime startedAt = LocalDateTime.now();
    private int player1Points;
    private int player2Points;
    private int player1Sets;
    private int player2Sets;
    private int player1Games;
    private int player2Games;
    private boolean tieBreak;
    private boolean isFinished;
    private PlayerDto winner;
    private int pointsToWinGame = DEFAULT_POINTS_TO_WIN_GAME;
    private int gamesToWinSetDifference = DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE;

    public OngoingMatch(PlayerDto player1, PlayerDto player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public synchronized OngoingMatchSnapshot getSnapshot() {
        OngoingMatchSnapshot ongoingMatchSnapshot = new OngoingMatchSnapshot();
        ongoingMatchSnapshot.setGAMES_TO_WIN_SET(GAMES_TO_WIN_SET);
        ongoingMatchSnapshot.setDEFAULT_POINTS_TO_WIN_GAME(DEFAULT_POINTS_TO_WIN_GAME);
        ongoingMatchSnapshot.setDEFAULT_GAMES_TO_WIN_SET_DIFFERENCE(DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE);
        ongoingMatchSnapshot.setSETS_TO_WIN_MATCH(SETS_TO_WIN_MATCH);
        ongoingMatchSnapshot.setPlayer1(player1);
        ongoingMatchSnapshot.setPlayer2(player2);
        ongoingMatchSnapshot.setPlayer1Points(player1Points);
        ongoingMatchSnapshot.setPlayer2Points(player2Points);
        ongoingMatchSnapshot.setPlayer1Sets(player1Sets);
        ongoingMatchSnapshot.setPlayer2Sets(player2Sets);
        ongoingMatchSnapshot.setPlayer1Games(player1Games);
        ongoingMatchSnapshot.setPlayer2Games(player2Games);
        ongoingMatchSnapshot.setTieBreak(tieBreak);
        ongoingMatchSnapshot.setFinished(isFinished);
        ongoingMatchSnapshot.setWinner(winner);
        ongoingMatchSnapshot.setPointsToWinGame(pointsToWinGame);
        ongoingMatchSnapshot.setGamesToWinSetDifference(gamesToWinSetDifference);
        return ongoingMatchSnapshot;
    }

    protected synchronized void addPoint(PlayerDto player) {
        if (isFinished) {
            throw new MatchIsAlreadyFinishedException("Match is already finished");
        }

        if (player.equals(player1)) {
            player1Points++;
            if (player1Points >= pointsToWinGame && player1Points - player2Points >= 2) {
                addGame(player1);
                resetPoints();
            }
        } else if (player.equals(player2)) {
            player2Points++;
            if (player2Points >= pointsToWinGame && player2Points - player1Points >= 2) {
                addGame(player2);
                resetPoints();
            }
        } else {
            throw new IllegalArgumentException("Unknown player");
        }
    }

    private void addGame(PlayerDto player) {
        if (isFinished) {
            throw new MatchIsAlreadyFinishedException("MatchIsAlreadyFinishedException: it's finished");
        }

        if (player.equals(player1)) {
            player1Games++;
            if (player1Games >= GAMES_TO_WIN_SET && player1Games - player2Games >= gamesToWinSetDifference) {
                addSet(player1);
                resetGames();
            }
        } else if (player.equals(player2)) {
            player2Games++;
            if (player2Games >= GAMES_TO_WIN_SET && player2Games - player1Games >= gamesToWinSetDifference) {
                addSet(player2);
                resetGames();
            }
        } else {
            throw new IllegalArgumentException("Unknown player");
        }
        if (player1Games == 6 && player2Games == 6) {
            tieBreak = true;
            pointsToWinGame = 7;
            gamesToWinSetDifference = 1;
        }
    }

    private void resetPoints() {
        player1Points = 0;
        player2Points = 0;
    }

    private void addSet(PlayerDto player) {
        if (isFinished) {
            throw new MatchIsAlreadyFinishedException("MatchIsAlreadyFinishedException: it's finished");
        }
        if (player.equals(player1)) {
            player1Sets++;
            if (player1Sets >= SETS_TO_WIN_MATCH) {
                isFinished = true;
                winner = player1;
            }
        } else if (player.equals(player2)) {
            player2Sets++;
            if (player2Sets >= SETS_TO_WIN_MATCH) {
                isFinished = true;
                winner = player2;
            }
        }
        resetTieBreak();
    }

    private synchronized void resetGames() {
        player1Games = 0;
        player2Games = 0;
    }

    private synchronized void resetTieBreak() {
        tieBreak = false;
        pointsToWinGame = DEFAULT_POINTS_TO_WIN_GAME;
        gamesToWinSetDifference = DEFAULT_GAMES_TO_WIN_SET_DIFFERENCE;
    }

    public synchronized PlayerDto getPlayer1() {
        if (!isFinished) {
            throw new MatchIsNotFinishedYetException("Match is not finished yet");
        }
        return player1;
    }

    public synchronized PlayerDto getPlayer2() {
        if (!isFinished) {
            throw new MatchIsNotFinishedYetException("Match is not finished yet");
        }
        return player2;
    }

    public synchronized PlayerDto getWinner() {
        if (!isFinished) {
            throw new MatchIsNotFinishedYetException("Match is not finished yet");
        }
        return winner;
    }
}